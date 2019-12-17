package system;

import org.junit.Assert;
import org.junit.Test;
import sun.reflect.FieldAccessor;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    @Test
    public void dirFunctionShouldUpdateWorkingTreeIfDirDoseNotExist() {
        FileSystem fileSystem = new FileSystem(1);
        String[] mockPathNames = new String[]{"root", "file exists"};
        Field fileSystemTree = null;
        try {
            fileSystemTree = FileSystem.class.getDeclaredField("fileSystemTree");
            fileSystemTree.setAccessible(true);
            Tree mockTree = new Tree("root");
            fileSystemTree.set(fileSystem,mockTree);

            Assert.assertEquals(0, mockTree.depth);

            fileSystem.dir(mockPathNames);
            Assert.assertEquals(1, mockTree.depth);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (BadFileNameException e) {
            Assert.fail("dir function throws `BadFileNameException`");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void dirFunctionShouldNotUpdateWorkingTreeIfDirExist() {
        FileSystem fileSystem = new FileSystem(1);
        String[] mockPathNames = new String[]{"root", "file exists"};
        Field fileSystemTree = null;
        Assert.fail("dada");
//        try {
//            fileSystemTree = FileSystem.class.getDeclaredField("fileSystemTree");
//            fileSystemTree.setAccessible(true);
//            Tree mockTree = new Tree("root");
//            fileSystemTree.set(fileSystem,mockTree);
//
//            Assert.assertEquals(0, mockTree.depth);
//
//            fileSystem.dir(mockPathNames);
//            Assert.assertEquals(1, ((Tree)mockTree.children.get("root")).children.size());
//
//            fileSystem.dir(mockPathNames);
//            Assert.assertEquals(1, ((Tree)mockTree.children.get("root")).children.size());
//
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (BadFileNameException e) {
//            Assert.fail("dir function throws `BadFileNameException`");
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (Exception e){
//            Assert.fail(e.getMessage());
//        }
    }

}
