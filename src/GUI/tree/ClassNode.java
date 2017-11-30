package GUI.tree;

import structure.Attribute;
import structure.Class;
import structure.Method;

import javax.swing.tree.DefaultMutableTreeNode;

public class ClassNode extends DefaultMutableTreeNode {
    private DefaultMutableTreeNode attributes, methods;

    public ClassNode(Class aClass) {
        super(new ComponentDetail(aClass.getName(), "Icon\\"
                                                    + (aClass.isAbstract() ? "abstractClass.png" : "class.png")));

        attributes = new DefaultMutableTreeNode("Attributes:");
        methods = new DefaultMutableTreeNode("Methods:");

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
