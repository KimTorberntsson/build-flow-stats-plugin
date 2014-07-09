package jenkins.plugins.build_flow_stats;

import org.w3c.dom.Node;
import java.util.ArrayList;

public abstract class Job {

	protected String jobName;
	protected BuildResults theResults;

	public Job(String jobName) {
		this.jobName = jobName;
		theResults = new BuildResults();
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

	public void createBuildsTree(int tabLevel, ArrayList<BuildTreeElement> strings) {
		strings.add(new BuildTreeElement("\n" + XMLJobFactory.createTabLevelString(tabLevel) + jobName + " " + theResults));
	}

}