package GUI.filter;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Class JPGFilter represents a jpg file filter
 *
 * @author Vu Minh Hieu
 */
public class JPGFilter extends FileFilter {

    /**
     * Filter a file
     *
     * @param f the file to checked for filter constrain
     * @return true if the file f is a jpg file
     * false otherwise
     */
    public boolean accept(File f) {
        if (f.isDirectory()) return true;
        String name = f.getName();
        return name.endsWith(".jpg") || name.endsWith(".JPG");
    }

    /**
     * Get the description of this filter
     *
     * @return a String represents the description
     */
    public String getDescription() {
        return "*.jpg, *.JPG";
    }
}
