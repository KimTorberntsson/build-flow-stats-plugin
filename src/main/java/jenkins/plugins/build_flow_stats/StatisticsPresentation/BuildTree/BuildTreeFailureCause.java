package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;

public class BuildTreeFailureCause extends BuildTreeElement {

	private String failureCauseName;
	private ArrayList<String> builds;

	public BuildTreeFailureCause(String jobName, String failureCauseName, ArrayList<String> builds) {
		super(jobName);
		this.failureCauseName = failureCauseName;
		this.builds = builds;
	}

	public boolean getIsBuildTreeJob() {
		return false;
	}

	public String getFailureCauseName() {
		return failureCauseName;
	}

	public ArrayList<String> getBuilds() {
		return builds;
	}




}