package GUI.tree;

import GUI.App;
import structure.Project;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;

public class TreePanel extends JScrollPane{
    private int loadedFilesCount = 0;

    public TreePanel() {
        super();
        initContent(new DefaultMutableTreeNode(new ComponentDetail("Empty", "Icon\\root.png")));
//        this.getHorizontalScrollBar().setBackground(new Color(90, 112, 160));
//        this.getVerticalScrollBar().setBackground(new Color(90, 112, 160));
//        this.getHorizontalScrollBar().setUI(new BasicScrollBarUI());
//        this.getVerticalScrollBar().setUI(new BasicScrollBarUI());
    }

    private void initContent(DefaultMutableTreeNode root) {
        JTree tree = new JTree(new DefaultTreeModel(root));

        DefaultTreeSelectionModel model = new DefaultTreeSelectionModel();
        model.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setSelectionModel(model);
        tree.setCellRenderer(new ComponentTreeCellRenderer());
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.setExpandsSelectedPaths(true);
        tree.addTreeSelectionListener(new TreeSelectionListener(){
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
        setPreferredSize(new Dimension(300, 800));
    }

    public void draw(Project project) {
        ProjectNode root = new ProjectNode(project);
        loadedFilesCount = root.getSourceFileCount();
        initContent(root);
    }

    public int getLoadedFilesCount(){
        return loadedFilesCount;
    }
}
