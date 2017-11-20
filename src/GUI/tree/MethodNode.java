package GUI.tree;

import parser.Method;

import javax.swing.tree.DefaultMutableTreeNode;

public class MethodNode extends DefaultMutableTreeNode{
    public MethodNode(Method method) {
        super(method.toString());
    }

}
