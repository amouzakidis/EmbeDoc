import org.amouzakidis.embedoc.vfs.VirtualFileSystem;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class TestVFS {

    @Test
    public void testVFS() throws IOException {
        String testFilePath = "./file.data";

        VirtualFileSystem vfs = new VirtualFileSystem(testFilePath, 4096);
        System.out.println("All ok with creating new file");

        vfs = new VirtualFileSystem(testFilePath, 4096);
        System.out.println("All ok with opening existing file");

        File f = new File(testFilePath);
        Assert.assertTrue( f.exists() );
        f.delete();
    }

}
