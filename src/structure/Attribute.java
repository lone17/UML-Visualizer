package structure;

import java.util.*;

/**
 * class Attribute represents a class's attribute
 *
 * @author Vu Minh Hieu
 */
public class Attribute extends Component {

    /**
     * Generate a list of instances from a attribute input
     *
     * @param input a String contains attribute inputs
     * @return a list of attributes declared in the param
     */
    public static LinkedList<Attribute> generateInstances(String input) {
        String[] instances = Parser.getAttributeDeclaration(input);

        LinkedList<Attribute> attributes = new LinkedList<Attribute>();

        attributes.add(new Attribute(instances[0]));
        String properties = instances[0].trim().replaceAll(" [^ ]*$", "");
        for (int i = 1, len = instances.length; i < len; ++i)
            attributes.add(new Attribute(properties + " " + instances[i]));

        return attributes;
    }

    /**
     * Attribute Constructor
     */
    private Attribute(String input) {
        String[] declaration = input.trim().split("\\s+");
        int len = declaration.length;

        name = declaration[len - 1];
        type = declaration[len - 2];

        for (int i = len - 3; i >= 0; --i) {
            String cur = declaration[i];
            if (cur.equals("static"))
                isStatic = true;
            else if (cur.equals("abstract"))
                isAbstract = true;
            else if (cur.equals("final"))
                isFinal = true;
            else if (modifiers.contains(cur))
                accessModifier = cur;
        }
    }

    /**
     * @return true if this component is a attribute
     *         false otherwise
     */
    @Override
    public boolean isAttribute() {
        return true;
    }

    /**
     * @return a string for brief presentation on tree panel
     */
    public String presentation() {
        return name + ": " + type;
    }

    /**
     * @return a string representation of Method for printing
     */
    @Override
    public String toString() {

        String s = "";
        if (!accessModifier.equals("default")) s += accessModifier + " ";
        if (isAbstract) s += "abstract ";
        if (isStatic)   s += "static ";
        if (isFinal)    s += "final ";
        if (type != null) s += type + " ";

        return s  + name;
    }

    /**
     * Local testing
     */
    public static void main(String[] args) {
        List<Attribute> atts = Attribute.generateInstances("protected boolean isStatic = , isAbstract, isFinal = false");
        for (Attribute a : atts)
            System.out.println(a);
    }
}
