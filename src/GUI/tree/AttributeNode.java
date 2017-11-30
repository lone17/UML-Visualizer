package GUI.tree;

import structure.Attribute;

import javax.swing.tree.DefaultMutableTreeNode;

public class AttributeNode extends DefaultMutableTreeNode{
    public AttributeNode(Attribute attribute) {
        super(new ComponentDetail(attribute.presentation(), "Icon\\Attribute\\"
                                                       + attribute.getAccessModifier() + ".png"));
    }
}
