package jenkins.plugins.build_flow_stats;

import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * List that contain job objects
 * @author Kim Torberntsson
 */
public class JobList {

	/**
	 * map of all the jobs. The keys are the names of the jobs.
	 */
	private Map<String, Job> jobs;

	/**
	 * Constructor that just creates an empty map.
	 */
	public JobList() {
		jobs = new TreeMap<String, Job>();
	}

	/**
	 * Adds a flow job to the list of jobs.
	 * @param flowJobName the name of the job that should be added
	 */
	public void addFlowJob(String flowJobName) {
		if (jobs.isEmpty() || !jobs.containsKey(flowJobName)) {
			jobs.put(flowJobName, new FlowJob(flowJobName));
		}
	}

	/**
	 * Adds a regular job to the list of jobs.
	 * @param regularJobName the name of the job that should be added
	 */
	public void addRegularJob(String regularJobName) {
		if (jobs.isEmpty() || !jobs.containsKey(regularJobName)) {
			jobs.put(regularJobName, new RegularJob(regularJobName));
		}
	}

	/**
	 * returns the job object from the map with all the jobs
	 * @param  jobName the name of the job that should get retured
	 * @return the job object
	 */
	public Job getJob(String jobName) {
		return jobs.get(jobName);
	}

	/**
	 * Adds the information for the presentation to a build tree.
	 * @param tabLevel the tab level that should be used
	 * @param buildTree the build tree to which the info should be added
	 */
	public void createBuildTree(int tabLevel, BuildTree buildTree) {
		Iterator<String> theKeys = jobs.keySet().iterator();
		while (theKeys.hasNext()) {
			jobs.get(theKeys.next()).createBuildTree(tabLevel, buildTree);
		}
	} 

	/**
	 * Creates a new build tree and adds the presentation information to it.
	 * @return the build tree with the presentation information
	 */
	public BuildTree createBuildTree() {
		BuildTree buildTree = new BuildTree();
		createBuildTree(0, buildTree);
		return buildTree;
	}

}