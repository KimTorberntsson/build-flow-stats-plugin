package jenkins.plugins.build_flow_stats;

public class FailureBuild {

	private String jobName;
	private String buildNumber;

	public FailureBuild(String jobName, String buildNumber) {
		this.jobName = jobName;
		this.buildNumber = buildNumber;
	}

	public String getJobName() {
		return jobName;
	}

	public String getBuildNumber() {
		return buildNumber;
	}

	public String toString() {
		return buildNumber;
	}
}