package GUI;

import javax.swing.*;

public class MainWindow extends JFrame {
	private static MainWindow window = new MainWindow();

	private MainWindow() {
		super("UML Visualizer");

		this.setSize(1400, 800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static MainWindow getMainWindowInstance() {
		return window;
	}
}
