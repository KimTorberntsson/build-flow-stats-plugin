package jenkins.plugins.build_flow_stats;

import java.io.File;
import java.io.FilenameFilter;

/**
 * A file filter that is used to find only the symbolic links that are 
 * relevant, in our case only those whose names are just numbers.
 */	 
public class MyFileFilter implements FilenameFilter {

	@Override
	public boolean accept(File directory, String fileName) {
		return fileName.matches("\\d+");
	}
}