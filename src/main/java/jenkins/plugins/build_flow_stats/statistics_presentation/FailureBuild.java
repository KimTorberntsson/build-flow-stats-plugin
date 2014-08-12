package jenkins.plugins.build_flow_stats;

/**
 * Contains information about a failed build. It has information
 * about the job name and the build number. The failure cause
 * for the failure build is specified by which FailureCause 
 * the failed build is located in.
 * @author Kim Torberntsson
 */
public class FailureBuild {

	/**
	 * the name of the job that the build is build from
	 */
	private String jobName;
	
	/**
	 * the build number of the job
	 */
	private String buildNumber;

	/**
	 * Base constructor. Stores information about jobname and buildnumber
	 * @param  jobName the name of the job
	 * @param  buildNumber the build number
	 */
	public FailureBuild(String jobName, String buildNumber) {
		this.jobName = jobName;
		this.buildNumber = buildNumber;
	}

	/**
	 * Returns the build number as a string
	 * @return the build number
	 */
	public String toString() {
		return buildNumber;
	}

	public String getJobName() {
		return jobName;
	}

	public String getBuildNumber() {
		return buildNumber;
	}
	
}