package GUI;

import GUI.tree.*;
import structure.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;

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
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
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
