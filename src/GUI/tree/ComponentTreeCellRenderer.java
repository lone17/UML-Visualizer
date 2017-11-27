package GUI.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.net.URL;

public class ComponentTreeCellRenderer implements TreeCellRenderer{

    private JLabel label;

    ComponentTreeCellRenderer() {
        label = new JLabel();
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                  boolean selected, boolean expanded,
                                                  boolean leaf, int row, boolean hasFocus) {
        Object o = ((DefaultMutableTreeNode) value).getUserObject();
        if (o instanceof ComponentDetail) {
            ComponentDetail methodDetail = (ComponentDetail) o;
            URL imageURL = getClass().getResource(methodDetail.getIcon());
            if (imageURL != null) {
                label.setIcon(new ImageIcon(imageURL));
            }
            label.setText(methodDetail.getName());
        } else {
            label.setIcon(null);
            label.setText("" + value);
        }

        return label;
    }
}
