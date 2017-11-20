package GUI.tree;

import parser.Attribute;
import parser.Class;
import parser.Method;

import javax.swing.tree.DefaultMutableTreeNode;

public class ClassNode extends DefaultMutableTreeNode {

    public ClassNode(Class aClass) {
        super(aClass.getName());

        for (Attribute atts : aClass.getAttributes()) {
            this.add(new AttributeNode(atts));
        }

        for (Method method : aClass.getMethods()) {
            this.add(new MethodNode(method));
        }
    }


}
