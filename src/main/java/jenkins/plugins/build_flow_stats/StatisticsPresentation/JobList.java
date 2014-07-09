package jenkins.plugins.build_flow_stats;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

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

	public void createBuildsTree(int tabLevel, ArrayList<BuildTreeElement> strings) {
		Iterator<String> theKeys = jobs.keySet().iterator();
		while (theKeys.hasNext()) {
			jobs.get(theKeys.next()).createBuildsTree(tabLevel, strings);
		}
	} 

	public ArrayList<BuildTreeElement> createBuildsTree() {
		ArrayList<BuildTreeElement> treeElements = new ArrayList<BuildTreeElement>();
		createBuildsTree(0, treeElements);
		return treeElements;
	}

}