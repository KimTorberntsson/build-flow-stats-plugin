package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;

public class BuildTreeFailureCause extends BuildTreeElement {

	private String failureCauseName;
	private ArrayList<FailureBuild> builds;

	public BuildTreeFailureCause(int tabLevel, String failureCauseName, ArrayList<FailureBuild> builds) {
		super(tabLevel);
		this.failureCauseName = failureCauseName;
		this.builds = builds;
	}

	public boolean getIsBuildTreeJob() {
		return false;
	}

	public String getFailureCauseName() {
		return failureCauseName;
	}

	public ArrayList<FailureBuild> getBuilds() {
		return builds;
	}

	public String getString() {
		return tabLevelString + failureCauseName;
	}

}