package system;

import org.junit.Assert;
import org.junit.Test;

public class TreeTest {

    //constructor
    @Test
    public void TreeConstructorUpdatesName() {
        Tree tree = new Tree("tree");

        Assert.assertEquals("tree", tree.name);
    }

    // getChildByName
    @Test
    public void getChildByNameWhenNameDoesntExistResultsInItsCreation() {
        Tree tree = new Tree("tree");
        Tree subTree = tree.GetChildByName("subTree");
        Assert.assertNotNull(subTree);
    }

    @Test
    public void getChildByNameWhenNameExistsReturnsChild() {
        String subTreeName = "subTree";

        Tree tree = new Tree("tree");
        tree.GetChildByName(subTreeName);

        Tree subTree = tree.GetChildByName(subTreeName);
        Assert.assertNotNull(subTree);
    }
    @Test
    public void rootParentShouldBeNull(){
        Tree tree = new Tree("tree");
        Assert.assertNull(tree.parent);
    }

    @Test
    public void rootDepthShouldBeZero(){
        Tree tree = new Tree("tree");
        int expected = 0;
        int actual = tree.depth;

        Assert.assertEquals(expected, actual);
    }


    @Test
    public void depthOfTreeShouldGrowByOneAfterAddingChild(){
        Tree tree = new Tree("tree");
        Tree subTree = tree.GetChildByName("subTree");

        int expected = tree.depth + 1;
        int actual = subTree.depth;

        Assert.assertEquals(expected, actual);
    }
}