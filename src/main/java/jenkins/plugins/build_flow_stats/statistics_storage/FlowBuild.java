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
 */
public class FlowBuild extends BuildInfo {

	protected BuildList subBuilds;

	public FlowBuild(Build build) {
		super(build);
		subBuilds = new BuildList();
		addSubBuilds((FlowRun) build);
	}

	private void addSubBuilds(FlowRun flowBuild) {
		Iterator<JobInvocation> subBuilds = flowBuild.getJobsGraph().vertexSet().iterator();
		while (subBuilds.hasNext()) {
			try {
				Build subBuild = (Build) subBuilds.next().getBuild();
				if (subBuild != null && !subBuild.getParent().getFullName().equals(flowBuild.getParent().getFullName())) {
					BuildInfo buildInfo;
					if (subBuild.getClass().toString().equals("class com.cloudbees.plugins.flow.FlowRun")) {
						buildInfo = new FlowBuild(subBuild);
					} else {
						buildInfo = new RegularBuild(subBuild);
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

	public String getString(int tabLevel) {
		String buildInfo = "";
		buildInfo += "\n" + TabLevel.getString(tabLevel) + "<FlowBuild>";
		buildInfo += super.getString(tabLevel + 1);
		buildInfo += subBuilds.getString(tabLevel + 1);
		buildInfo += "\n" + TabLevel.getString(tabLevel) + "</FlowBuild>";
		return buildInfo;
	}

}