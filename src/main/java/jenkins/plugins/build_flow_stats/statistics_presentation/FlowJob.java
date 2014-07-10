package jenkins.plugins.build_flow_stats;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.ArrayList;

public class FlowJob extends Job {

	protected JobList subJobs;

	public FlowJob(String jobName) {
		super(jobName);
		subJobs = new JobList();
	}

	public void addFlowJob(String flowJobName) {
		subJobs.addFlowJob(flowJobName);
	}

	public void addNonFlowJob(String nonFlowJobName) {
		subJobs.addNonFlowJob(nonFlowJobName);
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