package GUI.filter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class TXTFilter extends FileFilter{
	public boolean accept(File f) {
		if (f.isDirectory()) return false;
		String name = f.getName();
		return name.endsWith(".txt") || name.endsWith(".TXT");
	}

	public String getDescription(){
		return "*.txt, *.TXT";
	}
}
