package jenkins.plugins.build_flow_stats;

/**
 * Static class for the tab level method. 
 */
public class TabLevel {
	/**
	 * Return a string with the specified amount of tab characters
	 */
	public static String getString(int i) {
		String tabString = "";
		while (i > 0) {
			tabString += "\t";
			i -= 1;
		}
		return tabString;
	}
}