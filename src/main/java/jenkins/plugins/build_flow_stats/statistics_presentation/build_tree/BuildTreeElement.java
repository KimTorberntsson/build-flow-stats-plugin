package jenkins.plugins.build_flow_stats;

/**
 * Parent class for BuildTreeFailureCause and BuildTreeJob. It 
 * keeps track of the tab level for the elements. Each element represents 
 * a line in the presentation and contain information about either a job 
 * or a failure cause.
 */
public abstract class BuildTreeElement {
	
	protected String tabLevelString;

	public BuildTreeElement(int tabLevel) {
		tabLevelString = TabLevel.getString(tabLevel);
	}

	public abstract boolean getIsBuildTreeJob();

}