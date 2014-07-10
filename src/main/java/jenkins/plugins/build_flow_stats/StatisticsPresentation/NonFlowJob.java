package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class NonFlowJob extends Job {
	
	protected FailureCauseList failureCauses;

	public NonFlowJob(String jobName) {
		super(jobName);
		failureCauses = new FailureCauseList(jobName);
	}

	public void addBuildFromXML(Node nonFlowBuildNode, FailureCauseList allFailureCauses) {
		Element nonFlowBuildElement = (Element) nonFlowBuildNode;
		String result = nonFlowBuildElement.getElementsByTagName("Result").item(0).getTextContent();
		addResultForBuild(result);
		if (!result.equals("SUCCESS")) {
			String failureCause = nonFlowBuildElement.getElementsByTagName("FailureCause").item(0).getTextContent();
			String buildNumber = nonFlowBuildElement.getElementsByTagName("BuildNumber").item(0).getTextContent();
			failureCauses.addFailureCauseForBuild(failureCause, buildNumber);
			allFailureCauses.addFailureCauseForBuild(failureCause, buildNumber);
		}	
	}

	public void createBuildTree(int tabLevel, BuildTree buildTree) {
		super.createBuildTree(tabLevel, buildTree);
		failureCauses.createBuildTree(tabLevel+1, buildTree);
	}
		
}