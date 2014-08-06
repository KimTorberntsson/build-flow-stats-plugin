package jenkins.plugins.build_flow_stats;

/**
 * One of the two build tree classes. It contains a job name
 * and the build results for the job.
 */
public class BuildTreeJob extends BuildTreeElement {

	private String jobName;
	private String buildResults;

	public BuildTreeJob(String jobName, int tabLevel, String buildResults) {
		super(tabLevel);
		this.jobName = jobName;
		this.buildResults = buildResults;
	}

	public String getJobName() {
		return jobName;
	}

	public String getBuildResults() {
		return buildResults;
	}

	public boolean getIsBuildTreeJob() {
		return true;
	}

	public String getString() {
		return tabLevelString + jobName + " " + buildResults;
	}
}