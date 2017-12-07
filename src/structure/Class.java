package structure;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Class Component represents a component
 *
 * @author Vu Minh Hieu
 */
public class Class extends Extendable {

    // list of primitive types
    private static final List<String> primitiveTypes = Arrays.asList("int", "boolean", "double", "char", "long", "float", "short", "byte");

    private HashSet<String> associations; // all association components

    public Class(ClassOrInterfaceDeclaration declaration) {
        methods = new LinkedList<>();
        attributes = new LinkedList<>();
        baseInterfaces = new LinkedList<>();
        associations = new HashSet<>();

        isStatic = declaration.isStatic();
        isAbstract = declaration.isAbstract();
        isFinal = declaration.isFinal();

        type = "class";
        name = declaration.getNameAsString();

        if (declaration.isPublic()) accessModifier = "public";
        else if (declaration.isProtected()) accessModifier = "protected";
        else if (declaration.isPrivate()) accessModifier = "private";

        for (ClassOrInterfaceType item : declaration.getExtendedTypes())
            baseClass = item.getName().toString();

        for (ClassOrInterfaceType item : declaration.getImplementedTypes())
            baseInterfaces.add(item.getName().toString());
    }

    /**
     * Local testing
     */
    public static void main(String[] args) {
    }

    /**
     * Add an attribute
     *
     * @param attribute the attribute to be added
     */
    @Override
    public void addAttribute(Attribute attribute) {
        if (attribute == null) return;
        attributes.add(attribute);

        String type = attribute.getType().replaceAll("[\\[<].*", "");
        if (!primitiveTypes.contains(type)) associations.add(type);

        for (String item : attribute.getType().replaceAll("(\\s+)|(.*<)|(>.*)", "").split(",")) {
            type = item.replaceAll("[\\[<].*", "");
            if (!primitiveTypes.contains(type)) associations.add(type);
        }
    }

    /**
     * Add a list of attributes
     *
     * @param attributes the list of attributes to be added
     */
    @Override
    public void addAllAttributes(LinkedList<Attribute> attributes) {
        if (attributes == null) return;
        for (Attribute attribute : attributes)
            if (attribute != null) {
                this.attributes.add(attribute);

                String type = attribute.getType().replaceAll("[\\[<].*", "");
                if (!primitiveTypes.contains(type)) associations.add(type);

                for (String item : attribute.getType().replaceAll("(\\s+)|(.*<)|(>.*)", "").split(",")) {
                    type = item.replaceAll("[\\[<].*", "");
                    if (!primitiveTypes.contains(type)) associations.add(type);
                }
            }
    }

    /**
     * Return all associations
     *
     * @return a HashSet contains all association components
     */
    public HashSet<String> getAssociations() {
        return associations;
    }

    /**
     * @return a string representation of Class for printing
     */
    @Override
    public String toString() {

        String s = "";
        if (!accessModifier.equals("default")) s += accessModifier + " ";
        if (isAbstract) s += "abstract ";
        if (isStatic) s += "static ";
        if (isFinal) s += "final ";

        s += type + " " + name + "\n";

        if (baseClass != null) s += name + " ---|> " + baseClass + "\n";

        if (baseInterfaces != null) for (String item : baseInterfaces)
            s += name + " ---|> " + item + "\n";

        for (String item : associations)
            s += name + " <>--- " + item + "\n";

        s += "\n" + "Attributes: " + "\n";
        for (Attribute item : attributes)
            s += item.toString() + "\n";

        s += "\n" + "Method: " + "\n";
        for (Method item : methods)
            s += item.toString() + "\n";

        return s;
    }
}
