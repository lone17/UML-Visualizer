package GUI.tree;

import parser.Project;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class TreePanel extends JScrollPane{
    private JTree tree;
    private JScrollPane scrollPane;
    private int loadedFilesCount = 0;

    public TreePanel() {
        super();

        init(new DefaultMutableTreeNode("Empty"));

        this.add(scrollPane);
    }

    public TreePanel(String path) {
        super();

        ProjectNode root = new ProjectNode(new Project(path));
        loadedFilesCount = root.getSourceFileCount();

        init(root);

        this.add(scrollPane);
    }

    private void init(DefaultMutableTreeNode root) {
        tree = new JTree(root);

        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);

        scrollPane = new JScrollPane(tree);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(200, 600));
    }

    public int getLoadedFilesCount(){
        return loadedFilesCount;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }
}
