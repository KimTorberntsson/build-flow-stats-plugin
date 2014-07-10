package jenkins.plugins.build_flow_stats;

public abstract class BuildTreeElement {
	
	protected String tabLevelString;

	public BuildTreeElement(int tabLevel) {
		tabLevelString = createTabLevelString(tabLevel);
	}

	public String createTabLevelString(int i) {
		String tabString = "\n";
		while (i > 0) {
			tabString += "\t";
			i -= 1;
		}
		return tabString;
	}

	public abstract boolean getIsBuildTreeJob();

}