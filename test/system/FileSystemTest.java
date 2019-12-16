package system;

import org.junit.Assert;
import org.junit.Test;

public class FileSystemTest {

//constructor
    @Test
    public void fileSystemConstructorShouldReturnFileSystemObject() {
        FileSystem fileSystem = new FileSystem(1);
        Assert.assertNotNull(fileSystem);
    }

    @Test
    public void fileSystemConstructorShouldCreateSpaceObject() {
        FileSystem fileSystem = new FileSystem(1);
        Assert.assertNotNull(fileSystem.fileStorage);
    }

    //dir
    @Test (expected = BadFileNameException.class)
    public void dirFunctionShouldThrowExceptionWhenFirstNameNotRoot() throws BadFileNameException {
            FileSystem fileSystem = new FileSystem(1);
            String[] mockPathNames = new String[]{"noRoot"};

            fileSystem.dir(mockPathNames);
    }

    @Test (expected = BadFileNameException.class)
    public void dirFunctionShouldThrowExceptionWhenFileExists() throws BadFileNameException {
        FileSystem fileSystem = new FileSystem(1);
        String[] mockPathNames = new String[]{"root", "file exists"};

        fileSystem.dir(mockPathNames);
    }

}
