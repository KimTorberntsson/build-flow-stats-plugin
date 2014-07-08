package jenkins.plugins.build_flow_stats;

import java.util.*;
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

	public String toString() {
		return jobName + "\n" + subJobs;
	}

	public String getFailedBuildsTree(int tabLevel) {
		return super.getFailedBuildsTree(tabLevel) + subJobs.getFailedBuildsTree(tabLevel+1);
	}

	public void addResult(String resultString) {
		addResultForBuild(resultString);
	}

	public void addBuildFromXML(Node flowBuildNode) {
		Element flowBuildElement = (Element) flowBuildNode;
		addResult(flowBuildElement.getElementsByTagName("Result").item(0).getTextContent());
		XMLJobFactory.addBuildsFromNode(flowBuildNode, subJobs);
	}
}