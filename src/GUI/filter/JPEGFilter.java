package GUI.filter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Class JPEGFilter represents a jpeg file filter
 *
 * @author Vu Minh Hieu
 */
public class JPEGFilter extends FileFilter {

    /**
     * Filter a file
     *
     * @param f the file to checked for filter constrain
     * @return true if the file f is a jpeg file
     *         false otherwise
     */
	public boolean accept(File f) {
		if (f.isDirectory()) return true;
		String name = f.getName();
		return name.endsWith(".jpeg") || name.endsWith(".JPEG");
	}

	/**
	 * Get the description of this filter
	 *
	 * @return a String represents the description
	 */
	public String getDescription() {
		return "*.jpeg, *.JPEG";
	}
}
