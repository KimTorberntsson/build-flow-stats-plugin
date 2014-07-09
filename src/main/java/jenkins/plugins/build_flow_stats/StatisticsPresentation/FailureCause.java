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

	public void createBuildsTree(int tabLevel, ArrayList<BuildTreeElement> strings) {
		strings.add(new BuildTreeElement(jobName, "\n" + XMLJobFactory.createTabLevelString(tabLevel) + failureCauseName, builds));
	}
	
}