package system;

import org.junit.Assert;
import org.junit.Test;

public class LeafTest {

    //constructor
    @Test
    public void leafConstructorShouldNotThrow(){
        FileSystem.fileStorage = new SpaceStub(4);
        try {
            new Leaf("leaf", 3);
        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }
        return;
    }

    @Test (expected = OutOfSpaceException.class)
    public void leafConstructorShouldThrow() throws OutOfSpaceException {
        FileSystem.fileStorage = new SpaceStub(4);
        new Leaf("leaf", 5);
    }

    @Test
    public void leafConstructorShouldCreateName(){
        FileSystem.fileStorage = new SpaceStub(4);
        try {
           Leaf leaf = new Leaf("leaf", 3);
           Assert.assertEquals("leaf", leaf.name);

        } catch (OutOfSpaceException e) {
            Assert.fail("out of space,allocated 4 entered 3");
        }
    }

}
