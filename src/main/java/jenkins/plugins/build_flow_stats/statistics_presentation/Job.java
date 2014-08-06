package jenkins.plugins.build_flow_stats;

import org.w3c.dom.Node;
import java.util.ArrayList;

/**
 * Parent class for FlowJob and RegularJob
 */
public abstract class Job {

	protected String jobName;
	protected BuildResults buildResults;

	public Job(String jobName) {
		this.jobName = jobName;
		buildResults = new BuildResults();
	}

	public void addResultForBuild(String resultString) {
		if (resultString.equals("SUCCESS")) {
			buildResults.addSuccess();
		} else if (resultString.equals("UNSTABLE")) {
			buildResults.addUnstable();
		} else if (resultString.equals("NOT_BUILT")) {
			buildResults.addNoBuild();
		} else if (resultString.equals("ABORTED")) {
			buildResults.addAbort();
		} else {
			buildResults.addFailure();
		}
	}

	public abstract void addBuildFromXML(Node node, FailureCauseList allFailureCauses);

	public void createBuildTree(int tabLevel, BuildTree buildTree) {
		buildTree.add(new BuildTreeJob(jobName, tabLevel, "" + buildResults));
	}

}