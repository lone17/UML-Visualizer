package GUI.tree;

import structure.Interface;
import structure.Extendable;
import structure.Project;
import structure.SourceFile;
import structure.Class;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.LinkedList;

public class ProjectNode extends DefaultMutableTreeNode {
    private int sourceFileCount;

    public ProjectNode(Project project) {
        super(new ComponentDetail(project.getName(), "Icon\\root.png"));

        LinkedList<SourceFile> sourceFiles = project.getSourceFiles();
        sourceFileCount = sourceFiles.size();

        for(SourceFile file : sourceFiles) {
            for (Extendable object : file.getContainedExtendables())
                if (object instanceof  Class)
                    this.add(new ClassNode((Class) object));
                else
                    this.add(new InterfaceNode((Interface) object));
        }
    }

    public int getSourceFileCount(){
        return sourceFileCount;
    }
}
