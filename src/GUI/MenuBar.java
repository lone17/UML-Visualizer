package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;

import GUI.filter.*;
import jdk.nashorn.internal.scripts.JO;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import structure.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class MenuBar extends JMenuBar {
    private static MenuBar menu = new MenuBar();

    private JButton loadProject;
    private JButton saveAsImage;
    private JButton saveAsText;
    private JComboBox comboBox;

    private MenuBar() {
        super();

        loadProject = new JButton("Load Project", new ImageIcon("src\\icon\\load_project.png"));
        saveAsImage = new JButton("Save as Picture", new ImageIcon("src\\icon\\save_as_image.png"));
        saveAsText = new JButton("Save as Text", new ImageIcon("src\\icon\\save_as_text.png"));
        comboBox = new JComboBox();

        loadProject.setFocusPainted(false);
        saveAsImage.setFocusPainted(false);
        saveAsText.setFocusPainted(false);

        saveAsImage.setFocusable(false);
        saveAsText.setFocusable(false);

        addLoadListener();
        addSaveAsImageListener();
        addSaveAsTextListener();

        this.add(loadProject);
    }

    private void initSearchBar() {
        if (App.getProject() != null) {
            this.remove(comboBox);
            ArrayList<String> items = new ArrayList<>();

            items.add("  Search for classes and methods");
            for (SourceFile file : App.getProject().getSourceFiles()) {
                for (Extendable object : file.getContainedExtendables()) {
                    String name = object.getName();
                    items.add(name);
                    for (Method method : object.getMethods())
                        items.add(method.presentation() + " | " + name);
                }
            }
            Collections.sort(items, String.CASE_INSENSITIVE_ORDER);

            comboBox = new JComboBox(items.toArray());
            comboBox.setMaximumSize(new Dimension(500, 30));
            comboBox.setMaximumRowCount(4);
            comboBox.setAlignmentX(Box.RIGHT_ALIGNMENT);
            AutoCompleteDecorator.decorate(comboBox);
            this.add(comboBox);
            App.getDrawPanel().grabFocus();
        }
    }

    private void addLoadListener() {
        loadProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(new java.io.File("."));

                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.changeToParentDirectory();
                chooser.setDialogTitle("Load Project");
                chooser.setDialogType(JFileChooser.SAVE_DIALOG);

                if (chooser.showDialog(App.getMainWindow(), "Load") == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    String selectedPath = f.getAbsolutePath();
                    App.setProject(new Project(selectedPath));

                    App.getTreePanel().draw(App.getProject());
                    App.getDrawPanel().draw(App.getProject());
                    App.getText().append("Loaded " + App.getTreePanel().getLoadedFilesCount()
                            + " file(s) from " + selectedPath + "\n");

                    App.getMainWindow().revalidate();
                    App.getMainWindow().repaint();

                    loadProject.setFocusable(false);
                    add(saveAsImage);
                    add(saveAsText);

                    initSearchBar();
                    addSearchListener();

                    if (App.getTreePanel().getLoadedFilesCount() > 0)
                        JOptionPane.showMessageDialog(App.getMainWindow(), "Loaded file(s): " + App.getTreePanel().getLoadedFilesCount());
                    else
                        JOptionPane.showMessageDialog(App.getMainWindow(), "No source file found", "Input error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void addSearchListener() {
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox box = (JComboBox) e.getSource();
                String item = (String) box.getSelectedItem();
                if (item == null || item.equals("  Search for classes and methods")) return;
                if (item.contains("|"))
                    item = item.substring(item.lastIndexOf('|') + 2, item.length());
                App.getDrawPanel().focusOn(item);
                comboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent event) {
                        if (event.getKeyChar() == KeyEvent.VK_ENTER) {
                            comboBox.setSelectedItem("  Search for classes and methods");
                            App.getDrawPanel().grabFocus();
                        }
                    }
                });
            }
        });
    }

    private void addSaveAsImageListener() {
        saveAsImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(new java.io.File("."));

                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setDialogTitle("Save as");
                chooser.setSelectedFile(new File(".\\" + App.getProject().getName()));

                chooser.addChoosableFileFilter(new JPGFilter());
                chooser.addChoosableFileFilter(new JPEGFilter());
                chooser.addChoosableFileFilter(new PNGFilter());
                chooser.addChoosableFileFilter(new BMPFilter());
                chooser.addChoosableFileFilter(new GIFFilter());
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showDialog(App.getMainWindow(), "Save Picture") == JFileChooser.APPROVE_OPTION) {
                    String ext = "";

                    String description = chooser.getFileFilter().getDescription();
                    if (description.equals("*.jpg, *.JPG"))
                        ext = "jpg";
                    else if (description.equals("*.png, *.PNG"))
                        ext = "png";
                    else if (description.equals("*.jpeg, *.JPEG"))
                        ext = "jpeg";
                    else if (description.equals("*.bmp, *.BMP"))
                        ext = "bmp";
                    else
                        ext = "gif";

                    File f = chooser.getSelectedFile();
                    if (f == null) return;
                    if (!f.getName().toLowerCase().endsWith("." + ext))
                        f = new File(f.getParentFile(), f.getName() + "." + ext);

                    BufferedImage img = App.getDrawPanel().getDiagram().createImage(BufferedImage.TYPE_INT_RGB);
                    try {
                        ImageIO.write(img, ext, f);
                        JOptionPane.showMessageDialog(App.getMainWindow(),"Image saved successfully");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(App.getMainWindow(), "Save error", "Save error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    private void addSaveAsTextListener() {
        saveAsText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(new java.io.File("."));

                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setDialogTitle("Save as");
                chooser.setSelectedFile(new File(".\\" + App.getProject().getName()));

                chooser.addChoosableFileFilter(new TXTFilter());
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showDialog(App.getMainWindow(), "Save Text") == JFileChooser.APPROVE_OPTION) {

                    File f = chooser.getSelectedFile();
                    if (f == null) return;
                    if (!f.getName().toLowerCase().endsWith(".txt"))
                        f = new File(f.getParentFile(), f.getName() + ".txt");
                    try {
                        FileWriter writer = new FileWriter(f);
                        BufferedWriter out = new BufferedWriter(writer);

                        out.write(App.getProject().toString());

                        out.close();
                        JOptionPane.showMessageDialog(App.getMainWindow(), "Text file saved successfully");
                    } catch (IOException ex) {
                        System.out.println(ex);
                        JOptionPane.showMessageDialog(App.getMainWindow(), "Save error", "Save error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public static MenuBar getMenuBarInstance() {
        return menu;
    }
}
