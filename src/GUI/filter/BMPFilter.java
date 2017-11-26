package GUI.filter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class BMPFilter extends FileFilter{
	public boolean accept(File f) {
		if (f.isDirectory()) return false;
		String name = f.getName();
		return name.endsWith(".bmp") || name.endsWith(".BMP");
	}

	public String getDescription(){
		return "*.bmp, *.BMP";
	}
}
