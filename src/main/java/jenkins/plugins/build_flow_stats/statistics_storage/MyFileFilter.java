package jenkins.plugins.build_flow_stats;

import java.io.File;
import java.io.FilenameFilter;

/**
 * A file filter that is used to find only the symbolic links that are 
 * relevant, in our case only those whose names are just numbers. This 
 * filter should work as long as the internal storage of Jenkins remains
 * the same, which seems very likely.
 * @author Kim Torberntsson
 */	 
public class MyFileFilter implements FilenameFilter {

	/**
	 * Method that filters away filenames that does not only contain numbers
	 * @param  directory the directory where the file is located
	 * @param  fileName the name of the file
	 * @return whether the filename is accepted or not
	 */
	@Override
	public boolean accept(File directory, String fileName) {
		return fileName.matches("\\d+");
	}
}