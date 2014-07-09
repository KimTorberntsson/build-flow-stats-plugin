package jenkins.plugins.build_flow_stats;

public abstract class BuildTreeElement {
	
	protected String jobName;
	protected String tabLevelString;

	public BuildTreeElement(String jobName, int tabLevel) {
		this.jobName = jobName;
		tabLevelString = createTabLevelString(tabLevel);
	}

	public String getJobName() {
		return jobName;
	}

	public String createTabLevelString(int i) {
		String tabString = "\n";
		while (i > 0) {
			tabString += "\t";
			i -= 1;
		}
		return tabString;
	}

	public abstract boolean getIsBuildTreeJob();

}