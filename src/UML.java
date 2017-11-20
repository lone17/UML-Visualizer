

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

public class UML {


    public static void SetFrame(JFrame Frame) {
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setSize(1000, 600);
        Frame.setVisible(true);
    }

    private static void addMenu_File(JMenuBar MenuBar) {
        JMenu File = new JMenu("File");
        File.setMnemonic(KeyEvent.VK_F);    //Add shortcut key

        JMenuItem NewFile = new JMenuItem("New file");
        File.add(NewFile);

        final JFileChooser fileDialog = new JFileChooser();

        NewFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileDialog.showOpenDialog(File);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    java.io.File file = fileDialog.getSelectedFile();
                }
            }
        });

        File.addSeparator();

        JMenuItem NewProject = new JMenuItem("New project");
        File.add(NewProject);

        MenuBar.add(File);
    }

    private static void addMenu_Edit(JMenuBar MenuBar) {
        JMenu Edit = new JMenu("Edit");

        JMenuItem Delete = new JMenuItem("Delete");
        Edit.add(Delete);

        JMenu Find = new JMenu("Find");
        Edit.add(Find);

        JMenuItem TestFile = new JMenuItem("Test file");
        Find.add(TestFile);

        JMenuItem SourceFile = new JMenuItem("Source File");
        Find.add(SourceFile);

        MenuBar.add(Edit);
    }

    private static void addMenu_View(JMenuBar MenuBar) {
        JMenu View = new JMenu("View");

        MenuBar.add(View);
    }

    private static void addMenu_New(JMenuBar MenuBar) {
        JMenu New = new JMenu("New");
        JMenuItem Class = new JMenuItem("Class");
        Class.setMnemonic(KeyEvent.VK_C);
        New.add(Class);
        JMenuItem Interface = new JMenuItem("Interface");
        New.add(Interface);
        JMenuItem Property = new JMenuItem("Property");
        New.add(Property);
        JMenuItem Method = new JMenuItem("Method");
        New.add(Method);
        JMenuItem Argument = new JMenuItem("Argument");
        New.add(Argument);

        MenuBar.add(New);
    }

    private static void addMenu_Help(JMenuBar MenuBar) {
        JMenu Help = new JMenu("Help");
        JMenuItem About = new JMenuItem("About");
        Help.add(About);

        About.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Author 1: \nAuthor 2: ", "Author", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        MenuBar.add(Help);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("UML");
        SetFrame(frame);

        JMenuBar MenuBar = new JMenuBar();
        frame.setJMenuBar(MenuBar);

        addMenu_File(MenuBar);

        addMenu_Edit(MenuBar);

        addMenu_View(MenuBar);

        addMenu_New(MenuBar);

        addMenu_Help(MenuBar);
    }

}
