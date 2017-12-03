package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;

import GUI.filter.*;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXSearchPanel;
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

/**
 * Class MenuBar represents a menu bar
 *
 * @author Vu Minh Hieu
 */
public class MenuBar extends JMenuBar {

    private static MenuBar menu = new MenuBar(); // the single MenuBar instance

    private JButton loadProject; // load project button
    private JButton saveAsImage; // save as image button
    private JButton saveAsText; // save as text button
    private JComboBox searchBar; // the search bar

    /**
     * MenuBar private constructor
     */
    private MenuBar() {
        super();

        loadProject = new JButton("Load Project", new ImageIcon("src\\GUI\\icon\\load_project.png"));
        saveAsImage = new JButton("Save as Image", new ImageIcon("src\\GUI\\icon\\save_as_image.png"));
        saveAsText = new JButton("Save as Text", new ImageIcon("src\\GUI\\icon\\save_as_text.png"));
        searchBar = new JComboBox();

//        loadProject.setMaximumSize(new Dimension(150, 30));
//        saveAsImage.setMaximumSize(loadProject.getMaximumSize());
//        saveAsText.setMaximumSize(loadProject.getMaximumSize());

        loadProject.setMnemonic(KeyEvent.VK_L);
        saveAsImage.setMnemonic(KeyEvent.VK_I);
        saveAsText.setMnemonic(KeyEvent.VK_T);

        loadProject.setDisplayedMnemonicIndex(0);
        saveAsImage.setDisplayedMnemonicIndex(8);
        saveAsText.setDisplayedMnemonicIndex(8);

        loadProject.setToolTipText("Alt + L");
        saveAsImage.setToolTipText("Alt + I");
        saveAsText.setToolTipText("Alt + T");

        loadProject.setFocusPainted(false);
        saveAsImage.setFocusPainted(false);
        saveAsText.setFocusPainted(false);

        saveAsImage.setFocusable(false);
        saveAsText.setFocusable(false);

        addLoadListener();
        addSaveAsImageListener();
        addSaveAsTextListener();

        add(loadProject);
    }

    /**
     * Initial search bar's components
     */
    private void initSearchBar() {
        if (GUI.App.getProject() != null) {
            this.remove(searchBar);
            ArrayList<String> items = new ArrayList<>();

            items.add("");
            for (SourceFile file : GUI.App.getProject().getSourceFiles()) {
                for (Extendable object : file.getContainedExtendables()) {
                    String name = object.getName();
                    items.add(name);
                    for (Method method : object.getMethods())
                        items.add(method.presentation() + " | " + name);
                }
            }
            Collections.sort(items, String.CASE_INSENSITIVE_ORDER);

            searchBar = new JComboBox(items.toArray());
            searchBar.setPreferredSize(new Dimension(400, loadProject.getHeight() - 2));
            searchBar.setMaximumSize(new Dimension(600, loadProject.getHeight() - 2));
            searchBar.setLightWeightPopupEnabled(true);
            searchBar.setMaximumRowCount(4);
            searchBar.setAlignmentX(Box.RIGHT_ALIGNMENT);
            AutoCompleteDecorator.decorate(searchBar);
            JLabel label = new JLabel();
            label.setText("Search  ");
            label.setAlignmentY(Box.CENTER_ALIGNMENT);
            label.setFont(new Font("Arial", Font.ITALIC, 12));
            label.setLabelFor(searchBar);
            label.setDisplayedMnemonic('S');
            label.setToolTipText("Alt + S");
            label.setIcon(new ImageIcon("src\\GUI\\icon\\search.png"));
            this.add(Box.createHorizontalGlue());
            this.add(label);
            this.add(searchBar);
            GUI.App.getDrawPanel().grabFocus();
        }
    }

