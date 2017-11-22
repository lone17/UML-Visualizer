package GUI;

import GUI.*;
import GUI.tree.*;

import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;

import com.mxgraph.util.*;

public class App {
	private static MainWindow mainWindow;
	private static TreePanel treePanel;
	private static JScrollPane drawPanel;
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
		drawPanel = new DrawPanel();
		text = new JTextArea();
		text.setPreferredSize(new Dimension(1400, 30));


		mainWindow.add(treePanel, BorderLayout.WEST);
		mainWindow.add(drawPanel, BorderLayout.CENTER);
		mainWindow.add(text, BorderLayout.SOUTH);
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
