package structure;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import java.util.*;

/**
 * Class Component represents a component
 *
 * @author Vu Minh Hieu
 */
public class Class extends Extendable {

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

        if (declaration.isPublic())
            accessModifier = "public";
        else if (declaration.isProtected())
            accessModifier = "protected";
        else if (declaration.isPrivate())
            accessModifier = "private";

        for (ClassOrInterfaceType item : declaration.getExtendedTypes())
            baseClass = item.getName().toString();

        for (ClassOrInterfaceType item : declaration.getImplementedTypes())
           baseInterfaces.add(item.getName().toString());
    }

    /**
     * Add an attribute
     *
     * @param att the attribute to be added
     */
    @Override
    public void addAttribute(Attribute att) {
        if (att == null) return;
        attributes.add(att);
        associations.add(att.getType().replaceAll("[\\[<].*", ""));
    }

    /**
     * Add a list of attributes
     *
     * @param atts the list of attributes to be added
     */
    @Override
    public void addAllAttributes(LinkedList<Attribute> atts) {
        if (atts == null) return;
        for (Attribute att : atts)
            if (att != null) {
                attributes.add(att);
                associations.add(att.getType().replaceAll("[\\[<].*", ""));
//                associations.add(att.getType().replaceAll("(.*<)|(>*)", ""));
            }
    }

    /**
     * Return all associations
     *
     * @return a HashSet contains all association components
     */
    public HashSet<String> getAssociations(){
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
        if (isStatic)   s += "static ";
        if (isFinal)    s += "final ";

        s += type + " " + name + "\n";

        if (baseClass != null) s += name + " ---|> " + baseClass + "\n";

        if (baseInterfaces != null)
            for (String item : baseInterfaces)
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

    /**
     * Local testing
     */
    public static void main(String[] args) {
    }
}
