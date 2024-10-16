package ch.cyberduck.core.ctera;

/*
 * Copyright (c) 2002-2024 iterate GmbH. All rights reserved.
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

import ch.cyberduck.core.LocaleFactory;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.dav.DAVWriteFeature;
import ch.cyberduck.core.exception.BackgroundException;
import ch.cyberduck.core.exception.InvalidFilenameException;

import java.text.MessageFormat;

import static ch.cyberduck.core.ctera.CteraAttributesFinderFeature.WRITEPERMISSION;
import static ch.cyberduck.core.ctera.CteraAttributesFinderFeature.assumeRole;
import static ch.cyberduck.core.ctera.CteraTouchFeature.validate;

public class CteraWriteFeature extends DAVWriteFeature {

    public CteraWriteFeature(final CteraSession session) {
        super(session);
    }

    @Override
    public void preflight(Path file) throws BackgroundException {
        if(!validate(file.getName())) {
            throw new InvalidFilenameException(MessageFormat.format(LocaleFactory.localizedString("Cannot create {0}", "Error"), file.getName()));
        }
        assumeRole(file, WRITEPERMISSION);
    }
}
