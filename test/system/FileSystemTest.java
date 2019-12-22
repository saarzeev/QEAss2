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
    public void dirFunctionShouldNotThrowExceptionWhenReceivedValidParams(){
        FileSystem fileSystem = new FileSystem(1);

        String[] mockPathNames = new String[]{"root", "dir"};

        try {
            fileSystem.dir(mockPathNames);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        }
    }

    @Test
    public void dirFunctionShouldNotChangeWorkingTreeIfDirExist(){
        FileSystem fileSystem = new FileSystem(1);
        String[] root = new String[]{"root"};
        String[] mockPathNames = new String[]{"root", "dir"};

        try {
            fileSystem.dir(mockPathNames);
            String[] expected = fileSystem.lsdir(root);
            fileSystem.dir(mockPathNames);
            String[] actual = fileSystem.lsdir(root);

            Assert.assertArrayEquals(expected, actual);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        }
    }

    @Test
    public void dirFunctionShouldChangeWorkingTreeIfDirDoseNotExist(){
        FileSystem fileSystem = new FileSystem(1);
        String[] root = new String[]{"root"};

        String[] mockPathNames = new String[]{"root", "dir"};

        try {
            String[] noDir = fileSystem.lsdir(root);
            fileSystem.dir(mockPathNames);
            String[] actual = fileSystem.lsdir(root);

            String[] expected = new String[]{"dir"};
            fileSystem.dir(mockPathNames);

            Assert.assertNotEquals(noDir, actual);
            Assert.assertArrayEquals(expected, actual);
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

            return;
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }

    }

    @Test
    public void createsDirectoriesIfNotExist(){
        FileSystem fileSystem = new FileSystem(4);
        String[] root = new String[]{"root"};
        String[] mockPathNames = new String[]{"root", "dir", "file"};
        String[] expected = new String[]{"dir"};
        try {

            fileSystem.file(mockPathNames, 3);
            String[] actual = fileSystem.lsdir(root);
            Assert.assertArrayEquals(expected, actual);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }
    }

    @Test
    public void fileIfFileExistShouldUpdate(){
        FileSystem fileSystem = new FileSystem(5);
        String[] root = new String[]{"root"};
        String[] mockPathNames = new String[]{"root", "file"};
        String[] expected = new String[]{"file"};
        try {

            fileSystem.file(mockPathNames, 3);
            String[] actual = fileSystem.lsdir(root);

            Assert.assertArrayEquals(expected, actual);
            Assert.assertEquals(FileSystem.fileStorage.countFreeSpace(), 2);

            fileSystem.file(mockPathNames, 4);

            Assert.assertArrayEquals(expected, actual);
            Assert.assertEquals(FileSystem.fileStorage.countFreeSpace(), 1);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 5 entered 3/4");
        }
    }

    @Test
    public void fileIfNewFileUpdateOutOfSpaceShouldNotRemoveOld(){
        FileSystem fileSystem = new FileSystem(4);
        String[] root = new String[]{"root"};
        String[] mockPathNames = new String[]{"root", "file"};
        String[] expected = new String[]{"file"};
        try {

            fileSystem.file(mockPathNames, 3);

        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }
        try {
            fileSystem.file(mockPathNames, 4);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            String[] actual = fileSystem.lsdir(root);

            Assert.assertArrayEquals(expected, actual);
            Assert.assertEquals(FileSystem.fileStorage.countFreeSpace(), 2);
            return;
        }
    }

    @Test
    public void fileIfNewAndGotSpaceShouldChangeWorkingTree(){
        FileSystem fileSystem = new FileSystem(4);
        String[] root = new String[]{"root"};
        String[] mockPathNames = new String[]{"root", "file"};
        String[] expected = new String[]{"file"};
        try {
            fileSystem.file(mockPathNames, 3);

            String[] actual = fileSystem.lsdir(root);

            Assert.assertArrayEquals(expected, actual);
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

        Assert.assertNull(fileList);
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
            fileSystem.file(mockPathFileNames,3);
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
        String[] expectedDir = new String[]{"dir"};
        String[] expectedFiles = new String[]{"dir1", "file"};

        fileSystem.rmfile(mockUnexistingFile);
        String[] fileListRoot = fileSystem.lsdir(root);
        String[] fileListDir = fileSystem.lsdir(mockPathNames);

        Assert.assertArrayEquals(fileListRoot, expectedDir);
        Assert.assertArrayEquals(fileListDir, expectedFiles);
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
        String[] fileList = fileSystem.lsdir(mockPathNames);

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
        String[] fileList = fileSystem.lsdir(mockPathNames);

        Assert.assertArrayEquals(expected, fileList);
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
        String[] expected = new String[]{};

        try {
            fileSystem.rmdir(mockPathDirNames);
        } catch (DirectoryNotEmptyException e) {
            Assert.fail("directory not Empty");
        }

        String[] fileList = fileSystem.lsdir(mockPathNames);

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
            fileSystem.rmdir(mockPathFileNames);
        } catch (DirectoryNotEmptyException e) {
            Assert.fail("directory not Empty");
        }
        String[] fileList = fileSystem.lsdir(
                Arrays.copyOf(mockPathFileNames, mockPathFileNames.length-1));

        Assert.assertArrayEquals(fileList, expected);
    }

    @Test (expected = DirectoryNotEmptyException.class)
    public void rmdirFunctionShouldThrowIfDirectoryNotEmpty() throws DirectoryNotEmptyException {
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

        fileSystem.rmdir(mockPathNames);
    }

    //FileExists
    @Test
    public void fileExistsShouldReturnFileIfExist() {
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
            Assert.fail("out of space, allocated 4 entered 3");
        }

        Leaf actual = fileSystem.FileExists(mockPathFileNames);

        Assert.assertEquals("file", actual.name);
        Assert.assertEquals("dir", actual.parent.name);
    }

    @Test
    public void fileExistsShouldReturnLeafTypeIfFileExist() {
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "dir"};
        String[] mockPathFileNames = new String[]{"root", "dir", "file"};

        try {
            fileSystem.dir(mockPathNames);
            fileSystem.file(mockPathFileNames,3);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }

       Assert.assertEquals("system.Leaf" ,fileSystem.FileExists(mockPathFileNames).getClass().getName());
    }

    @Test
    public void fileExistsShouldReturnNullIfNotExist() {
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "dir"};
        String[] mockPathFileNames = new String[]{"root", "dir", "file"};
        String[] mockPathDirNames = new String[]{"root", "dir", "dir1"};

        try {
            fileSystem.dir(mockPathNames);
            fileSystem.dir(mockPathDirNames);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/", mockPathNames) +
                    "is valid param, should not throw");
        }

       Assert.assertNull(fileSystem.FileExists(mockPathFileNames));
    }

    @Test
    public void fileExistsShouldReturnNullIfReceivedDir() {
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "dir"};

        try {
            fileSystem.dir(mockPathNames);

        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        }
        Assert.assertNull(fileSystem.FileExists(mockPathNames));
    }

    //DirExists
    @Test
    public void dirExistsShouldReturnDirIfExist() {
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
            Assert.fail("out of space, allocated 4 entered 3");
        }

        Tree actual = fileSystem.DirExists(mockPathNames);

        Assert.assertEquals("dir", actual.name);
        Assert.assertEquals("root", actual.parent.name);
        Assert.assertEquals(2, actual.children.size());
    }

    @Test
    public void dirExistsShouldReturnTreeTypeIfDirExist() {
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "dir"};

        try {
            fileSystem.dir(mockPathNames);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        }

        Assert.assertEquals("system.Tree" ,fileSystem.DirExists(mockPathNames).getClass().getName());
    }

    @Test
    public void dirExistsShouldReturnNullIfNotExist() {
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "dir"};

        String[] mockPathDirNames = new String[]{"root", "dir", "dir1"};

        try {
            fileSystem.dir(mockPathNames);
        } catch (BadFileNameException e) {
            Assert.fail(String.join("/", mockPathNames) +
                    "is valid param, should not throw");
        }

        Assert.assertNull(fileSystem.DirExists(mockPathDirNames));
    }

    @Test
    public void dirExistsShouldReturnNullIfReceivedFile() {
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "file"};

        try {
            fileSystem.file(mockPathNames, 3);

        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space, allocated 4 entered 3");
        }
        Assert.assertNull(fileSystem.DirExists(mockPathNames));
    }

    //Disk
    @Test
    public void diskFunctionReturnsRigthSizeOfDiskMatrix() {
        FileSystem fileSystem = new FileSystem(4);
        String[][] disk = fileSystem.disk();
        Assert.assertEquals(4, disk.length);
    }

    @Test
    public void diskFunctionReturnsNullEateriesWhenAllEmpty() {
        FileSystem fileSystem = new FileSystem(4);
        String[][] disk = fileSystem.disk();
        for( int i = 0 ; i < disk.length ; i++){
            Assert.assertNull( disk[i]);
        }
    }

    @Test
    public void diskFunctionReturnsNullEateriesWhenWhereEmpty() {
        FileSystem fileSystem = new FileSystem(4);
        String[] mockPathNames = new String[]{"root", "file"};
        try {
            fileSystem.file(mockPathNames, 3);

            String[][] disk = fileSystem.disk();

            for( int i = 0 ; i < 3 ; i++){
                Assert.assertArrayEquals(mockPathNames, disk[i]);
            }

            Assert.assertNull(disk[3]);

        } catch (BadFileNameException e) {
            Assert.fail(String.join("/",mockPathNames) +
                    "is valid param, should not throw");
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space, allocated 4 entered 3");
        }
    }
}
