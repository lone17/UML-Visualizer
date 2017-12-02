package GUI.tree;

import GUI.App;
import structure.Project;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;

/**
 * The container of the tree
 *
 * @author Nguyen Xuan Tung
 * @author Vu Minh Hieu
 */
public class TreePanel extends JScrollPane {

    /**
     * TreePanel constructor
     */
    public TreePanel() {
        super();
        initContent(new DefaultMutableTreeNode(new ComponentDetail("Empty", "Icon\\root.png")));
    }

    /**
     * Initial contents in the tree
     *
     * @param root of the tree, in this specific project, root of the tree is the project name
     */
    private void initContent(DefaultMutableTreeNode root) {
        JTree tree = new JTree(new DefaultTreeModel(root));

        DefaultTreeSelectionModel model = new DefaultTreeSelectionModel();
        model.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setSelectionModel(model);
        tree.setCellRenderer(new ComponentTreeCellRenderer());
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.setExpandsSelectedPaths(true);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (node == null) return;
                TreeNode[] path = node.getPath();
                if (node.getLevel() < 1) return;

                node = (DefaultMutableTreeNode) path[1];
                ComponentDetail nodeInfo = (ComponentDetail) node.getUserObject();

                App.getDrawPanel().focusOn(nodeInfo.getName());
            }
        });

        setViewportView(tree);
        setPreferredSize(new Dimension(250, App.getMainWindow().getHeight()));
    }

    /**
     * Show the tree in the tree panel
     *
     * @param project the project to be shown in the tree
     */
    public void draw(Project project) {
        ProjectNode root = new ProjectNode(project);
        initContent(root);
    }
}
