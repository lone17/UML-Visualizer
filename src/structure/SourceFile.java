package structure;

import java.io.*;
import java.util.LinkedList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

/**
 * class SourceFile represents a source file
 *
 * @author Vu Minh Hieu
 */
public class SourceFile {

    private LinkedList<Extendable> containedExtendables; // the class contained in the source file

    // name, address and the simplified content of the file
    private String name, path;

    /**
     * SourceFile Constructor
     *
     * @param filePath the path to a source file
     */
    public SourceFile(String filePath) {
        try {
            File self;
            self = new File(filePath);
            name = self.getName();
            path = self.getAbsolutePath();

            CompilationUnit cu = JavaParser.parse(self);

            containedExtendables = new LinkedList<>();
            (new Visitor.ExtendableVisitor()).visit(cu, containedExtendables);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SourceFile(InputStream stream, String path) {
        this.path = path;
        name = path.replace("/", "\"").substring(path.lastIndexOf("\\") + 1, path.length());

        CompilationUnit cu = JavaParser.parse(stream);

        containedExtendables = new LinkedList<>();
        (new Visitor.ExtendableVisitor()).visit(cu, containedExtendables);
    }

    /**
     * Return the name of the source file
     *
     * @return the name of the source file
     */
    public String getName() {
        return name;
    }

    /**
     * return the address of the source file
     *
     * @return the absolute path of the source file
     */
    public String getPath() {
        return path;
    }

    /**
     * return the contained class
     *
     * @return a Class represents the contained class
     */
    public LinkedList<Extendable> getContainedExtendables() {
        return containedExtendables;
    }

    /**
     * Local testing
     */
    public static void main(String[] args) {
        SourceFile test = new SourceFile("E:\\Code\\OOP\\UML-Parser\\SourceFile.java");
        for (Extendable e : test.containedExtendables)
            System.out.println(e);
    }
}
