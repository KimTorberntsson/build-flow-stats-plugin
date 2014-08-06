package jenkins.plugins.build_flow_stats;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.ArrayList;

/**
 * Contains the relevant information for a flow job i.e a job that uses
 * the Build Flow plugin. It contains a list of subjobs associated with
 * the flow job.
 */
public class FlowJob extends Job {

	protected JobList subJobs;

	public FlowJob(String jobName) {
		super(jobName);
		subJobs = new JobList();
	}

	public void addFlowJob(String flowJobName) {
		subJobs.addFlowJob(flowJobName);
	}

	public void addRegularJob(String regularJobName) {
		subJobs.addRegularJob(regularJobName);
	}

	public void addBuildFromXML(Node flowBuildNode, FailureCauseList allFailureCauses) {
		Element flowBuildElement = (Element) flowBuildNode;
		addResultForBuild(flowBuildElement.getElementsByTagName("Result").item(0).getTextContent());
		XMLJobFactory.addBuildsFromNode(flowBuildNode, subJobs, allFailureCauses);
	}

	public void createBuildTree(int tabLevel, BuildTree buildTree) {
		super.createBuildTree(tabLevel, buildTree);
		subJobs.createBuildTree(tabLevel+1, buildTree);
	}
}