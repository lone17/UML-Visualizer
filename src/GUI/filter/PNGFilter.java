package GUI.filter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class PNGFilter extends FileFilter{
	public boolean accept(File f) {
		if (f.isDirectory()) return false;
		String name = f.getName();
		return name.endsWith(".png") || name.endsWith(".PNG");
	}

	public String getDescription(){
		return "*.png, *.PNG";
	}
}
