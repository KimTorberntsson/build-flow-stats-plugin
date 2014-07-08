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

	public String toString() {
		return jobName + "\n" + failureCauses;
	}

	public String getFailedBuildsTree(int tabLevel) {
		return super.getFailedBuildsTree(tabLevel) + failureCauses.getFailedBuildsTree(tabLevel+1);
	}

	public void addBuildToJob(String failureCause, String buildNumber, String resultString) {
		addFailureCauseForBuild(failureCause, buildNumber);
		addResultForBuild(resultString);
	}

	public void addBuildFromXML(Node nonFlowBuildNode) {
		Element nonFlowBuildElement = (Element) nonFlowBuildNode;
		String result = nonFlowBuildElement.getElementsByTagName("Result").item(0).getTextContent();
		if (result.equals("SUCCESS")) {
			addResultForBuild(result);
		} else {
			String buildNumber = nonFlowBuildElement.getElementsByTagName("BuildNumber").item(0).getTextContent();
			String failureCause = nonFlowBuildElement.getElementsByTagName("FailureCause").item(0).getTextContent();
			addBuildToJob(failureCause, buildNumber, result);
		}	
	}
		
}