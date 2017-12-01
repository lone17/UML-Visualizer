package structure;

import java.util.LinkedList;

/**
 * Class Extendable represents a Class or an Interface
 *
 * @author Vu Minh Hieu
 */
public abstract class Extendable extends Component {

    protected String baseClass; // parent class
    protected LinkedList<String> baseInterfaces; // implemented interfaces
    protected LinkedList<Method> methods; // contained methods
    protected LinkedList<Attribute> attributes; // contained attributes

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
    public LinkedList<String> getBaseInterfaces() {
        return baseInterfaces;
    }

    /**
     * Check whether this Class has any attribute
     *
     * @return true if there is any attribute contained in this Class
     * false otherwise
     */
    public boolean hasAttribute() {
        return attributes.size() != 0;
    }

    /**
     * Check whether this Class has any method
     *
     * @return true if there is any method contained in this Class
     * false otherwise
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
        if (method == null) return;
        methods.add(method);
    }

    /**
     * Add a list of methods
     *
     * @param methods the list of methods to be added
     */
    public void addAllMethods(LinkedList<Method> methods) {
        if (methods == null) return;
        for (Method method : methods) {
            if (method != null) methods.add(method);
        }
    }

    /**
     * Get all contained methods
     *
     * @return a LinkedList contains all methods
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
        if (att == null) return;
        attributes.add(att);
    }

    /**
     * Add a list of attributes
     *
     * @param atts the list of attributes to be added
     */
    public void addAllAttributes(LinkedList<Attribute> atts) {
        if (atts == null) return;
        for (Attribute att : atts)
            if (att != null) {
                attributes.add(att);
            }
    }

    /**
     * Return all contained attributes
     *
     * @return a Linkedlist contains all attributes
     */
    public LinkedList<Attribute> getAttributes() {
        return attributes;
    }

}
