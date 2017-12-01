package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;

import GUI.filter.*;
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
public class MenuBar extends JMenuBar{

	private static MenuBar menu = new MenuBar(); // the single MenuBar instance

	private JButton loadProject; // load project button
	private JButton saveAsImage; // save as image button
	private JButton saveAsText; // save as text button
	private JComboBox comboBox; // the search bar

	/**
	 * MenuBar private constructor
	 */
	private MenuBar() {
		super();

		loadProject = new JButton("Load Project");
		saveAsImage = new JButton("Save as Picture");
		saveAsText = new JButton("Save as Text");
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

	/**
	 * Initial search bar's components
	 */
	private void initSearchBar() {
		if(App.getProject() != null) {
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

	/**
	 * Add listener to listen to load event
	 */
	private void addLoadListener() {
		loadProject.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
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

					JOptionPane.showMessageDialog(App.getMainWindow(), "Loaded files: " + App.getTreePanel().getLoadedFilesCount());
				}
			}
		});
	}

	/**
	 * Add listener to listen to search event
	 */
	private void addSearchListener() {
		comboBox.addActionListener(new ActionListener(){
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

	/**
	 * Add listener to listen to save image event
	 */
	private void addSaveAsImageListener() {
		saveAsImage.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser(new java.io.File("."));

				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setDialogTitle("Save as");
				chooser.setSelectedFile( new File(".\\" + App.getProject().getName()) );

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
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}

	/**
	 * Add listener to listen to save text event
	 */
	private void addSaveAsTextListener() {
		saveAsText.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser(new java.io.File("."));

				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.setDialogTitle("Save as");
				chooser.setSelectedFile( new File(".\\" + App.getProject().getName()) );

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
					} catch (IOException ex) {
						System.out.println(ex);
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
	public static MenuBar getMenuBarInstance() {
		return menu;
	}
}
