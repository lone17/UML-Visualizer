package parser;

import java.util.*;

/**
 * class Interface represents an interface
 *
 * @author Vu Minh Hieu
 */
public class Interface extends Component {

    private String[] baseInterfaces; // extended interfaces
    private LinkedList<Method> methods; // all contianed methods
    private LinkedList<Attribute> attributes; // all contianed attributes

    /**
     * Interface Constructor
     */
    public Interface(String input) {
        String[] declaration = Parser.getInterfaceDeclaration(input);

        String[] parts = declaration[0].split("\\s+");
        int len = parts.length;

        name = parts[len - 1];

        for (int i = len - 2; i >= 0; --i) {
            String cur = parts[i];
            if (modifiers.contains(cur))
                accessModifier = cur;
            else
                type = cur;
        }
        isAbstract = true;

        if (declaration[1] != null)
            baseInterfaces = declaration[1].split("\\s+");

        methods = new LinkedList<Method>();
        attributes = new LinkedList<Attribute>();
    }

    /**
     * Return all parent interfaces
     *
     * @return all extended interfaces
     */
    public String[] getBaseInterfaces() {
        return baseInterfaces;
    }

    /**
     * Add a method
     *
     * @param method a method to be added
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
    public void addAtribute(Attribute att) {
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
    public LinkedList<Attribute> getAttibutes() {
        return attributes;
    }
	/**
	 * @return a string representation of Interface for printing
	 */
	@Override
	public String toString() {

        String s = "";
        if (accessModifier != null) s += accessModifier + " ";

        s += type + " " + name + "\n";

        for (String item : baseInterfaces)
            s += name + " ---> " + item + "\n";


        for (Attribute item : attributes)
            s += item.toString() + "\n";

        for (Method item : methods)
            s += item.toString() + "\n";

        return s;
	}

    /**
     * Local testing
     */
    public static void main(String[] args) {
        String s = "public interface Interface extends UMLComponent, a1, a2";
        Interface test = new Interface(s);
        System.out.println(test);
    }
}
