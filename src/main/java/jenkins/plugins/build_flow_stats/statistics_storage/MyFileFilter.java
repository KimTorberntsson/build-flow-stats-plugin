package jenkins.plugins.build_flow_stats;

import java.io.File;
import java.io.FilenameFilter;
	 
public class MyFileFilter implements FilenameFilter {

	@Override
	public boolean accept(File directory, String fileName) {
		return fileName.matches("\\d+");
	}
}