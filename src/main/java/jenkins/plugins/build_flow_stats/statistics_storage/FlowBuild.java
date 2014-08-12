package jenkins.plugins.build_flow_stats;

import java.util.Iterator;
import hudson.model.Build;
import com.cloudbees.plugins.flow.FlowRun;
import com.cloudbees.plugins.flow.JobInvocation;
import java.util.concurrent.ExecutionException;

/**
 * Contains the relevant information for a flow build.
 * It has the information of a regular build, except the information 
 * about failure cause, and a list of subbuilds that are associated 
 * with the build.
 * @author Kim Torberntsson
 */
public class FlowBuild extends BuildInfo {

	/**
	 * list of the subbuilds for the flow build
	 */
	protected BuildList subBuilds;

	/**
	 * Creates a flow build object and adds information from the jenkins build
	 * object. The analyser is used for analysing failure causes.
	 * @param  build the jenkins build object from where the information is gathered
	 * @param  analyser for failure cause analysis
	 */
	public FlowBuild(Build build, FailureAnalyser analyser) {
		super(build, analyser);
		subBuilds = new BuildList();
		addSubBuilds((FlowRun) build);
	}

	/**
	 * Adds information about subbuilds from the jenkins build 
	 * object to the flow build. Note that the method is recursive making
	 * it possible to have multiple levels of flow builds.
	 * @param flowBuild the flow build object from which data is collected
	 */
	private void addSubBuilds(FlowRun flowBuild) {
		Iterator<JobInvocation> subBuilds = flowBuild.getJobsGraph().vertexSet().iterator();
		while (subBuilds.hasNext()) {
			try {
				Build subBuild = (Build) subBuilds.next().getBuild();
				if (subBuild != null && !subBuild.getParent().getFullName().equals(flowBuild.getParent().getFullName())) {
					BuildInfo buildInfo;
					if (subBuild.getClass().toString().equals("class com.cloudbees.plugins.flow.FlowRun")) {
						buildInfo = new FlowBuild(subBuild, analyser);
					} else {
						buildInfo = new RegularBuild(subBuild, analyser);
					}
					subBuild = null;
					this.subBuilds.addBuildInfo(buildInfo);
				}
			} catch (ExecutionException ee) {
				//TODO: Fix the exception handling
			} catch (InterruptedException ie) {
				//TODO: Fix the exception handling
			} 
		}
	}

	/**
	 * Returns a string with the information that is needed for storage to XML-file.
	 * @param  tabLevel the tab level that should be used
	 * @return string with the information for XML-storage
	 */
	public String getString(int tabLevel) {
		String buildInfo = "";
		buildInfo += "\n" + Globals.getTabLevelString(tabLevel) + "<FlowBuild>";
		buildInfo += super.getString(tabLevel + 1);
		buildInfo += subBuilds.getString(tabLevel + 1);
		buildInfo += "\n" + Globals.getTabLevelString(tabLevel) + "</FlowBuild>";
		return buildInfo;
	}

}