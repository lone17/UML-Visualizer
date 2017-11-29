package GUI.tree;

import parser.Attribute;
import parser.Interface;
import parser.Method;

import javax.swing.tree.DefaultMutableTreeNode;

public class InterfaceNode extends DefaultMutableTreeNode {
	private DefaultMutableTreeNode attributes, methods;

	public InterfaceNode(Interface aInterface) {
		super(new ComponentDetail(aInterface.getName(), "Icon\\interface.png"));

		attributes = new DefaultMutableTreeNode("Attributes:");
		methods = new DefaultMutableTreeNode("Methods:");

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
