package GUI.tree;

import structure.Interface;
import structure.Extendable;
import structure.Project;
import structure.SourceFile;
import structure.Class;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Represent project node in the tree
 *
 * @author Nguyen Xuan Tung
 */
public class ProjectNode extends DefaultMutableTreeNode {

    /**
     * Constructor
     *
     * @param project Project object
     */
    public ProjectNode(Project project) {
        super(new ComponentDetail(project.getName(), "Icon\\root.png"));

        ArrayList<SourceFile> sourceFiles = project.getSourceFiles();

        for (SourceFile file : sourceFiles) {
            for (Extendable object : file.getContainedExtendables())
                if (object instanceof Class)
                    this.add(new ClassNode((Class) object));
                else
                    this.add(new InterfaceNode((Interface) object));
        }
    }
}
