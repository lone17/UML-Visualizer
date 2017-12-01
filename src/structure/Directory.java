package structure;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * class Directory represents a directory
 *
 * @author Vu Minh Hieu
 */
public class Directory {

    protected File self; // the directory itself
    protected final LinkedList<Directory> subDirectories; // all subdirectories
    protected final LinkedList<String> allSourceFilePaths; // all source files path

    /**
     * Constructor
     *
     * @param path the directory's address
     */
    public Directory(String path) {
        self = new File(path);
        if (!self.exists()) throw new RuntimeException(": Directory not found");
        subDirectories = new LinkedList<>();
        setSubDirectories();
        allSourceFilePaths = new LinkedList<String>();
        setAllSourceFilePaths();
    }

    /**
     * Get all subfolder and file
     */
    protected void setSubDirectories() {
        if (self.isFile()) return;

        for (File file : self.listFiles()) {
            subDirectories.add(new Directory(file.getPath()));
        }
    }

    /**
     * Get all source files
     */
    protected void setAllSourceFilePaths() {
        if (self.isFile()) {
            if (self.getName().endsWith(".java"))
                allSourceFilePaths.add(self.getAbsolutePath());
            return;
        }

        for (Directory dir : subDirectories)
            allSourceFilePaths.addAll(dir.allSourceFilePaths);
    }


    /**
     * Return the name of the Directory
     *
     * @return directory's name
     */
    public String getName() {
        return self.getName();
    }

    /**
     * Return all subdirectories and files
     *
     * @return a Linked List contains children of this directory
     */
    public LinkedList<Directory> getSubDirectories() {
        return subDirectories;
    }

    /**
     * Return all source files path
     *
     * @return a Linked List contains all source files path
     */
    public LinkedList<String> getAllSourceFilePaths() {
        return allSourceFilePaths;
    }

    /**
     * Print all files, subfolders and their files/subfolders
     */
    public void printContent() {
        printContent(0);
    }

    /**
     * Helper method for content printing
     *
     * @param level depth-wise level of the directory
     */
    protected void printContent(int level) {
        for (int i = 0; i < level; ++i)
            System.out.print("  ");

        if (self.isDirectory() && level != 0) System.out.print("> ");
        else if (self.isFile()) System.out.print("- ");

        System.out.println(self.getName());

        if (self.isDirectory()) {
            for (Directory dir : subDirectories)
                dir.printContent(level + 1);
        }
    }

    /*
     * Local testing
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Directory dir = null;

        if (args.length != 0) dir = new Directory(args[0]);
        else
            dir = new Directory("E:\\Code\\OOP\\UML-Visualizer\\Attribute.java");

        for (String s : dir.getAllSourceFilePaths())
            System.out.println(s);
    }
}
