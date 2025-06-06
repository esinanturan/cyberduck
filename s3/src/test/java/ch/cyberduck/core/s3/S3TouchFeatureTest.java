package ch.cyberduck.core.s3;

import ch.cyberduck.core.AsciiRandomStringService;
import ch.cyberduck.core.DisabledLoginCallback;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.PathAttributes;
import ch.cyberduck.core.exception.AccessDeniedException;
import ch.cyberduck.core.features.Delete;
import ch.cyberduck.core.features.Encryption;
import ch.cyberduck.core.features.Home;
import ch.cyberduck.core.shared.DefaultAttributesFinderFeature;
import ch.cyberduck.core.shared.DefaultFindFeature;
import ch.cyberduck.core.transfer.TransferStatus;
import ch.cyberduck.test.IntegrationTest;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Collections;
import java.util.EnumSet;

import static org.junit.Assert.*;

@Category(IntegrationTest.class)
public class S3TouchFeatureTest extends AbstractS3Test {

    @Test
    public void testFile() {
        final S3AccessControlListFeature acl = new S3AccessControlListFeature(session);
        assertFalse(new S3TouchFeature(session, acl).isSupported(Home.root(), StringUtils.EMPTY));
        assertTrue(new S3TouchFeature(session, acl).isSupported(new Path(Home.root(), "/container", EnumSet.of(Path.Type.volume, Path.Type.directory)), StringUtils.EMPTY));
        assertTrue(new S3TouchFeature(virtualhost, acl).isSupported(Home.root(), StringUtils.EMPTY));
        assertTrue(new S3TouchFeature(virtualhost, acl).isSupported(new Path(Home.root(), "/container", EnumSet.of(Path.Type.volume, Path.Type.directory)), StringUtils.EMPTY));
    }

    @Test
    public void testTouch() throws Exception {
        final Path container = new Path("test-eu-central-1-cyberduck", EnumSet.of(Path.Type.volume, Path.Type.directory));
        final S3AccessControlListFeature acl = new S3AccessControlListFeature(session);
        final S3TouchFeature feature = new S3TouchFeature(session, acl);
        final String filename = new AsciiRandomStringService().random();
        assertFalse(feature.isSupported(Home.root(), filename));
        assertTrue(feature.isSupported(container, filename));
        final Path test = feature.touch(new Path(container, filename, EnumSet.of(Path.Type.file)), new TransferStatus());
        assertNull(test.attributes().getVersionId());
        assertTrue(new S3FindFeature(session, acl).find(test));
        assertEquals(test.attributes(), new S3AttributesFinderFeature(session, acl).find(test));
        assertEquals(test.attributes(), new DefaultAttributesFinderFeature(session).find(test));
        new S3DefaultDeleteFeature(session, acl).delete(Collections.singletonList(test), new DisabledLoginCallback(), new Delete.DisabledCallback());
        assertFalse(new S3FindFeature(session, acl).find(test));
    }

    @Test
    public void testTouchVirtualHost() throws Exception {
        final S3AccessControlListFeature acl = new S3AccessControlListFeature(session);
        final S3TouchFeature feature = new S3TouchFeature(virtualhost, acl);
        final String filename = new AsciiRandomStringService().random();
        assertTrue(feature.isSupported(Home.root(), filename));
        final Path test = feature.touch(new Path(filename, EnumSet.of(Path.Type.file)), new TransferStatus());
        assertNull(test.attributes().getVersionId());
        assertTrue(new S3FindFeature(virtualhost, acl).find(test));
        assertEquals(test.attributes(), new S3AttributesFinderFeature(virtualhost, acl).find(test));
        assertEquals(test.attributes(), new DefaultAttributesFinderFeature(virtualhost).find(test));
        new S3DefaultDeleteFeature(virtualhost, acl).delete(Collections.singletonList(test), new DisabledLoginCallback(), new Delete.DisabledCallback());
        assertFalse(new S3FindFeature(virtualhost, acl).find(test));
    }

    @Test
    public void testTouchCarriageReturnKey() throws Exception {
        final Path container = new Path("test-eu-central-1-cyberduck", EnumSet.of(Path.Type.volume, Path.Type.directory));
        final S3AccessControlListFeature acl = new S3AccessControlListFeature(session);
        final Path test = new S3TouchFeature(session, acl).touch(
                new Path(container, String.format("%s\n-\r", new AsciiRandomStringService().random()), EnumSet.of(Path.Type.file)), new TransferStatus());
        assertNull(test.attributes().getVersionId());
        assertTrue(new S3FindFeature(session, acl).find(test));
        assertEquals(test.attributes(), new DefaultAttributesFinderFeature(session).find(test));
        assertEquals(test.attributes(), new S3AttributesFinderFeature(session, acl).find(test));
        new S3DefaultDeleteFeature(session, acl).delete(Collections.singletonList(test), new DisabledLoginCallback(), new Delete.DisabledCallback());
        assertFalse(new S3FindFeature(session, acl).find(test));
    }

