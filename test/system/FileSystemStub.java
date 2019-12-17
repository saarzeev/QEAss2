package system;

public class FileSystemStub extends FileSystem {
    private TreeStub fileSystemTreeStub;
    /**
     * Ctor - Initialise filesystem with empty root directory and \c m KB of space
     *
     * @param m Amount, in KB, of disk space to allocate
     */
    public FileSystemStub(int m) {
        super(m);
         fileSystemTreeStub = new TreeStub("root");
    }

    @Override
    public LeafStub FileExists(String[] name) {
        if(name[0] == "root" && name[1] == "file exists"){
            return null;
        }
        return new LeafStub("leafStub", 2);
    }

    @Override
    public void dir(String[] name) throws BadFileNameException {

        TreeStub workingTree = fileSystemTreeStub;

        if (name[0] != "root" || (FileExists(name) != null)) {
            throw new BadFileNameException();
        }

        if (DirExists(name) != null) {
            return;
        }

        //loop all the way, creating as we go down if necessary
        for (int i = 0; i < name.length; i++) {
            workingTree = workingTree.GetChildByName(name[i]);
        }
    }
}
