package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicMenuBarUI;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.plaf.multi.MultiMenuBarUI;
import javax.swing.plaf.synth.SynthButtonUI;

import GUI.filter.*;
import GUI.tree.TreePanel;
import com.sun.java.swing.plaf.windows.WindowsRadioButtonMenuItemUI;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import parser.Directory;
import parser.Method;
import parser.Project;
import parser.SourceFile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MenuBar extends JMenuBar{
	private static MenuBar menu = new MenuBar();

	private JButton loadProject = new JButton("Load Project");
	private JButton saveAsImage = new JButton("Save as Picture");
	private JButton saveAsText = new JButton("Save as Text");
	private JComboBox combobox;

	private MenuBar() {
		super();

		this.setUI(new BasicMenuBarUI());
//		this.setPreferredSize(new Dimension(800, 30));

		loadProject.setFocusPainted(false);
		saveAsImage.setFocusPainted(false);
		saveAsText.setFocusPainted(false);

//		loadProject.setUI(new BasicButtonUI());
//		saveAsImage.setUI(new BasicButtonUI());
//		saveAsText.setUI(new BasicButtonUI());

		addLoadListener();
		addSaveAsImageListener();
		addSaveAsTextListener();

		this.add(loadProject);
		this.add(saveAsImage);
		this.add(saveAsText);

//		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	}

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
					//					for (SourceFile file : App.getProject().getSourceFiles()) {
					//						System.out.println(file.getContainedClass());
					//						System.out.println("************************");
					//					}
					App.getTreePanel().draw(App.getProject());
					App.getDrawPanel().draw(App.getProject());
					App.getText().append("Loaded " + App.getTreePanel().getLoadedFilesCount()
							                     + " file(s) from " + selectedPath + "\n");

					App.getMainWindow().revalidate();
					App.getMainWindow().repaint();

					BufferedImage img = App.getDrawPanel().getDiagram().createImage(BufferedImage.TYPE_INT_RGB);

					if(App.getProject() != null) {
						AutoCompleteDecorator decorator;
						ArrayList<String> items = new ArrayList<>();
						for (SourceFile file : App.getProject().getSourceFiles()) {
							String className = file.getContainedClass().getName();
							items.add(className);
							for (Method method : file.getContainedClass().getMethods())
								items.add(method.presentation() + " | " + className);
						}

						Collections.sort(items, String.CASE_INSENSITIVE_ORDER);
						combobox = new JComboBox(items.toArray());
						AutoCompleteDecorator.decorate(combobox);
						combobox.setUI(new BasicComboBoxUI());
						add("Search", combobox);
						combobox.setMaximumSize(new Dimension(600, 30));
						combobox.setMaximumRowCount(4);
						combobox.setAlignmentX(Box.RIGHT_ALIGNMENT);
						addSearchListenner();
					}
					JOptionPane.showMessageDialog(App.getMainWindow(), "Loaded files: " + App.getTreePanel().getLoadedFilesCount());
				}
			}
		});
	}

	private void addSearchListenner() {
		combobox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox box = (JComboBox) e.getSource();
				String item = (String) box.getSelectedItem();
				if (item.contains("|"))
					item = item.substring(item.lastIndexOf("|") + 2, item.length());
				App.getDrawPanel().focusOn(item);
			}
		});
	}

	private void addSaveAsImageListener() {
		saveAsImage.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser(new java.io.File("."));

				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
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

	private void addSaveAsTextListener() {
		saveAsText.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser(new java.io.File("."));

				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
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

	public static MenuBar getMenuBarInstance() {
		return menu;
	}
}
