package jenkins.plugins.build_flow_stats;

import org.w3c.dom.Node;

public abstract class Job {

	protected String jobName;
	protected BuildResults theResults;

	public Job(String jobName) {
		this.jobName = jobName;
		theResults = new BuildResults();
	}

	public String getName() {
		return jobName;
	}

	public String getFailedBuildsTree(int tabLevel) {
		String ret = "";
		while (tabLevel > 0) {
			ret = ret + "\t";
			tabLevel = tabLevel - 1;
		}
		return ret + jobName + " " + theResults;
	}

	public void addResultForBuild(String resultString) {
		if (resultString.equals("SUCCESS")) {
			theResults.addSuccess();
		} else if (resultString.equals("UNSTABLE")) {
			theResults.addUnstable();
		} else if (resultString.equals("NOT_BUILT")) {
			theResults.addNoBuild();
		} else if (resultString.equals("ABORTED")) {
			theResults.addAbort();
		} else {
			theResults.addFailure();
		}
	}

	public abstract void addBuildFromXML(Node node);

}