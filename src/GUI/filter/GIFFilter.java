package GUI.filter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class GIFFilter extends FileFilter{
	public boolean accept(File f) {
		if (f.isDirectory()) return false;
		String name = f.getName();
		return name.endsWith(".gif") || name.endsWith(".GIF");
	}

	public String getDescription(){
		return "*.gif, *.GIF";
	}
}
