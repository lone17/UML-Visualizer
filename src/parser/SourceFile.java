package parser;

import java.io.*;

/**
 * class SourceFile represents a source file
 *
 * @author Vu Minh Hieu
 */
public class SourceFile {

    private Class containedClass; // the class contianed in the source file

    // name, address and the simplified content of the file
    private String name, path, text = "";

    /**
     * SourceFile Constructor
     *
     * @param filePath the path to a source file
     */
    public SourceFile(String filePath) {
        File self;
        try {
            self = new File(filePath);
            name = self.getName();
            path = self.getAbsolutePath();
            String line;
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            while ((line = br.readLine()) != null) {
                text += Parser.removeQuote(line) + "\n";
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        text = Parser.preprocess(text);

        // text = text.replaceAll("\\{(?:.|[\\r|\\n])*?\\}", "");
        // text = text.replaceAll("[\\n\\r;]", " ").trim();

        String[] lines = text.trim().split("\\s*;\\s*");
        // for (String s : lines) {
        //     System.out.println(s);
        // }
        containedClass = new Class(lines[0]);
        for (int i = 1, len = lines.length; i < len; ++i) {
            // System.out.println(lines[i]);
            if (lines[i].contains("(") && !lines[i].contains("="))
                containedClass.addMethod(new Method(lines[i]));
            else
                containedClass.addAllAttributes(Attribute.generateInstances(lines[i]));
        }
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
    public Class getContainedClass() {
        return containedClass;
    }

    /**
     * Local testing
     */
    public static void main(String[] args) {
        SourceFile test = new SourceFile("D:\\Downloads\\K53CC\\r50\\src\\com\\tavanduc\\uml\\gui\\Relationship.java");
        System.out.println(test.containedClass);
        // System.out.println(test.text);
    }
}
