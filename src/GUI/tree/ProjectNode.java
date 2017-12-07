package GUI.tree;

import structure.Class;
import structure.*;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

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
        super(new ComponentDetail(project.getName(), "icon\\root.png"));

        ArrayList<SourceFile> sourceFiles = project.getSourceFiles();

        for (SourceFile file : sourceFiles) {
            for (Extendable object : file.getContainedExtendables())
                if (object instanceof Class)
                    this.add(new ClassNode((Class) object));
                else this.add(new InterfaceNode((Interface) object));
        }
    }
}
