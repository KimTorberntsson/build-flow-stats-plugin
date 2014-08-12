package jenkins.plugins.build_flow_stats;

/**
 * One of the two build tree classes. It contains a job name
 * and the build results for the job.
 * @author Kim Torberntsson
 */
public class BuildTreeJob extends BuildTreeElement {

	/**
	 * the jobname 
	 */
	private String jobName;
	
	/**
	 * the results for the job
	 */
	private String buildResults;

	/**
	 * Constructor for the job element. 
	 * @param  jobName the name of the job
	 * @param  tabLevel the tab level that should be used
	 * @param  buildResults the build results for the job
	 */
	public BuildTreeJob(String jobName, int tabLevel, String buildResults) {
		super(tabLevel);
		this.jobName = jobName;
		this.buildResults = buildResults;
	}

	/**
	 * checks if the element is a build element and returns true 
	 * since it is.
	 * @return returns true
	 */
		public boolean getIsBuildTreeJob() {
		return true;
	}

	/**
	 * Creates a presentation string that contains the jobname and the build results
	 * with the proper tab level.
	 * @return the presentation string
	 */
	public String getString() {
		return tabLevelString + jobName + " " + buildResults;
	}

	public String getJobName() {
		return jobName;
	}

	public String getBuildResults() {
		return buildResults;
	}

}