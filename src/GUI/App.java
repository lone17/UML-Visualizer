package GUI;

import GUI.tree.*;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import parser.Project;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class App {
	private static Project project;
	private static MainWindow mainWindow;
	private static MenuBar menuBar;
	private static TreePanel treePanel;
	private static DrawPanel drawPanel;
	private static JTextArea text;

	public static MainWindow getMainWindow() {
		return mainWindow;
	}

	public static TreePanel getTreePanel() {
		return treePanel;
	}

	public static DrawPanel getDrawPanel(){
		return drawPanel;
	}

	public static JTextArea getText(){
		return text;
	}

	public static Project getProject() {
		return project;
	}

	public static void setTreePanel(TreePanel treePanel){
		App.treePanel = treePanel;
	}

	public static void setDrawPanel(DrawPanel drawPanel){
		App.drawPanel = drawPanel;
	}

	public static void setProject(Project project) {
		App.project = project;
	}

	public static void run() {
		mainWindow = MainWindow.getMainWindowInstance();
		menuBar = MenuBar.getMenuBarInstance();
		treePanel = new TreePanel();
		drawPanel = new DrawPanel();
		text = new JTextArea();
		text.setPreferredSize(new Dimension(1400, 30));


		mainWindow.add(menuBar, BorderLayout.NORTH);
		mainWindow.add(treePanel, BorderLayout.WEST);
		mainWindow.add(drawPanel, BorderLayout.CENTER);
		mainWindow.add(text, BorderLayout.SOUTH);
	}

	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				App.run();
			}
		});

	}

}
