package system;

import system.FileSystem;
import system.OutOfSpaceException;

class LeafStub extends system.Leaf {

    /**
     * Size (in KB) of system.Leaf
     */
    public int size;
    /**
     * Array of blocks containing system.Leaf data
     */
    public int[] allocations;

    /**
     * Ctor - create leaf.
     *
     * @param name Name of the leaf.
     * @param size Size, in KB, of the leaf.
     * @throws OutOfSpaceException Allocating space failed.
     */
    public LeafStub(String name, int size) {

        this.name = name;

        //allocateSpace(size);

    }

    private void allocateSpace(int size) throws OutOfSpaceException {

        FileSystem.fileStorage.Alloc(size, this);

    }
}