package system;

import java.util.HashMap;

class TreeStub extends Tree {

    /**
     * The children of the current node
     */
    public HashMap<String, Node> children = new HashMap<String, Node>();


    /**
     * Ctor - create tree.
     *
     * @param name The name of the root element of this tree.
     */
    public TreeStub(String name) {

        this.name = name;

    }

    /**
     * Get a child from a \c system.Node, or create it if nonexistant.
     *
     * @param name Name of child to search for.
     * @return system.Tree found (or created).
     */
    @Override
    public TreeStub GetChildByName(String name) {

        if (this.children.containsKey(name)) {

            return (TreeStub) this.children.get(name);

        }

        //not found - create

        TreeStub newTree = new TreeStub(name);
        newTree.parent = this;
        newTree.depth = newTree.parent.depth + 1;

        this.children.put(name, newTree);

        return newTree;

    }
}