package GUI;

import javax.swing.*;

public class MainWindow extends JFrame {
	private static MainWindow window = new MainWindow();

	private MainWindow() {
		super("UML Visualizer");

		this.setJMenuBar(MenuBar.getMenuBarInstance());
		this.setSize(600, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static MainWindow getMainWindowInstance() {
		return window;
	}
}
