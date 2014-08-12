package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;

/**
 * One of the two build tree classes. It contains a failure cause name
 * and a list of FailureBuilds associated with that failure cause.
 * @author Kim Torberntsson
 */
public class BuildTreeFailureCause extends BuildTreeElement {

	/**
	 * the name of the failure cause
	 */
	private String failureCauseName;

	/**
	 * the builds that matches the failure cause
	 */
	private ArrayList<FailureBuild> builds;

	/**
	 * Constructor for creating the failure cause element
	 * @param  tabLevel the tab level that should be used
	 * @param  failureCauseName the name of the failure cause
	 * @param  builds the builds that should be added 
	 */
	public BuildTreeFailureCause(int tabLevel, String failureCauseName, ArrayList<FailureBuild> builds) {
		super(tabLevel);
		this.failureCauseName = failureCauseName;
		this.builds = builds;
	}

	/**
	 * checks if the element is a build element and returns false 
	 * since it is not.
	 * @return returns false
	 */
	public boolean getIsBuildTreeJob() {
		return false;
	}

	/**
	 * returns the name with the proper tab level
	 * @return
	 */
	public String getString() {
		return tabLevelString + failureCauseName;
	}

	public String getFailureCauseName() {
		return failureCauseName;
	}

	public ArrayList<FailureBuild> getBuilds() {
		return builds;
	}

}