package jenkins.plugins.build_flow_stats;

public abstract class BuildTreeElement {
	
	private String jobName;

	public BuildTreeElement(String jobName) {
		this.jobName = jobName;
	}

	public String getJobName() {
		return jobName;
	}

	public abstract boolean getIsBuildTreeJob();

}