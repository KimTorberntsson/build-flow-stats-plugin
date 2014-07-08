package jenkins.plugins.build_flow_stats;

import java.util.*;

public class JobList {

	private Map<String, Job> jobs;

	public JobList() {
		jobs = new HashMap<String, Job>();
	}

	public void addFlowJob(String flowJobName) {
		if (jobs.isEmpty() || !jobs.containsKey(flowJobName)) {
			jobs.put(flowJobName, new FlowJob(flowJobName));
		}
	}

	public void addNonFlowJob(String nonFlowJobName) {
		if (jobs.isEmpty() || !jobs.containsKey(nonFlowJobName)) {
			jobs.put(nonFlowJobName, new NonFlowJob(nonFlowJobName));
		}
	}

	public Job getJob(String jobName) {
		return jobs.get(jobName);
	}

	public String toString() {
		String ret = "";
		Iterator<String> theKeys = jobs.keySet().iterator();
		while (theKeys.hasNext()) {
			ret = ret + jobs.get(theKeys.next()) + "\n";
		}
		return ret;
	}

	public String getFailedBuildsTree(int tabLevel) {
		String ret = "";
		Iterator<String> theKeys = jobs.keySet().iterator();
		while (theKeys.hasNext()) {
			ret = ret + "\n" + jobs.get(theKeys.next()).getFailedBuildsTree(tabLevel);
		}
		return ret;
	} 

}