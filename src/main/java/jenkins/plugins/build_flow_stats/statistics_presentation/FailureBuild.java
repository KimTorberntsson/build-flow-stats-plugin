package jenkins.plugins.build_flow_stats;

/**
 * Contains information about a failed build. It has information
 * about the job name and the build number. The failure cause
 * for the failure build is specified by which FailureCause 
 * the failed build is located in.
 */
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