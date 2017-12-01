package GUI.tree;

import structure.Attribute;
import structure.Class;
import structure.Method;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Tree node represent a Class
 *
 * @author Nguyen Xuan Tung
 */
public class ClassNode extends DefaultMutableTreeNode{

	/**
	 * ClassNode Constructor
	 *
	 * @param aClass Class object to be shown in the node
	 */
	public ClassNode(Class aClass){
		super(new ComponentDetail(aClass.getName(), "Icon\\" + (aClass.isAbstract() ? "abstractClass.png" : "class.png")));

		DefaultMutableTreeNode attributes = new DefaultMutableTreeNode("Attributes:");
		DefaultMutableTreeNode methods = new DefaultMutableTreeNode("Methods:");

		for (Attribute attribute : aClass.getAttributes()) {
			attributes.add(new AttributeNode(attribute));
		}

		for (Method method : aClass.getMethods()) {
			methods.add(new MethodNode(method));
		}

		this.add(attributes);
		this.add(methods);
	}


}
