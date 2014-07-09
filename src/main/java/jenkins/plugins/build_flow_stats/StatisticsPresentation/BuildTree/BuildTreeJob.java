package jenkins.plugins.build_flow_stats;

public class BuildTreeJob extends BuildTreeElement {

	private String buildResults;

	public BuildTreeJob(String jobName, String buildResults) {
		super(jobName);
		this.buildResults = buildResults;
	}

	public boolean getIsBuildTreeJob() {
		return true;
	}

	public String getBuildResults() {
		return buildResults;
	}
}