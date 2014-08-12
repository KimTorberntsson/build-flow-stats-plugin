package jenkins.plugins.build_flow_stats;

import org.w3c.dom.Node;
import java.util.ArrayList;

/**
 * Parent class for FlowJob and RegularJob
 * @author Kim Torberntsson
 */
public abstract class Job {

	/**
	 * the name of the job
	 */
	protected String jobName;
	
	/**
	 * the build results for the job
	 */
	protected BuildResults buildResults;

	/**
	 * Base constructor. Only adds information about the jobname
	 * @param  jobName the name of the job
	 */
	public Job(String jobName) {
		this.jobName = jobName;
		buildResults = new BuildResults();
	}

	/**
	 * Adds results for a build to the job
	 * @param resultString the string with the result for the build
	 */
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

	/**
	 * The subclasses (FlowJob and ResularJob) need to be able to read data from XML.
	 * The failure cause list is included so that they will be able to add information 
	 * about failures from builds to it. That list contains information about all failures
	 * and are therefore not divided by jobs. The result is presented under the section
	 * "Most Common Failure Causes" in the jelly presentation page.
	 * @param node the XML node object
	 * @param allFailureCauses list of failure causes for all the jobs.
	 */
	public abstract void addBuildFromXML(Node node, FailureCauseList allFailureCauses);

	/**
	 * Adds the information for the presentation to a build tree.
	 * @param tabLevel the tab level that should be used
	 * @param buildTree the build tree to which the info should be added
	 */
	public void createBuildTree(int tabLevel, BuildTree buildTree) {
		buildTree.add(new BuildTreeJob(jobName, tabLevel, "" + buildResults));
	}

}