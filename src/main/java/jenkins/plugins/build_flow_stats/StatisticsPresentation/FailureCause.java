package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;

public class FailureCause {
	
	private String failureCauseName;
	private ArrayList<String> builds;

	public FailureCause(String failureCauseName) {
		this.failureCauseName = failureCauseName;
		builds = new ArrayList<String>();
	}

	public FailureCause(String failureCauseName, String buildNumber) {
		this(failureCauseName);
		builds.add(buildNumber);
	}

	public ArrayList<String> getBuilds() {
		return builds;
	}

	public int getNumberOfBuilds() {
		return builds.size();
	}

	public void addBuild(String buildNumber) {
		builds.add(buildNumber);
	}

	public String getFailureCauseName() {
		return failureCauseName;
	}

	public String toString() {
		return builds.size() + "st. " + failureCauseName + " " + builds;
	}

	public String getFailedBuildsTree(int tabLevel) {
		String tab = "";
		while (tabLevel > 0) {
			tab = tab + "\t";
			tabLevel = tabLevel - 1;
		}
		return tab + toString();
	}
}