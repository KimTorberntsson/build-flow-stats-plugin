package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;

public class BuildTreeElement {
	
	private String jobName;
	private String info;
	private boolean hasBuilds;
	private ArrayList<String> builds;

	public BuildTreeElement(String jobName, String info, ArrayList<String> builds) {
		this.jobName = jobName;
		this.info = info;
		this.hasBuilds = true;
		this.builds = builds;
	}

	public BuildTreeElement(String info) {
		this.jobName = null;
		this.info = info;
		this.hasBuilds = false;
		this.builds = null;
	}

	public String getJobName() {
		return jobName;
	}

	public String getInfo() {
		return info;
	}

	public boolean getHasBuilds() {
		return hasBuilds;
	}

	public ArrayList<String> getBuilds() {
		return builds;
	}
}