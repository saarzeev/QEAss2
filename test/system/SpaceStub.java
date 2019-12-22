package system;

public class SpaceStub extends Space {

    public int size;
    /**
     * Ctor - create \c size blank filesystem blocks.
     *
     * @param size
     */
    public SpaceStub(int size) {
        super(size);
        this.size = size;
    }

    @Override
    public void Alloc(int size, Leaf file) throws OutOfSpaceException {
        if(size > this.size){
            throw new OutOfSpaceException();
        }
        this.size -= size;
    }


}
