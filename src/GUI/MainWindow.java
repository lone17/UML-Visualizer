package GUI;

import javax.swing.*;

public class MainWindow extends JFrame {

	private int height = 700;
	private int width = 1200;
	private static MainWindow window = new MainWindow();
	private int height = 700;
	private int width = 1200;

    private MainWindow() {
		super("UML Visualizer");

		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static MainWindow getMainWindowInstance() {
		return window;
	}
}
