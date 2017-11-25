package GUI.tree;

import parser.Project;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class TreePanel extends JScrollPane{
    private int loadedFilesCount = 0;

    public TreePanel() {
        super();
        initContent(new DefaultMutableTreeNode("Empty"));

    }

    private void initContent(DefaultMutableTreeNode root) {
        JTree tree = new JTree(root);

        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);

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
