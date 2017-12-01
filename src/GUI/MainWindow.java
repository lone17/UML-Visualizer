package GUI;

import javax.swing.*;

public class MainWindow extends JFrame {

	private int height = 700; // the height of the window
	private int width = 1200; // the width of the window

	// the single MainWindow instance
	private static MainWindow window = new MainWindow();

	/**
	 * private MainWindow constructor
	 */
	private MainWindow() {
		super("UML Visualizer");

		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/**
	 * Get the single MainWindow instance
	 *
	 * @return the single MainWindow instance
	 */
	public static MainWindow getMainWindowInstance() {
		return window;
	}
}
