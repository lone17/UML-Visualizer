package GUI.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.net.URL;

/**
 * Defines the requirements for an object that displays a tree node
 *
 * @author Nguyen Xuan Tung
 * @author Vu Minh Hieu
 */
public class ComponentTreeCellRenderer implements TreeCellRenderer {

    private JLabel label;

    /**
     * Constructor
     */
    ComponentTreeCellRenderer() {
        label = new JLabel();
    }

    /**
     * Set up
     */
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
