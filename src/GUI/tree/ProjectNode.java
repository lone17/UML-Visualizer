package GUI.tree;

import parser.Project;
import parser.SourceFile;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.LinkedList;

public class ProjectNode extends DefaultMutableTreeNode {
    private int sourceFileCount;

    public ProjectNode(Project project) {
        super(project.getName());

        LinkedList<SourceFile> sourceFiles = project.getSourceFiles();
        sourceFileCount = sourceFiles.size();

        for(SourceFile file : sourceFiles) {
            this.add(new ClassNode(file.getContainedClass()));
        }
    }

    public int getSourceFileCount(){
        return sourceFileCount;
    }
}
