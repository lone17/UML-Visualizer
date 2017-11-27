package GUI.tree;

import javax.swing.tree.DefaultMutableTreeNode;

public class TreeNode extends DefaultMutableTreeNode {
	private String name, iconPath = "";

	public TreeNode(String name, String iconPath) {
		super(name);
		this.name = name;
		this.iconPath = iconPath;
	}

	public String getName(){
		return name;
	}

	public String getIconPath(){
		return iconPath;
	}
}
