package parser;

import java.util.*;

/**
 * Class Component represents a component
 *
 * @author Vu Minh Hieu
 */
public class Class extends Component {
    
    private String baseClass; // parent class
    private String[] baseInterfaces; // implemented interfaces
    private LinkedList<Method> methods; // contianed methods
    private LinkedList<Attribute> attributes; // contained attributes

    /**
     * Class Constructor
     *
     * @param input a String contains class declaration
     */
    public Class(String input) {
        String[] declaration = Parser.getClassDeclaration(input);

        String[] parts = declaration[0].split("\\s+");
        int len = parts.length;

        name = parts[len - 1];

        for (int i = len - 2; i >= 0; --i) {
            String cur = parts[i];
            if (cur.equals("static"))
                isStatic = true;
            else if (cur.equals("abstract"))
                isAbstract = true;
            else if (cur.equals("final"))
                isFinal = true;
            else if (modifiers.contains(cur))
                accessModifier = cur;
            else
                type = cur;
        }

        baseClass = declaration[1];
        if (declaration[2] != null)
            baseInterfaces = declaration[2].split("\\s+");

        methods = new LinkedList<Method>();
        attributes = new LinkedList<Attribute>();
    }

    /**
     * get parent class
     *
     * @return the caller's parent class
     */
    public String getBaseClass() {
        return baseClass;
    }

    /**
     * get implemented interfaces
     *
     * @return the caller's all parent interfaces
     */
    public String[] getBaseInterfaces() {
        return baseInterfaces;
    }

    /**
     * Check wether this Class has any attribute
     *
     * @return true if there is any attribute contained in this Class
     *         false otherwise
     */
    public boolean hasAttribute() {
        return attributes.size() != 0;
    }

    /**
     * Check wether this Class has any method
     *
     * @return true if there is any method contained in this Class
     *         false otherwise
     */
    public boolean hasMethod() {
        return methods.size() != 0;
    }

    /**
     * Add a method
     *
     * @param method the method to be added
     */
    public void addMethod(Method method) {
        methods.add(method);
    }

    /**
     * Add a list of methods
     *
     * @param methods the list of methods to be added
     */
    public void addAllMethods(LinkedList<Method> methods) {
        methods.addAll(methods);
    }

    /**
     * Get all contained methods
     *
     * @return all contained methods
     */
    public LinkedList<Method> getMethods() {
        return methods;
    }

    /**
     * Add an attribute
     *
     * @param att the attribute to be added
     */
    public void addAttribute(Attribute att) {
        attributes.add(att);
    }

    /**
     * Add a list of attributes
     *
     * @param atts the list of attributes to be added
     */
    public void addAllAttributes(LinkedList<Attribute> atts) {
        attributes.addAll(atts);
    }

    /**
     * Return all contianed attributes
     *
     * @return all contained attributes
     */
    public LinkedList<Attribute> getAttributes() {
        return attributes;
    }
	/**
	 * @return a string representation of Class for printing
	 */
	@Override
	public String toString() {

        String s = "";
        if (accessModifier != null) s += accessModifier + " ";
        if (isAbstract) s += "abstract ";
        if (isStatic)   s += "static ";
        if (isFinal)    s += "final ";

        s += type + " " + name + "\n";

        if (baseClass != null) s += name + " ---|> " + baseClass + "\n";

        if (baseInterfaces != null)
            for (String item : baseInterfaces)
                s += name + " ---|> " + item + "\n";

        for (Attribute item : attributes)
            s += name + " <>--- " + item.getType() + "\n";

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
        String s = "public class Class extends UMLComponent {";
        Class test = new Class(s);
        System.out.println(test.getBaseClass());
    }
}
