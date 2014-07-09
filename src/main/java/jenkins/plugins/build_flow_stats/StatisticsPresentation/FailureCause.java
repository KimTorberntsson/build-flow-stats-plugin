package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;

public class FailureCause {
	
	private String jobName;
	private String failureCauseName;
	private ArrayList<String> builds;

	public FailureCause(String jobName, String failureCauseName, String buildNumber) {
		this.jobName = jobName;
		this.failureCauseName = failureCauseName;
		builds = new ArrayList<String>();
		builds.add(buildNumber);
	}

	public void addBuild(String buildNumber) {
		builds.add(buildNumber);
	}

	public ArrayList<String> getBuilds() {
		return builds;
	}

	public int getNumberOfBuilds() {
		return builds.size();
	}

	public void createBuildTree(int tabLevel, BuildTree buildTree) {
		buildTree.add(new BuildTreeFailureCause(jobName, "\n" + XMLJobFactory.createTabLevelString(tabLevel) + failureCauseName, builds));
	}
	
}