package system;

import org.junit.Assert;
import org.junit.Test;
import sun.reflect.FieldAccessor;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.DirectoryNotEmptyException;
import java.util.Arrays;

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
        FileSystem fileSystem = new FileSystem(4);

        String[] mockDirName = new String[]{"root", "dir"};
        String[] mockFilePathName = new String[]{"root", "dir", "file"};


        fileSystem.dir(mockDirName);
        try {
            fileSystem.file( mockFilePathName, 3);
            fileSystem.dir(mockFilePathName);
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }
    }

    @Test
    public void dirFunctionShouldNotThrowExceptionWhenReceivedValidParas(){
        FileSystem fileSystem = new FileSystem(1);

        String[] mockPathNames = new String[]{"root", "dir"};

        try {
            fileSystem.dir(mockPathNames);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        }
    }

    //file
    @Test (expected = BadFileNameException.class)
    public void fileFunctionShouldThrowExceptionWhenFirstNameNotRoot() throws BadFileNameException {
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"noRoot", "file"};

        try {
            fileSystem.file(mockPathNames, 3);
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }
    }


    @Test (expected = OutOfSpaceException.class)
    public void whenNewFileOutOfSpaceFileFunctionShouldThrow() throws OutOfSpaceException {
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "file"};

        try {
            fileSystem.file(mockPathNames, 5);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        }
    }

    @Test (expected = BadFileNameException.class)
    public void fileFunctionShouldThrowIfExistDirInsteadOfFile() throws BadFileNameException {
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "dir"};
        fileSystem.dir(mockPathNames);

        try {
            fileSystem.file(mockPathNames, 3);
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }
    }

    @Test
    public void whenNewFileNotOutOfSpaceFileAfterRemovingExistingShouldNotThrow(){
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "file"};

        try {
            fileSystem.file(mockPathNames, 3);
            fileSystem.file(mockPathNames, 3);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }
    }

    //lsdir
    @Test
    public void lsdirFunctionShouldReturnNullIfDirDoseNotExist(){
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"nonExistingDir"};

        String[] fileList = fileSystem.lsdir(mockPathNames);

        Assert.assertNotNull(fileList);
    }

    @Test
    public void lsdirFunctionShouldReturnEmptyArrayIfDirIsEmpty(){
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "emptyDir"};
        try {
            fileSystem.dir(mockPathNames);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        }

        String[] fileList = fileSystem.lsdir(mockPathNames);

        Assert.assertArrayEquals(fileList, new String[]{});
    }


    @Test
    public void lsdirFunctionShouldReturnArrayOfDirsContentIfDirNotEmpty(){
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "dir"};
        String[] mockPathFileNames = new String[]{"root", "dir", "file"};
        String[] mockPathDirNames = new String[]{"root", "dir", "dir1"};

        try {
            fileSystem.dir(mockPathNames);
            fileSystem.dir(mockPathDirNames);
            fileSystem.file(mockPathFileNames,3;
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }

        String[] fileList = fileSystem.lsdir(mockPathNames);
        String[] expected = new String[]{"dir1", "file"};
        Assert.assertArrayEquals(fileList, expected);
    }

    //rmfile

    @Test
    public void rmfileShouldNotChangingWorkingTreeIfFileDoseNotExist(){
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "dir"};
        String[] mockPathFileNames = new String[]{"root", "dir", "file"};
        String[] mockPathDirNames = new String[]{"root", "dir", "dir1"};
        String[] mockUnexistingFile = new String[]{"root", "dir", "file1"};

        try {
            fileSystem.dir(mockPathNames);
            fileSystem.dir(mockPathDirNames);
            fileSystem.file(mockPathFileNames,3);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }
        String[] root = new String[]{"root"};
        String[] expected = new String[]{"dir1", "file"};

        fileSystem.rmfile(mockUnexistingFile);
        String[] fileList = fileSystem.lsdir(root);

        Assert.assertArrayEquals(fileList, expected);
    }

    @Test
    public void rmfileShouldDeleteFileIfExist(){
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "dir"};
        String[] mockPathFileNames = new String[]{"root", "dir", "file"};
        String[] mockPathDirNames = new String[]{"root", "dir", "dir1"};

        try {
            fileSystem.dir(mockPathNames);
            fileSystem.dir(mockPathDirNames);
            fileSystem.file(mockPathFileNames,3);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }
        String[] expected = new String[]{"dir1"};

        fileSystem.rmfile(mockPathFileNames);
        String[] fileList = fileSystem.lsdir(
                Arrays.copyOf(mockPathFileNames, mockPathFileNames.length-1));

        Assert.assertArrayEquals(fileList, expected);
    }

    @Test
    public void rmfileShouldNotDeleteDir(){
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "dir"};
        String[] mockPathFileNames = new String[]{"root", "dir", "file"};
        String[] mockPathDirNames = new String[]{"root", "dir", "dir1"};

        try {
            fileSystem.dir(mockPathNames);
            fileSystem.dir(mockPathDirNames);
            fileSystem.file(mockPathFileNames,3);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }
        String[] expected = new String[]{"dir1", "file"};

        fileSystem.rmfile(mockPathDirNames);
        String[] fileList = fileSystem.lsdir(
                Arrays.copyOf(mockPathDirNames, mockPathDirNames.length-1));

        Assert.assertArrayEquals(fileList, expected);
    }

    //rmdir
    @Test
    public void rmdirShouldNotChangingWorkingTreeIfDirDoseNotExist(){
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "dir"};
        String[] mockPathFileNames = new String[]{"root", "dir", "file"};
        String[] mockPathDirNames = new String[]{"root", "dir", "dir1"};
        String[] mockUnexistingDir = new String[]{"root", "dir", "dir2"};

        try {
            fileSystem.dir(mockPathNames);
            fileSystem.dir(mockPathDirNames);
            fileSystem.file(mockPathFileNames,3);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }
        String[] root = new String[]{"root"};
        String[] expected = new String[]{"dir1", "file"};

        try {
            fileSystem.rmdir(mockUnexistingDir);
        } catch (DirectoryNotEmptyException e) {
            Assert.fail("directory not Empty");
        }
        String[] fileList = fileSystem.lsdir(root);

        Assert.assertArrayEquals(fileList, expected);
    }

    @Test
    public void rmdirShouldDeleteDirIfExist(){
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "dir"};
        String[] mockPathDirNames = new String[]{"root", "dir", "dir1"};

        try {
            fileSystem.dir(mockPathNames);
            fileSystem.dir(mockPathDirNames);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        }
        String[] expected = new String[]{"file"};

        try {
            fileSystem.rmdir(mockPathDirNames);
        } catch (DirectoryNotEmptyException e) {
            Assert.fail("directory not Empty");
        }

        String[] fileList = fileSystem.lsdir(
                Arrays.copyOf(mockPathDirNames, mockPathDirNames.length-1));

        Assert.assertArrayEquals(fileList, expected);
    }

    @Test
    public void rmdirShouldNotDeleteFile(){
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "dir"};
        String[] mockPathFileNames = new String[]{"root", "dir", "file"};
        String[] mockPathDirNames = new String[]{"root", "dir", "dir1"};

        try {
            fileSystem.dir(mockPathNames);
            fileSystem.dir(mockPathDirNames);
            fileSystem.file(mockPathFileNames,3);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }
        String[] expected = new String[]{"dir1", "file"};

        try {
            fileSystem.rmdir(mockPathDirNames);
        } catch (DirectoryNotEmptyException e) {
            Assert.fail("directory not Empty");
        }
        String[] fileList = fileSystem.lsdir(
                Arrays.copyOf(mockPathDirNames, mockPathDirNames.length-1));

        Assert.assertArrayEquals(fileList, expected);
    }

    @Test (expected = DirectoryNotEmptyException.class)
    public void rmdirFunctionShouldThrowIfDirectoryNotEmpty() throws DirectoryNotEmptyException {

    }
}