    /**
     * Add listener to listen to load event
     */
    private void addLoadListener(){
        loadProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser(new java.io.File("."));

                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.changeToParentDirectory();
                chooser.setDialogTitle("Choose your source location");
                chooser.setDialogType(JFileChooser.SAVE_DIALOG);

                if (chooser.showDialog(GUI.App.getMainWindow(), "Load") == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    String selectedPath = f.getAbsolutePath();
                    GUI.App.setProject(new Project(selectedPath));

                    if (GUI.App.getProject().getSourceFileCount() == 0) {
                        GUI.App.getEventHistoryPanel().append("No source file was found in " + selectedPath +"\n");
                        JOptionPane.showMessageDialog(GUI.App.getMainWindow(),
                                "The path you have chosen does not contain any source file.\n" +
                                        "Please pick another.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    GUI.App.getTreePanel().draw(GUI.App.getProject());
                    GUI.App.getDrawPanel().draw(GUI.App.getProject());

                    GUI.App.getMainWindow().revalidate();
                    GUI.App.getMainWindow().repaint();

                    loadProject.setFocusable(false);
                    add(saveAsImage);
                    add(saveAsText);
                    updateUI();

                    initSearchBar();
                    addSearchListener();

                    GUI.App.getEventHistoryPanel().append("Loaded "
                                                              + GUI.App.getProject().getSourceFileCount()
                                                              + " file(s) from " + selectedPath + "\n");
                    JOptionPane.showMessageDialog(GUI.App.getMainWindow(),
                            "Loaded file(s): " + GUI.App.getProject().getSourceFileCount());
                }
            }
        });
    }

    /**
     * Add listener to listen to search event
     */
    private void addSearchListener() {
        searchBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                JComboBox box = (JComboBox) e.getSource();
                String item = (String) box.getSelectedItem();
                if (item == null || item.equals(""))
                    return;
                if (item.contains("|"))
                    item = item.substring(item.lastIndexOf('|') + 2, item.length());
                GUI.App.getDrawPanel().focusOn(item);
                searchBar.getEditor().getEditorComponent().addKeyListener(new KeyAdapter(){
                    @Override
                    public void keyReleased(KeyEvent event){
                        if (event.getKeyChar() == KeyEvent.VK_ENTER) {
                            searchBar.setSelectedItem("");
                            GUI.App.getDrawPanel().grabFocus();
                        }
                    }
                });
            }
        });
    }

    /**
     * Add listener to listen to save image event
     */
    private void addSaveAsImageListener() {
        saveAsImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                JFileChooser chooser = new JFileChooser(new java.io.File("."));

                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setDialogTitle("Save as");
                chooser.setSelectedFile(new File(".\\" + GUI.App.getProject().getName()));

                chooser.addChoosableFileFilter(new JPGFilter());
                chooser.addChoosableFileFilter(new JPEGFilter());
                chooser.addChoosableFileFilter(new PNGFilter());
                chooser.addChoosableFileFilter(new BMPFilter());
                chooser.addChoosableFileFilter(new GIFFilter());
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showDialog(GUI.App.getMainWindow(), "Save Image") == JFileChooser.APPROVE_OPTION) {
                    String ext;

                    String description = chooser.getFileFilter().getDescription();
                    if (description.equals("*.jpg, *.JPG")) ext = "jpg";
                    else if (description.equals("*.png, *.PNG")) ext = "png";
                    else if (description.equals("*.jpeg, *.JPEG")) ext = "jpeg";
                    else if (description.equals("*.bmp, *.BMP")) ext = "bmp";
                    else ext = "gif";

                    File f = chooser.getSelectedFile();
                    if (f == null) return;
                    if (!f.getName().toLowerCase().endsWith("." + ext))
                        f = new File(f.getParentFile(), f.getName() + "." + ext);

                    BufferedImage img = GUI.App.getDrawPanel().getDiagram().createImage(BufferedImage.TYPE_INT_RGB);
                    try {
                        ImageIO.write(img, ext, f);
                        GUI.App.getEventHistoryPanel().append("Saved image as " + f.getAbsolutePath() + "\n");
                        JOptionPane.showMessageDialog(GUI.App.getMainWindow(),
                                "Image saved successfully.");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        GUI.App.getEventHistoryPanel().append("Cannot save image as " + f.getAbsolutePath() + "\n");
                        JOptionPane.showMessageDialog(GUI.App.getMainWindow(),
                                "Cannot save Image.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    /**
     * Add listener to listen to save text event
     */
    private void addSaveAsTextListener() {
        saveAsText.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                JFileChooser chooser = new JFileChooser(new java.io.File("."));

                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setDialogTitle("Save as");
                chooser.setSelectedFile(new File(".\\" + GUI.App.getProject().getName()));

                chooser.addChoosableFileFilter(new TXTFilter());
                chooser.setAcceptAllFileFilterUsed(false);

                if (chooser.showDialog(GUI.App.getMainWindow(), "Save Text") == JFileChooser.APPROVE_OPTION) {

                    File f = chooser.getSelectedFile();
                    if (f == null) return;
                    if (!f.getName().toLowerCase().endsWith(".txt"))
                        f = new File(f.getParentFile(), f.getName() + ".txt");
                    try {
                        FileWriter writer = new FileWriter(f);
                        BufferedWriter out = new BufferedWriter(writer);

                        out.write(GUI.App.getProject().toString());

                        out.close();
                        JOptionPane.showMessageDialog(GUI.App.getMainWindow(), "Text file saved successfully.");
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(GUI.App.getMainWindow(), "Cannot save file", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    /**
     * Get the single MenuBar instance
     *
     * @return the single MenuBar instance
     */
    public static MenuBar getInstance() {
        return menu;
    }
}
