package GUI.tree;

import structure.Method;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Represent class's method in the tree
 *
 * @author Nguyen Xuan Tung
 */
public class MethodNode extends DefaultMutableTreeNode {

    /**
     * Constructor use a Method object from Parser to create a tree node
     *
     * @param method Method object
     */
    public MethodNode(Method method) {
        super(new ComponentDetail(method.presentation(), "Icon\\Method\\"
                + method.getAccessModifier() + ".png"));
    }

}
