package jenkins.plugins.build_flow_stats;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

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

	public void addBuildFromXML(Node flowBuildNode) {
		Element flowBuildElement = (Element) flowBuildNode;
		addResultForBuild(flowBuildElement.getElementsByTagName("Result").item(0).getTextContent());
		XMLJobFactory.addBuildsFromNode(flowBuildNode, subJobs);
	}

	public String toString() {
		return jobName + "\n" + subJobs;
	}

	public String getFailedBuildsTree(int tabLevel) {
		return super.getFailedBuildsTree(tabLevel) + subJobs.getFailedBuildsTree(tabLevel+1);
	}
}