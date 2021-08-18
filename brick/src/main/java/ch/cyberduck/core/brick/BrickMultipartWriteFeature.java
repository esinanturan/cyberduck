package ch.cyberduck.core.brick;/*
 * Copyright (c) 2002-2021 iterate GmbH. All rights reserved.
 * https://cyberduck.io/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

import ch.cyberduck.core.ConnectionCallback;
import ch.cyberduck.core.DefaultIOExceptionMappingService;
import ch.cyberduck.core.DisabledConnectionCallback;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.brick.io.swagger.client.ApiException;
import ch.cyberduck.core.brick.io.swagger.client.api.FileActionsApi;
import ch.cyberduck.core.brick.io.swagger.client.api.FilesApi;
import ch.cyberduck.core.brick.io.swagger.client.model.BeginUploadPathBody;
import ch.cyberduck.core.brick.io.swagger.client.model.FileUploadPartEntity;
import ch.cyberduck.core.brick.io.swagger.client.model.FilesPathBody;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.InteroperabilityException;
import ch.cyberduck.core.features.MultipartWrite;
import ch.cyberduck.core.http.HttpResponseOutputStream;
import ch.cyberduck.core.io.DefaultStreamCloser;
import ch.cyberduck.core.io.MemorySegementingOutputStream;
import ch.cyberduck.core.preferences.HostPreferences;
import ch.cyberduck.core.threading.BackgroundExceptionCallable;
import ch.cyberduck.core.threading.DefaultRetryCallable;
import ch.cyberduck.core.transfer.TransferStatus;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class BrickMultipartWriteFeature implements MultipartWrite<Void> {
    private static final Logger log = Logger.getLogger(BrickMultipartWriteFeature.class);

    private final BrickSession session;
    private final Integer partsize;

    public BrickMultipartWriteFeature(final BrickSession session) {
        this(session, new HostPreferences(session.getHost()).getInteger("brick.upload.multipart.size"));
    }

    public BrickMultipartWriteFeature(final BrickSession session, final Integer partsize) {
        this.session = session;
        this.partsize = partsize;
    }

    @Override
    public HttpResponseOutputStream<Void> write(final Path file, final TransferStatus status, final ConnectionCallback callback) throws BackgroundException {
        final MultipartOutputStream proxy = new MultipartOutputStream(file, status);
        return new HttpResponseOutputStream<Void>(new MemorySegementingOutputStream(proxy, partsize)) {
            @Override
            public Void getStatus() {
                return null;
            }
        };
    }

    @Override
    public Append append(final Path file, final TransferStatus status) throws BackgroundException {
        return new Append(false).withStatus(status);
    }

    private final class MultipartOutputStream extends OutputStream {
        private final BrickWriteFeature writer = new BrickWriteFeature(session);
        private final Path file;
        private final TransferStatus overall;
        private final AtomicBoolean close = new AtomicBoolean();
        private String ref = null;
        private int partNumber;
        private final List<TransferStatus> checksums = new ArrayList<>();

        public MultipartOutputStream(final Path file, final TransferStatus status) {
            this.file = file;
            this.overall = status;
        }

        @Override
        public void write(final int value) throws IOException {
            throw new IOException(new UnsupportedOperationException());
        }

        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException {
            try {
                partNumber++;
                checksums.add(new DefaultRetryCallable<>(session.getHost(), new BackgroundExceptionCallable<TransferStatus>() {
                    @Override
                    public TransferStatus call() throws BackgroundException {
                        final List<FileUploadPartEntity> uploadPartEntities;
                        try {
                            uploadPartEntities = new FileActionsApi(new BrickApiClient(session.getApiKey(), session.getClient()))
                                .beginUpload(StringUtils.removeStart(file.getAbsolute(), String.valueOf(Path.DELIMITER)), new BeginUploadPathBody().ref(ref).part(partNumber));
                        }
                        catch(ApiException e) {
                            throw new BrickExceptionMappingService().map("Upload {0} failed", e, file);
                        }
                        for(FileUploadPartEntity uploadPartEntity : uploadPartEntities) {
                            final TransferStatus status = new TransferStatus().withLength(len);
                            status.setChecksum(writer.checksum(file, status).compute(new ByteArrayInputStream(b, off, len), status));
                            status.setUrl(uploadPartEntity.getUploadUri());
                            status.setSegment(true);
                            final HttpResponseOutputStream<Void> proxy = writer.write(file, status, new DisabledConnectionCallback());
                            final byte[] content = Arrays.copyOfRange(b, off, len);
                            try {
                                IOUtils.write(content, proxy);
                            }
                            catch(IOException e) {
                                throw new DefaultIOExceptionMappingService().map("Upload {0} failed", e, file);
                            }
                            finally {
                                new DefaultStreamCloser().close(proxy);
                            }
                            ref = uploadPartEntity.getRef();
                            return status;
                        }
                        throw new InteroperabilityException();
                    }
                }, overall).call());
            }
            catch(BackgroundException e) {
                throw new IOException(e.getMessage(), e);
            }
        }

        @Override
        public void close() throws IOException {
            try {
                if(close.get()) {
                    log.warn(String.format("Skip double close of stream %s", this));
                    return;
                }
                if(null == ref) {
                    new BrickTouchFeature(session).touch(file, new TransferStatus());
                }
                else {
                    try {
                        new FilesApi(new BrickApiClient(session.getApiKey(), session.getClient())).postFilesPath(new FilesPathBody()
                            .providedMtime(new DateTime(overall.getTimestamp()))
                            .etagsEtag(checksums.stream().map(s -> s.getChecksum().hash).collect(Collectors.toList()))
                            .etagsPart(checksums.stream().map(TransferStatus::getPart).collect(Collectors.toList()))
                            .ref(ref)
                            .action("end"), StringUtils.removeStart(file.getAbsolute(), String.valueOf(Path.DELIMITER)));
                    }
                    catch(ApiException e) {
                        throw new IOException(e.getMessage(), new BrickExceptionMappingService().map(e));
                    }
                    if(log.isDebugEnabled()) {
                        log.debug(String.format("Completed multipart upload for %s", file));
                    }
                }
            }
            catch(BackgroundException e) {
                throw new IOException(e);
            }
            finally {
                close.set(true);
            }
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MultipartOutputStream{");
            sb.append("file=").append(file);
            sb.append('}');
            return sb.toString();
        }
    }
}