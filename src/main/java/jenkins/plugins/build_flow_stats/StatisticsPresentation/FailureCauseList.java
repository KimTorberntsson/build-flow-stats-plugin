package jenkins.plugins.build_flow_stats;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

public class FailureCauseList {
	
	private Map<String, FailureCause> failureCauses;

	public FailureCauseList() {
		failureCauses = new HashMap<String, FailureCause>();
	}

	public void addFailureCauseForBuild(String failureCause, String buildNumber) {
		if (failureCauses.isEmpty() || !failureCauses.containsKey(failureCause)) {
			failureCauses.put(failureCause, new FailureCause(failureCause, buildNumber));
		} else {
			failureCauses.get(failureCause).addBuild(buildNumber);
		}
	}

	public void getFailedBuildsTree(int tabLevel, ArrayList<String> strings) {
		Iterator<String> theKeys = failureCauses.keySet().iterator();
		while (theKeys.hasNext()) {
			failureCauses.get(theKeys.next()).getFailedBuildsTree(tabLevel, strings);
		}
	}
	
}