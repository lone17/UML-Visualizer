package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;

import GUI.filter.*;
import GUI.tree.TreePanel;
import parser.Project;
import parser.SourceFile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuBar extends JMenuBar{
	private static MenuBar menu = new MenuBar();

	private JMenu fileMenu = new JMenu("File");
	private JMenu save = new JMenu("Save as");

	private JMenuItem loadProject = new JMenuItem("Load Project");
	private JMenuItem asImage = new JMenuItem("As Picture");

	private MenuBar() {
		super();

		addLoadListener();
		fileMenu.add(loadProject);

		addSaveAsImageListener();
		save.add(asImage);

		this.add(fileMenu);
		this.add(save);
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

					BufferedImage img = App.getDrawPanel().diag.createImage(BufferedImage.TYPE_INT_RGB);
				}
			}
		});
	}

	private void addSaveAsImageListener() {
		asImage.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser(new java.io.File("."));

				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setDialogTitle("Save as");
				chooser.setSelectedFile( new File(".\\UMLImage") );

				chooser.addChoosableFileFilter(new JPGFilter());
				chooser.addChoosableFileFilter(new JPEGFilter());
				chooser.addChoosableFileFilter(new PNGFilter());
				chooser.addChoosableFileFilter(new BMPFilter());
				chooser.addChoosableFileFilter(new GIFFilter());
				chooser.setAcceptAllFileFilterUsed(false);

				if (chooser.showDialog(App.getMainWindow(), "Save Picture") == JFileChooser.APPROVE_OPTION) {
					String ext = "";

					String discription = chooser.getFileFilter().getDescription();
					if (discription.equals("*.jpg, *.JPG"))
						ext = "jpg";
					else if (discription.equals("*.png, *.PNG"))
						ext = "png";
					else if (discription.equals("*.jpeg, *.JPEG"))
						ext = "jpeg";
					else if (discription.equals("*.bmp, *.BMP"))
						ext = "bmp";
					else
						ext = "gif";

					File f = chooser.getSelectedFile();
					if (f == null) return;
					if (!f.getName().toLowerCase().endsWith("." + ext))
						f = new File(f.getParentFile(), f.getName() + "." + ext);

					BufferedImage img = App.getDrawPanel().diag.createImage(BufferedImage.TYPE_INT_RGB);
					try {
						ImageIO.write(img, ext, f);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}

	public static MenuBar getMenuBarInstance() {
		return menu;
	}
}
