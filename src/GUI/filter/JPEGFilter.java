package GUI.filter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class JPEGFilter extends FileFilter{
	public boolean accept(File f) {
		if (f.isDirectory()) return false;
		String name = f.getName();
		return name.endsWith(".jpeg") || name.endsWith(".JPEG");
	}

	public String getDescription(){
		return "*.jpeg, *.JPEG";
	}
}
