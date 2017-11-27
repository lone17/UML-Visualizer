package parser;

import java.util.Enumeration;
import java.util.LinkedList;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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

        if (!path.endsWith(".zip")) {
            for (String filePath : allSourceFilePaths)
                sourceFiles.add(new SourceFile(filePath));
        }
        else try {
            ZipFile file = new ZipFile(path);
            Enumeration<? extends ZipEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".java"))
                    sourceFiles.add(new SourceFile(file.getInputStream(entry),
                                                          file.getName() + "\\" + entry.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * Returns a String representation
     *
     * @return a String representation of this Project
     */
    @Override
    public String toString() {
        String s = "# Project loaded from: " + self.getAbsolutePath() + "\n";
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
            uml = new Project("E:\\Code\\OOP\\Parser.zip");

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
