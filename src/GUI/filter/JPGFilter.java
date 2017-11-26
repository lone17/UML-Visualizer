package GUI.filter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class JPGFilter extends FileFilter {
	public boolean accept(File f) {
		if (f.isDirectory()) return false;
		String name = f.getName();
		return name.endsWith(".jpg") || name.endsWith(".JPG");
	}

	public String getDescription() {
		return "*.jpg, *.JPG";
	}
}
