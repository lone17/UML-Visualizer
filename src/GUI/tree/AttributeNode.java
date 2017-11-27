package GUI.tree;

import parser.Attribute;

import javax.swing.tree.DefaultMutableTreeNode;

public class AttributeNode extends DefaultMutableTreeNode{
    public AttributeNode(Attribute attribute) {
        super(new ComponentDetail(attribute.toString(), "Icon\\Attribute\\"
                                                       + attribute.getAccessModifier() + ".png"));
    }
}
