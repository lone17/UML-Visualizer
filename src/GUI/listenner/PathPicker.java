package GUI.listenner;

import GUI.App;
import GUI.tree.TreePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class PathPicker implements ActionListener {

	private JMenuItem target;
	private String selectedPath;

	public PathPicker(JMenuItem target) {
		this.target = target;
		this.selectedPath = "";
	}

	@Override
	public void actionPerformed(ActionEvent e){
		if (e.getSource() == target) {
			JFileChooser chooser = new JFileChooser(new java.io.File("."));

			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			int i = chooser.showOpenDialog(App.getMainWindow());
			if (i == JFileChooser.APPROVE_OPTION) {
				File f = chooser.getSelectedFile();
				selectedPath = f.getAbsolutePath();
				App.getMainWindow().remove(App.getTreePanel().getScrollPane());
				App.setTreePanel(new TreePanel(selectedPath));
				App.getMainWindow().add(App.getTreePanel().getScrollPane(), BorderLayout.WEST);
				App.getText().append("Loaded " + App.getTreePanel().getLoadedFilesCount()
						                + " file(s) from " + f.getAbsolutePath() + "\n");
				App.getMainWindow().revalidate();
				App.getMainWindow().repaint();
			}
		}
	}
}
