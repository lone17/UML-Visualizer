package GUI.tree;

import parser.Method;

import javax.swing.tree.DefaultMutableTreeNode;

public class MethodNode extends DefaultMutableTreeNode{
    public MethodNode(Method method) {
        super(new ComponentDetail(method.presentation(), "Icon\\Method\\"
                                                    + method.getAccessModifier() + ".png"));
    }

}
