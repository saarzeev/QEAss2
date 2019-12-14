package system;

import org.junit.Assert;
import org.junit.Test;

public class NodeTest {
    @Test
    public void rootPathShouldBeEmptyArray() {
        TreeStub tree = new TreeStub("tree");

        String[] path = new String[0];
        Assert.assertArrayEquals(path, tree.getPath());
    }

    @Test
    public void rootSubtreePathShouldBeRootOnly() {
        TreeStub tree = new TreeStub("tree");
        TreeStub subtree = tree.GetChildByName("subTree");
        TreeStub subSubtree = subtree.GetChildByName("subSubTree");

        String[] path = new String[2];
        path[0] = "subTree";
        path[1] = "subSubTree";
        Assert.assertArrayEquals(path, subSubtree.getPath());
    }
}