package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Contains the relevant information for a regular job i.e. a job that 
 * is not a job that uses the Build Flow plugin. 
 * @author Kim Torberntsson
 */
public class RegularJob extends Job {
	
		/**
		 * list of all the failure causes for the job
		 */
	protected FailureCauseList failureCauses;

	/**
	 * Creates the object and adds information about the job name
	 * @param  jobName
	 * @return
	 */
	public RegularJob(String jobName) {
		super(jobName);
		failureCauses = new FailureCauseList();
	}

	/**
	 * Adds information about a build from a XML Node to the job. 
	 * @param regularBuildNode the node from which the info should be collected
	 * @param allFailureCauses the list of failure causes
	 */
	public void addBuildFromXML(Node regularBuildNode, FailureCauseList allFailureCauses) {
		Element nonFlowBuildElement = (Element) regularBuildNode;
		String result = nonFlowBuildElement.getElementsByTagName("Result").item(0).getTextContent();
		addResultForBuild(result);
		if (!result.equals("SUCCESS")) {
			String failureCause = nonFlowBuildElement.getElementsByTagName("FailureCause").item(0).getTextContent();
			String buildNumber = nonFlowBuildElement.getElementsByTagName("BuildNumber").item(0).getTextContent();
			failureCauses.addFailureCauseForBuild(jobName, failureCause, buildNumber);
			allFailureCauses.addFailureCauseForBuild(jobName, failureCause, buildNumber);
		}	
	}

	/**
	 * Adds the information for the presentation to a build tree.
	 * @param tabLevel the tab level that should be used
	 * @param buildTree the build tree to which the info should be added
	 */
	public void createBuildTree(int tabLevel, BuildTree buildTree) {
		super.createBuildTree(tabLevel, buildTree);
		failureCauses.createBuildTree(tabLevel+1, buildTree);
	}
		
}