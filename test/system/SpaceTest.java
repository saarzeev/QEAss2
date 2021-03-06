package system;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class SpaceTest {
    @Before
    public void initFileSystem(){
        FileSystem fs = new FileSystem(10);
    }

    @Test
    public void allocationOfSpaceSmallerThanTotalSpaceShouldBeSuccessful() throws OutOfSpaceException {
        Space space = new Space(10);
        space.Alloc(5, new LeafStub("stub", 5));
        int expected = 5;
        int actual = space.countFreeSpace();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = OutOfSpaceException.class)
    public void allocationOfSpaceBiggerThanTotalSpaceShouldThrowAnException() throws OutOfSpaceException{
        Space space = new Space(10);
        //FileSystem.fileStorage = space;
        space.Alloc(11, new LeafStub("stub", 5));
    }
//
    @Test
    public void countFreeSpaceShouldReturn10(){
        Space space = new Space(10);
        int expected = 10;
        int actual = space.countFreeSpace();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void fileAllocationShouldLowerFreeSpace() throws OutOfSpaceException{
        int spaceSize = 10;
        int fileSize = 5;

        Space space = new Space(spaceSize);
        space.Alloc(fileSize, new LeafStub("stub", fileSize));

        int expected = spaceSize - fileSize;
        int actual = space.countFreeSpace();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void deallocShouldFreeSpace() throws OutOfSpaceException {
        int spaceSize = 10;
        int fileSize = 5;

        Space space = new Space(spaceSize);
        LeafStub stub = new LeafStub("stub", fileSize);
        stub.parent = new TreeStub("treeStub");
        space.Alloc(fileSize, stub);
        space.Alloc(fileSize, new LeafStub("stub2", fileSize));
        space.Dealloc(stub);

        int expected = spaceSize - fileSize;
        int actual = space.countFreeSpace();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void allocationsShouldBeSaved() throws OutOfSpaceException {
        int spaceSize = 10;
        int fileSize = 5;

        Space space = new Space(spaceSize);
        LeafStub[] stubs = new LeafStub[]{new LeafStub("stub", fileSize)};
        space.Alloc(fileSize, stubs[0]);
        Leaf[] actual = space.getAlloc();

        for( int i = 0 ; i < 5 ; i++){
            Assert.assertEquals(true, actual[i].equals(stubs[0]));
        }

    }

    @Test
    public void deallocationsShouldRemoveLeaf() throws OutOfSpaceException {
        int spaceSize = 10;
        int fileSize = 5;

        Space space = new Space(spaceSize);
        LeafStub[] stubs = new LeafStub[]{new LeafStub("stub", fileSize)};
        stubs[0].parent = new TreeStub("treeStub");
        space.Alloc(fileSize, stubs[0]);
        space.Dealloc(stubs[0]);
        Leaf[] actual = space.getAlloc();
        for( int i = 0 ; i < 10 ; i++){
            Assert.assertNull(actual[i]);
        }
    }
}