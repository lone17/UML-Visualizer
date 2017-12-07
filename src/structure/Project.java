package structure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Class Project represent a java project
 *
 * @author Vu Minh Hieu
 */
public class Project extends Directory {

    private final ArrayList<SourceFile> sourceFiles; // all source files in the project

    /**
     * Project Constructor
     *
     * @param path the path of a project
     */
    public Project(String path) {
        super(path);
        sourceFiles = new ArrayList<>();

        if (!path.endsWith(".zip")) {
            for (String filePath : allSourceFilePaths)
                sourceFiles.add(new SourceFile(filePath));
        } else try {
            ZipFile file = new ZipFile(path);
            Enumeration<? extends ZipEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".java"))
                    sourceFiles.add(new SourceFile(file.getInputStream(entry), file.getName() + "\\" + entry.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(sourceFiles, new Comparator<SourceFile>() {
            @Override
            public int compare(SourceFile f1, SourceFile f2) {
                return f1.getName().compareTo(f2.getName());
            }
        });
    }

    /**
     * Local testing
     */
    public static void main(String[] args) {
        Project uml;
        if (args.length != 0) uml = new Project(args[0]);
        else uml = new Project("E:\\Code\\OOP\\UML-Visualizer\\src\\structure");

        //        try {
        //            FileWriter writer = new FileWriter("C:\\Users\\Vu Minh Hieu\\Desktop\\file1.txt");
        //            BufferedWriter out = new BufferedWriter(writer);
        //
        //            out.append(uml.toString());
        //
        //            out.close();
        //        } catch(IOException e) {
        //            System.out.println(e);
        //        }

        System.out.println(uml);
    }

    /**
     * Return all source files
     *
     * @return a LinkedList contains all source files
     */
    public ArrayList<SourceFile> getSourceFiles() {
        return sourceFiles;
    }

    /**
     * Get the number of source files in the project
     *
     * @return the number of source files
     */
    public int getSourceFileCount() {
        return sourceFiles.size();
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
            for (Extendable item : file.getContainedExtendables()) {
                s += "-----------------------------------------------\n";
                s += item.toString() + "\n";
            }
        }

        return s;
    }
}
