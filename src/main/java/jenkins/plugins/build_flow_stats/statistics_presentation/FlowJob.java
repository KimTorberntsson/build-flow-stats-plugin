package jenkins.plugins.build_flow_stats;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.ArrayList;

/**
 * Contains the relevant information for a flow job i.e a job that uses
 * the Build Flow plugin. It contains a list of subjobs associated with
 * the flow job.
 * @author Kim Torberntsson
 */
public class FlowJob extends Job {

	/**
	 * list of all the subjobs for the build
	 */
	protected JobList subJobs;

	/**
	 * Creates a flowjob. Stores the jobname.
	 * @param  jobName
	 * @return
	 */
	public FlowJob(String jobName) {
		super(jobName);
		subJobs = new JobList();
	}

	/**
	 * Adds a flow job as a sub job
	 * @param flowJobName the name of the subjob
	 */
	public void addFlowJob(String flowJobName) {
		subJobs.addFlowJob(flowJobName);
	}

	/**
	 * Adds a regular job as a subjob
	 * @param regularJobName the name of the subjob
	 */
	public void addRegularJob(String regularJobName) {
		subJobs.addRegularJob(regularJobName);
	}

	/**
	 * Adds the  information about a build from an XML Node object.
	 * @param flowBuildNode the node that contains the information
	 * @param allFailureCauses the list of the failuracauses
	 */
	public void addBuildFromXML(Node flowBuildNode, FailureCauseList allFailureCauses) {
		Element flowBuildElement = (Element) flowBuildNode;
		buildResults.addResultForBuild(flowBuildElement.getElementsByTagName("Result").item(0).getTextContent());
		XMLJobFactory.addBuildsFromNode(flowBuildNode, subJobs, allFailureCauses);
	}

	/**
	 * Adds the information for the presentation to a build tree.
	 * @param tabLevel the tab level that should be used
	 * @param buildTree the build tree to which the info should be added
	 */
	public void createBuildTree(int tabLevel, BuildTree buildTree) {
		super.createBuildTree(tabLevel, buildTree);
		subJobs.createBuildTree(tabLevel+1, buildTree);
	}
}