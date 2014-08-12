package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;

/**
 * Contains information about a failure cause, more specifically
 * the name of the failure cause and the failure builds that are 
 * associated with that build. 
 * @author Kim Torberntsson
 */
public class FailureCause {
	
	/**
	 * the name of the failure cause
	 */
	private String failureCauseName;
	
	/**
	 * a list of all the builds that have failed due to the failure cause
	 */
	private ArrayList<FailureBuild> builds;

	/**
	 * Base constructor for a failure cause presentation object
	 * @param  jobName the name of the job that the builds come from
	 * @param  failureCauseName the name of the failure cause
	 * @param  buildNumber the number of the build that should get added to the failure causes
	 */
	public FailureCause(String jobName, String failureCauseName, String buildNumber) {
		this.failureCauseName = failureCauseName;
		builds = new ArrayList<FailureBuild>();
		addBuild(jobName, buildNumber);
	}

	/**
	 * Adds a build to the list of builds associated with the failure cause
	 * @param jobName the jobname for the build
	 * @param buildNumber the number for the build
	 */
	public void addBuild(String jobName, String buildNumber) {
		builds.add(new FailureBuild(jobName, buildNumber));
	}

	public ArrayList<FailureBuild> getBuilds() {
		return builds;
	}

	/**
	 * Calculates and returns the number of builds in the builds list
	 * @return the number of builds
	 */
	public int getNumberOfBuilds() {
		return builds.size();
	}

	/**
	 * Creates a element for the build tree that is used for the presentation that uses jelly
	 * @param tabLevel the tablevel that should be used
	 * @param buildTree the buildtree that the elements should be added to
	 */
	public void createBuildTree(int tabLevel, BuildTree buildTree) {
		buildTree.add(new BuildTreeFailureCause(tabLevel, failureCauseName, builds));
	}
	
}