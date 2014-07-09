package jenkins.plugins.build_flow_stats;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

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

	public void createBuildsTree(int tabLevel, ArrayList<BuildTreeElement> strings) {
		Iterator<String> theKeys = failureCauses.keySet().iterator();
		while (theKeys.hasNext()) {
			failureCauses.get(theKeys.next()).createBuildsTree(tabLevel, strings);
		}
	}
	
}