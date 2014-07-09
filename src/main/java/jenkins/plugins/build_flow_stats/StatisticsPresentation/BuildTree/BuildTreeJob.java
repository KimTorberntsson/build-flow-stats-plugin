package jenkins.plugins.build_flow_stats;

public class BuildTreeJob extends BuildTreeElement {

	private String buildResults;

	public BuildTreeJob(String jobName, int tabLevel, String buildResults) {
		super(jobName, tabLevel);
		this.buildResults = buildResults;
	}

	public boolean getIsBuildTreeJob() {
		return true;
	}

	public String getBuildResults() {
		return buildResults;
	}

	public String getString() {
		return tabLevelString + jobName + " " + buildResults;
	}
}