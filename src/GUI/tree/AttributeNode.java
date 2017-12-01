package GUI.tree;

import structure.Attribute;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Tree node represent an Attribute
 *
 * @author Nguyen Xuan Tung
 */
public class AttributeNode extends DefaultMutableTreeNode{

	/**
	 * AttributeNode Constructor
	 *
	 * @param attribute Attribute object to be shown on the node
	 */
	public AttributeNode(Attribute attribute){
		super(new ComponentDetail(attribute.presentation(), "Icon\\Attribute\\" + attribute.getAccessModifier() + ".png"));
	}
}
