package jenkins.plugins.build_flow_stats;

import java.util.*;

/**
 * A class that contains a map of failure causes.
 * @author Kim Torberntsson
 */
public class FailureCauseList {
	
	/**
	 * the map which contains the failure causes. The keys are the names 
	 * of the jobs that the failures are associated with.
	 */
	private Map<String, FailureCause> failureCauses;

	/**
	 * Base constructor. Only creates a map without data.
	 */
	public FailureCauseList() {
		failureCauses = new HashMap<String, FailureCause>();
	}

	/**
	 * Adds a failure cause for a build to the list. If the failure cause 
	 * already is in the map, the build is added with information about the job 
	 * that the build was build from and the build number. If the failure cause 
	 * did not already exist in the map a new failurecause isadded to the map.
	 * @param jobName
	 * @param failureCauseName
	 * @param buildNumber
	 */
	public void addFailureCauseForBuild(String jobName, String failureCauseName, String buildNumber) {
		if (failureCauses.isEmpty() || !failureCauses.containsKey(failureCauseName)) {
			failureCauses.put(failureCauseName, new FailureCause(jobName, failureCauseName, buildNumber));
		} else {
			failureCauses.get(failureCauseName).addBuild(jobName, buildNumber);
		}
	}

	/**
	 * Returns a list with all failure causes where the order is based 
	 * on how many builds they have. The failures with the most builds come
	 * first.
	 * @return the sorted list
	 */
	public List<FailureCause> getFailureCausesInSortedList() {
		List<FailureCause> list = new ArrayList<FailureCause>(failureCauses.values());
		Collections.sort(list, new CompareFailureCausesBasedOnNumberOfBuilds());
		return list;
	}

	/**
	 * Adds the information about the builds to the tree that is used for the
	 * presentation with the jelly scripts. 
	 * @param tabLevel the tab level that should be used
	 * @param buildTree the build tree to which the new elements should be added
	 */
	public void createBuildTree(int tabLevel, BuildTree buildTree) {
		Iterator<FailureCause> iterator = getFailureCausesInSortedList().iterator();
		while (iterator.hasNext()) {
			iterator.next().createBuildTree(tabLevel, buildTree);
		}
	}

	/**
	 * Adds the information about the builds to the tree that is used for the
	 * presentation with the jelly scripts. However, the maxElements parameter
	 * sets a limit to how many failure causes will be added.
	 * @param tabLevel
	 * @param buildTree the tab level that should be used
	 * @param maxElements the build tree to which the new elements should be added
	 */
	public void createBuildTree(int tabLevel, BuildTree buildTree, int maxElements) {
		Iterator<FailureCause> iterator = getFailureCausesInSortedList().iterator();
		int elementCount = 0;
		while (iterator.hasNext() && elementCount < maxElements) {
			iterator.next().createBuildTree(tabLevel, buildTree);
			elementCount++;
		}
	}

	/**
	 * Creates a new build tree and adds the failure causes to it.
	 * @return the build tree with the presentation information
	 */
	public BuildTree createBuildTree() {
		BuildTree buildTree = new BuildTree();
		createBuildTree(0, buildTree);
		return buildTree;
	}

	/**
	 * Creates a new build tree and adds the failure causes to it. However, 
	 * the maxElements parameter sets a limit to how many failure causes 
	 * will be added.
	 * @return the build tree with the presentation information
	 */
	public BuildTree createBuildTree(int maxElements) {
		BuildTree buildTree = new BuildTree();
		createBuildTree(0, buildTree, maxElements);
		return buildTree;
	}
	
}