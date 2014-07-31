package jenkins.plugins.build_flow_stats;

public class TabLevel {

	public static String getString(int i) {
		String tabString = "";
		while (i > 0) {
			tabString += "\t";
			i -= 1;
		}
		return tabString;
	}

}