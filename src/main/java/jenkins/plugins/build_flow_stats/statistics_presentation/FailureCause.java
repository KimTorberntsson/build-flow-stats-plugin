package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;

/**
 * Contains information about a failure cause, more specifically
 * the name of the failure cause and the failure builds that are 
 * associated with that build. 
 */
public class FailureCause {
	
	private String failureCauseName;
	private ArrayList<FailureBuild> builds;

	public FailureCause(String jobName, String failureCauseName, String buildNumber) {
		this.failureCauseName = failureCauseName;
		builds = new ArrayList<FailureBuild>();
		addBuild(jobName, buildNumber);
	}

	public void addBuild(String jobName, String buildNumber) {
		builds.add(new FailureBuild(jobName, buildNumber));
	}

	public ArrayList<FailureBuild> getBuilds() {
		return builds;
	}

	public int getNumberOfBuilds() {
		return builds.size();
	}

	public void createBuildTree(int tabLevel, BuildTree buildTree) {
		buildTree.add(new BuildTreeFailureCause(tabLevel, failureCauseName, builds));
	}
	
}