package jenkins.plugins.build_flow_stats;

import hudson.model.Build;

/**
 * Contains the relevant information for a regular build
 * @author Kim Torberntssons
 */
public class RegularBuild extends BuildInfo {

	/**
	 * the name of the failure cause for the build
	 */
	protected String failureCause;

	/**
	 * Base constructor. Adds information to the object from the jenkins
	 * build object and searches for failure causes. 
	 * @param  build the jenkins build object from which data is collected
	 * @param  analyser for analysing failure cause
	 * @return
	 */
	public RegularBuild(Build build, FailureAnalyser analyser) {
		super(build, analyser);
		failureCause = "";
		if (!result.equals("SUCCESS")) {
			failureCause = analyser.matches(build).getName();
		}
	}

	/**
	 * Returns a string with the information that is needed for storage to XML-file.
	 * @param  tabLevel the tab level that should be used
	 * @return string with the information for XML-storage
	 */
	public String getString(int tabLevel) {
		String buildInfo = "";
		buildInfo += "\n" + Globals.getTabLevelString(tabLevel) + "<Build>";
		buildInfo += super.getString(tabLevel + 1);
		if (!failureCause.equals("")) {
			buildInfo += "\n" + Globals.getTabLevelString(tabLevel + 1) + "<FailureCause>" + failureCause + "</FailureCause>";
		}
		buildInfo += "\n" + Globals.getTabLevelString(tabLevel) + "</Build>";
		return buildInfo;
	}
}