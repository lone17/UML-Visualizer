package GUI;

import GUI.*;
import GUI.tree.*;

import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;

public class App {
	private static MainWindow mainWindow;
	private static TreePanel treePanel;
	private static JTextArea text;

	public static MainWindow getMainWindow() {
		return mainWindow;
	}

	public static TreePanel getTreePanel() {
		return treePanel;
	}

	public static JTextArea getText(){
		return text;
	}

	public static void setTreePanel(TreePanel treePanel){
		App.treePanel = treePanel;
	}

	public static void run() {
		mainWindow = MainWindow.getMainWindowInstance();
		treePanel = new TreePanel();
		text = new JTextArea();


		mainWindow.add(treePanel.getScrollPane(), BorderLayout.WEST);
		mainWindow.add(text, BorderLayout.CENTER);
//		JPanel drawPanel = new JPanel();
//		drawPanel.setSize(new Dimension(300, 400));
//		mainWindow.add(drawPanel, BorderLayout.SOUTH);
//		mainWindow.pack();
	}

	public static void main(String[] args){
//		run();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				App.run();
			}
		});

	}

}
