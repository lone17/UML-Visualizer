package GUI.tree;

import structure.Attribute;
import structure.Interface;
import structure.Method;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Represent project's interface in the tree
 *
 * @author Nguyen Xuan Tung
 */
public class InterfaceNode extends DefaultMutableTreeNode {

    /**
     * Constructor
     *
     * @param aInterface Class object
     */
    public InterfaceNode(Interface aInterface) {
        super(new ComponentDetail(aInterface.getName(), "Icon\\interface.png"));

        DefaultMutableTreeNode attributes = new DefaultMutableTreeNode("Attributes:");
        DefaultMutableTreeNode methods = new DefaultMutableTreeNode("Methods:");

        for (Attribute attribute : aInterface.getAttributes()) {
            attributes.add(new AttributeNode(attribute));
        }

        for (Method method : aInterface.getMethods()) {
            methods.add(new MethodNode(method));
        }

        this.add(attributes);
        this.add(methods);
    }
}
