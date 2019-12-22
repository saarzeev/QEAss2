package system;

import system.FileSystem;
import system.OutOfSpaceException;

import java.util.Arrays;
import java.util.Objects;

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
        this.allocations = new int[size];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeafStub leafStub = (LeafStub) o;
        return size == leafStub.size &&
                Arrays.equals(allocations, leafStub.allocations);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(allocations);
        return result;
    }
}