package jenkins.plugins.build_flow_stats;

import java.util.*;

public class FailureCauseList {
	
	private String jobName;
	private Map<String, FailureCause> failureCauses;

	public FailureCauseList(String jobName) {
		this.jobName = jobName;
		failureCauses = new HashMap<String, FailureCause>();
	}

	public void addFailureCauseForBuild(String failureCause, String buildNumber) {
		if (failureCauses.isEmpty() || !failureCauses.containsKey(failureCause)) {
			failureCauses.put(failureCause, new FailureCause(jobName, failureCause, buildNumber));
		} else {
			failureCauses.get(failureCause).addBuild(buildNumber);
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
	
}