    @Test
    public void testTouchUriEncoding() throws Exception {
        final Path container = new Path("test-eu-central-1-cyberduck", EnumSet.of(Path.Type.volume, Path.Type.directory));
        final S3AccessControlListFeature acl = new S3AccessControlListFeature(session);
        final Path test = new S3TouchFeature(session, acl).touch(
                new Path(container, String.format("%s-+*~@([", new AsciiRandomStringService().random()), EnumSet.of(Path.Type.file)), new TransferStatus());
        assertNull(test.attributes().getVersionId());
        assertTrue(new S3FindFeature(session, acl).find(test));
        assertEquals(test.attributes(), new S3AttributesFinderFeature(session, acl).find(test));
        new S3DefaultDeleteFeature(session, acl).delete(Collections.singletonList(test), new DisabledLoginCallback(), new Delete.DisabledCallback());
        assertFalse(new S3FindFeature(session, acl).find(test));
    }

    @Test
    public void testTouchVersioning() throws Exception {
        final Path container = new Path("versioning-test-eu-central-1-cyberduck", EnumSet.of(Path.Type.directory, Path.Type.volume));
        final Path file = new Path(container, new AsciiRandomStringService().random(), EnumSet.of(Path.Type.file));
        final S3AccessControlListFeature acl = new S3AccessControlListFeature(session);
        final String version1 = new S3TouchFeature(session, acl).touch(file, new TransferStatus()).attributes().getVersionId();
        assertNotNull(version1);
        assertEquals(version1, new S3AttributesFinderFeature(session, acl).find(file).getVersionId());
        final String version2 = new S3TouchFeature(session, acl).touch(file, new TransferStatus()).attributes().getVersionId();
        assertNotNull(version2);
        assertEquals(version2, new S3AttributesFinderFeature(session, acl).find(file).getVersionId());
        assertTrue(new S3FindFeature(session, acl).find(file));
        assertTrue(new DefaultFindFeature(session).find(file));
        assertTrue(new DefaultFindFeature(session).find(new Path(file.getParent(), file.getName(), file.getType(),
                new PathAttributes(file.attributes()).setVersionId(version1))));
        assertTrue(new DefaultFindFeature(session).find(new Path(file.getParent(), file.getName(), file.getType(),
                new PathAttributes(file.attributes()).setVersionId(version2))));
        assertTrue(new S3FindFeature(session, acl).find(new Path(file.getParent(), file.getName(), file.getType(),
                new PathAttributes(file.attributes()).setVersionId(version1))));
        assertTrue(new S3FindFeature(session, acl).find(new Path(file.getParent(), file.getName(), file.getType(),
                new PathAttributes(file.attributes()).setVersionId(version2))));
        new S3DefaultDeleteFeature(session, acl).delete(Collections.singletonList(new Path(file).withAttributes(PathAttributes.EMPTY)), new DisabledLoginCallback(), new Delete.DisabledCallback());
        // Versioned files are not deleted but with delete marker added
        assertTrue(new DefaultFindFeature(session).find(new Path(file.getParent(), file.getName(), file.getType(),
                new PathAttributes(file.attributes()).setVersionId(version1))));
        assertTrue(new DefaultFindFeature(session).find(new Path(file.getParent(), file.getName(), file.getType(),
                new PathAttributes(file.attributes()).setVersionId(version2))));
        assertTrue(new S3FindFeature(session, acl).find(new Path(file.getParent(), file.getName(), file.getType(),
                new PathAttributes(file.attributes()).setVersionId(version1))));
        assertTrue(new S3FindFeature(session, acl).find(new Path(file.getParent(), file.getName(), file.getType(),
                new PathAttributes(file.attributes()).setVersionId(version2))));
    }

    @Test(expected = AccessDeniedException.class)
    public void testFailureWithServerSideEncryptionBucketPolicy() throws Exception {
        final Path container = new Path("sse-test-us-east-1-cyberduck", EnumSet.of(Path.Type.directory, Path.Type.volume));
        final Path test = new Path(container, new AsciiRandomStringService().random(), EnumSet.of(Path.Type.file));
        final S3TouchFeature touch = new S3TouchFeature(session, new S3AccessControlListFeature(session));
        final TransferStatus status = new TransferStatus();
        status.setEncryption(Encryption.Algorithm.NONE);
        touch.touch(test, status);
    }

    @Test
    public void testSuccessWithServerSideEncryptionBucketPolicy() throws Exception {
        final Path container = new Path("sse-test-us-east-1-cyberduck", EnumSet.of(Path.Type.directory, Path.Type.volume));
        final Path test = new Path(container, new AsciiRandomStringService().random(), EnumSet.of(Path.Type.file));
        final S3TouchFeature touch = new S3TouchFeature(session, new S3AccessControlListFeature(session));
        final TransferStatus status = new TransferStatus();
        status.setEncryption(S3EncryptionFeature.SSE_AES256);
        touch.touch(test, status);
    }
}
