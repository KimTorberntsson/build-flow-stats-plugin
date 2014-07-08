package jenkins.plugins.build_flow_stats;

import java.util.*;

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

	public String toString() {
		String ret = "";
		Iterator<String> theKeys = failureCauses.keySet().iterator();
		while (theKeys.hasNext()) {
			ret = ret + failureCauses.get(theKeys.next()) + "\n";
		}
		return ret;
	}

	public String getFailedBuildsTree(int tabLevel) {
		String ret = "";
		Iterator<String> theKeys = failureCauses.keySet().iterator();
		while (theKeys.hasNext()) {
			ret = ret + "\n" + failureCauses.get(theKeys.next()).getFailedBuildsTree(tabLevel);
		}
		return ret;
	}
}