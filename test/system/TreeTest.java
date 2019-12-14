package system;

import org.junit.Assert;
import org.junit.Test;

public class TreeTest {


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
        int expecetd = 0;
        int actual = tree.depth;

        Assert.assertEquals(expecetd, actual);
    }

    @Test
    public void depthOfSubTreeShouldRootPlusOne(){
        Tree tree = new Tree("tree");
        Tree subTree = tree.GetChildByName("subTree");

        int expecetd = tree.depth + 1;
        int actual = subTree.depth;

        Assert.assertEquals(expecetd, actual);
    }
}