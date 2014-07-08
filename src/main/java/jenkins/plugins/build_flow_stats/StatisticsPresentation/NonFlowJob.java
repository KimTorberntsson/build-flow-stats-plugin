package jenkins.plugins.build_flow_stats;

import java.util.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class NonFlowJob extends Job {
	
	protected FailureCauseList failureCauses;

	public NonFlowJob(String jobName) {
		super(jobName);
		failureCauses = new FailureCauseList();
	}

	public void addFailureCauseForBuild(String failureCause, String buildNumber) {
		failureCauses.addFailureCauseForBuild(failureCause, buildNumber);
	}

	public void addBuildFromXML(Node nonFlowBuildNode) {
		Element nonFlowBuildElement = (Element) nonFlowBuildNode;
		String result = nonFlowBuildElement.getElementsByTagName("Result").item(0).getTextContent();
		addResultForBuild(result);
		if (!result.equals("SUCCESS")) {
			String failureCause = nonFlowBuildElement.getElementsByTagName("FailureCause").item(0).getTextContent();
			String buildNumber = nonFlowBuildElement.getElementsByTagName("BuildNumber").item(0).getTextContent();
			addFailureCauseForBuild(failureCause, buildNumber);
		}	
	}

	public void getFailedBuildsTree(int tabLevel, ArrayList<String> strings) {
		super.getFailedBuildsTree(tabLevel, strings);
		failureCauses.getFailedBuildsTree(tabLevel+1, strings);
	}
		
}