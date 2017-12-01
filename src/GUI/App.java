package GUI;

import GUI.tree.*;
import structure.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;

/**
 * Class App represents the UML-Visualizer itself
 *
 * @author Vu Minh Hieu
 */
public class App {

	private static Project project; // the project to be visualized
	private static MainWindow mainWindow; // the main window where everything is shown
	private static MenuBar menuBar; // the application's menu bar
	private static TreePanel treePanel; // where the project's tree is shown
	private static DrawPanel drawPanel; // where the project's diagram is drawn
	private static JTextArea text; // event history

	/**
	 * Return the main window of the Application
	 *
	 * @return the main window
	 */
	public static MainWindow getMainWindow() {
		return mainWindow;
	}

	/**
	 * Return the application's tree panel
	 *
	 * @return the tree panel
	 */
	public static TreePanel getTreePanel() {
		return treePanel;
	}

	/**
	 * Return the application's draw panel
	 *
	 * @return the draw panel
	 */
	public static DrawPanel getDrawPanel(){
		return drawPanel;
	}

	/**
	 * Return the text area where event histories are written
	 * @return the bottom text area
	 */
	public static JTextArea getText(){
		return text;
	}

	/**
	 * Return the application's project
	 *
	 * @return the application's project
	 */
	public static Project getProject() {
		return project;
	}

	/**
	 * Set the application's tree panel
	 *
	 * @param treePanel the desired tree panel
	 */
	public static void setTreePanel(TreePanel treePanel){
		App.treePanel = treePanel;
	}

	/**
	 * Set the application's draw panel
	 *
	 * @param drawPanel the disired draw panel
	 */
	public static void setDrawPanel(DrawPanel drawPanel){
		App.drawPanel = drawPanel;
	}

	/**
	 * Set the application's project
	 *
	 * @param project the desired project
	 */
	public static void setProject(Project project) {
		App.project = project;
	}

	/**
	 * Run the application
	 */
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
		text.setPreferredSize(new Dimension(mainWindow.getWidth(), 30));
		text.setEditable(false);


		mainWindow.add(menuBar, BorderLayout.NORTH);
		mainWindow.add(treePanel, BorderLayout.WEST);
		mainWindow.add(drawPanel, BorderLayout.CENTER);
//		mainWindow.add(text, BorderLayout.SOUTH);
	}

	/**
	 * Execute everything
	 *
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				App.run();
			}
		});

	}

}
