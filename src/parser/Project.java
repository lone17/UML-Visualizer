package parser;

import java.util.LinkedList;
import java.io.*;

/**
 * class Project represent a java project
 *
 * @author Vu Minh Hieu
 */
public class Project extends Directory {

    private final LinkedList<SourceFile> sourceFiles; // all source files in the project

    /**
     * Project Constructor
     *
     * @param path the path of a project
     */
    public Project(String path) {
        super(path);
        sourceFiles = new LinkedList<SourceFile>();

        for (String filePath : allSourceFilePaths)
            sourceFiles.add(new SourceFile(filePath));
    }

    /**
     * Return all source files
     *
     * @return a LinkedList contains all source files
     */
    public LinkedList<SourceFile> getSourceFiles() {
        return sourceFiles;
    }

    /**
     * Return the name of the Project
     *
     * @return project's name
     */
    public String getName(){
        return self.getName();
    }

    /**
     * Returns a String representation
     *
     * @return a String representation of this Project
     */
    @Override
    public String toString() {
        String s = "# Project loaded from: " + self.getAbsolutePath();
        for (SourceFile file : sourceFiles) {
            s += "-----------------------------------------------\n";
            s += file.getContainedClass().toString() + "\n";
        }

        return s;
    }

    /**
     * Local testing
     */
    public static void main(String[] args) {
        Project uml;
        if (args.length != 0)
            uml = new Project(args[0]);
        else
            uml = new Project("E:\\Code\\OOP\\UML-Visualizer");

        // try {
        //     FileWriter writer = new FileWriter("file1.txt");
        //     BufferedWriter out = new BufferedWriter(writer);
        //
        //     for (SourceFile file : uml.sourceFiles) {
        //         out.append("*************************************************\n");
        //         out.append(file.getContainedClass() + "\n");
        //     }
        //
        //     out.close();
        // } catch(IOException e) {
        //     System.out.println(e);
        // }

        System.out.println(uml);
    }
}
