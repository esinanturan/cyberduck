package ch.cyberduck.core.dav;

import ch.cyberduck.core.AlphanumericRandomStringService;
import ch.cyberduck.core.DisabledLoginCallback;
import ch.cyberduck.core.Path;
import ch.cyberduck.core.exception.InteroperabilityException;
import ch.cyberduck.core.exception.NotfoundException;
import ch.cyberduck.core.features.Delete;
import ch.cyberduck.core.shared.DefaultHomeFinderService;
import ch.cyberduck.core.transfer.TransferStatus;
import ch.cyberduck.test.IntegrationTest;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Collections;
import java.util.EnumSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Category(IntegrationTest.class)
public class DAVDeleteFeatureTest extends AbstractDAVTest {

    @Test
    public void testDeleteFile() throws Exception {
        final Path test = new Path(new DefaultHomeFinderService(session).find(), new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        new DAVTouchFeature(session).touch(test, new TransferStatus());
        assertTrue(new DAVFindFeature(session).find(test));
        new DAVDeleteFeature(session).delete(Collections.singletonMap(test, new TransferStatus()), new DisabledLoginCallback(), new Delete.DisabledCallback());
        assertFalse(new DAVFindFeature(session).find(test));
    }

    @Test
    public void testDeleteFileWithLock() throws Exception {
        final Path test = new Path(new DefaultHomeFinderService(session).find(), new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        new DAVTouchFeature(session).touch(test, new TransferStatus());
        String lock = null;
        try {
            lock = new DAVLockFeature(session).lock(test);
        }
        catch(InteroperabilityException e) {
            // Not supported
        }
        assertTrue(new DAVFindFeature(session).find(test));
        new DAVDeleteFeature(session).delete(Collections.singletonMap(test, new TransferStatus().setLockId(lock)), new DisabledLoginCallback(), new Delete.DisabledCallback());
        assertFalse(new DAVFindFeature(session).find(test));
    }

    @Test
    public void testDeleteDirectory() throws Exception {
        final Path test = new Path(new DefaultHomeFinderService(session).find(), new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.directory));
        new DAVDirectoryFeature(session).mkdir(test, new TransferStatus());
        assertTrue(new DAVFindFeature(session).find(test));
        new DAVTouchFeature(session).touch(new Path(test, new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file)), new TransferStatus());
        new DAVDeleteFeature(session).delete(Collections.singletonMap(test, new TransferStatus()), new DisabledLoginCallback(), new Delete.DisabledCallback());
        assertFalse(new DAVFindFeature(session).find(test));
    }

    @Test(expected = NotfoundException.class)
    public void testDeleteNotFound() throws Exception {
        final Path test = new Path(new DefaultHomeFinderService(session).find(), new AlphanumericRandomStringService().random(), EnumSet.of(Path.Type.file));
        new DAVDeleteFeature(session).delete(Collections.singletonMap(test, new TransferStatus()), new DisabledLoginCallback(), new Delete.DisabledCallback());
    }
}
