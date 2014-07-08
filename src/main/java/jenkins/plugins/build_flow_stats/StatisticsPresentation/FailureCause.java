package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;

public class FailureCause {
	
	private String failureCauseName;
	private ArrayList<String> builds;

	public FailureCause(String failureCauseName, String buildNumber) {
		this.failureCauseName = failureCauseName;
		builds = new ArrayList<String>();
		builds.add(buildNumber);
	}

	public void addBuild(String buildNumber) {
		builds.add(buildNumber);
	}

	public void getFailedBuildsTree(int tabLevel, ArrayList<String> strings) {
		strings.add(XMLJobFactory.createTabLevelString(tabLevel) + builds.size() 
			+ "st. " + failureCauseName + " " + builds);
	}
	
}