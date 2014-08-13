package jenkins.plugins.build_flow_stats;

/**
 * Parent class for BuildTreeFailureCause and BuildTreeJob. It 
 * keeps track of the tab level for the elements. Each element represents 
 * a line in the presentation and contain information about either a job 
 * or a failure cause.
 * @author Kim Torberntsson
 */
public abstract class BuildTreeElement {
	
	/**
	 * variable for storing the proper tab level
	 */
	protected String tabLevelString;

	/**
	 * Base constructor. Only creates the string.
	 * @param  tabLevel the tab level that should be used
	 */
	public BuildTreeElement(int tabLevel) {
		tabLevelString = Globals.getTabLevelString(tabLevel);
	}

	/**
	 * Abstract method for deciding if the instance is a job element. Should be
	 * possible to redisign so that this method would not be necessary, but
	 * since i am dealing with jelly scripts with the presentation I could not
	 * come up with a better solution.
	 */
	public abstract boolean getIsBuildTreeJob();

}