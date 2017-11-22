package GUI.tree;

import parser.Project;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class TreePanel extends JScrollPane{
    private int loadedFilesCount = 0;

    public TreePanel() {
        super();

        init(new DefaultMutableTreeNode("Empty"));

    }

    public TreePanel(String path) {
        super();

        ProjectNode root = new ProjectNode(new Project(path));
        loadedFilesCount = root.getSourceFileCount();

        init(root);
    }

    private void init(DefaultMutableTreeNode root) {
        JTree tree = new JTree(root);

        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);

        setViewportView(tree);
        setPreferredSize(new Dimension(200, 800));
    }

    public int getLoadedFilesCount(){
        return loadedFilesCount;
    }
}
