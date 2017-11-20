package GUI;

import javax.swing.*;

import GUI.listenner.*;

public class MenuBar extends JMenuBar{
	private static MenuBar menu = new MenuBar();

	private JMenu fileMenu = new JMenu("File");

	private JMenuItem openProject = new JMenuItem("Open Project");

	private MenuBar() {
		super();

		openProject.addActionListener(new PathPicker(openProject));
		fileMenu.add(openProject);

		this.add(fileMenu);

	}

	public static MenuBar getMenuBarInstance() {
		return menu;
	}
}
