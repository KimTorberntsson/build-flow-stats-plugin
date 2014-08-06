package jenkins.plugins.build_flow_stats;

import java.util.*;

/**
 * List of failure causes. 
 */
public class FailureCauseList {
	
	private Map<String, FailureCause> failureCauses;

	public FailureCauseList() {
		failureCauses = new HashMap<String, FailureCause>();
	}

	public void addFailureCauseForBuild(String jobName, String failureCauseName, String buildNumber) {
		if (failureCauses.isEmpty() || !failureCauses.containsKey(failureCauseName)) {
			failureCauses.put(failureCauseName, new FailureCause(jobName, failureCauseName, buildNumber));
		} else {
			failureCauses.get(failureCauseName).addBuild(jobName, buildNumber);
		}
	}

	public List<FailureCause> getFailureCausesInSortedList() {
		List<FailureCause> list = new ArrayList<FailureCause>(failureCauses.values());
		Collections.sort(list, new CompareFailureCausesBasedOnNumberOfBuilds());
		return list;
	}

	public void createBuildTree(int tabLevel, BuildTree buildTree) {
		Iterator<FailureCause> iterator = getFailureCausesInSortedList().iterator();
		while (iterator.hasNext()) {
			iterator.next().createBuildTree(tabLevel, buildTree);
		}
	}

	public void createBuildTree(int tabLevel, BuildTree buildTree, int maxElements) {
		Iterator<FailureCause> iterator = getFailureCausesInSortedList().iterator();
		int elementCount = 0;
		while (iterator.hasNext() && elementCount < maxElements) {
			iterator.next().createBuildTree(tabLevel, buildTree);
			elementCount++;
		}
	}

	public BuildTree createBuildTree() {
		BuildTree buildTree = new BuildTree();
		createBuildTree(0, buildTree);
		return buildTree;
	}

	public BuildTree createBuildTree(int maxElements) {
		BuildTree buildTree = new BuildTree();
		createBuildTree(0, buildTree, maxElements);
		return buildTree;
	}
	
}