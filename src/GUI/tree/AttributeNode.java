package GUI.tree;

import structure.Attribute;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Represent class's attribute in the tree
 *
 * @author Nguyen Xuan Tung
 */
public class AttributeNode extends DefaultMutableTreeNode {
    /**
     * Constructor use an Attribute object from Parser to create a tree node
     *
     * @param attribute Attribute object
     */
    public AttributeNode(Attribute attribute) {
        super(new ComponentDetail(attribute.presentation(), "icon\\Attribute\\" + attribute.getAccessModifier() + ".png"));
    }
}
