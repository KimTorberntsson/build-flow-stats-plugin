package jenkins.plugins.build_flow_stats;

public abstract class BuildTreeElement {
	
	protected String tabLevelString;

	public BuildTreeElement(int tabLevel) {
		tabLevelString = TabLevel.getString(tabLevel);
	}

	public abstract boolean getIsBuildTreeJob();

}