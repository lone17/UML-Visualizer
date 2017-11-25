package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;

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

	private JMenuItem openProject = new JMenuItem("Open Project");

	private MenuBar() {
		super();

		openProject.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JFileChooser chooser = new JFileChooser(new java.io.File("."));

				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

				if (chooser.showOpenDialog(App.getMainWindow()) == JFileChooser.APPROVE_OPTION) {
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

				}
			}
		});
		fileMenu.add(openProject);

		this.add(fileMenu);

	}

	public static MenuBar getMenuBarInstance() {
		return menu;
	}
}
