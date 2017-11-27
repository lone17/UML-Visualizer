package GUI.tree;

import parser.Attribute;
import parser.Class;
import parser.Method;

import javax.swing.tree.DefaultMutableTreeNode;

public class InterfaceNode extends TreeNode {
	private DefaultMutableTreeNode attributes, methods;

	public InterfaceNode(Class aInterface) {
		super(aInterface.getName(), "Icon\\interface.png");

				attributes = new DefaultMutableTreeNode("Attributes:");
		methods = new DefaultMutableTreeNode("Methods:");

		for (Attribute atts : aInterface.getAttributes()) {
			attributes.add(new AttributeNode(atts));
		}

		for (Method method : aInterface.getMethods()) {
			methods.add(new MethodNode(method));
		}

		this.add(attributes);
		this.add(methods);
	}


}