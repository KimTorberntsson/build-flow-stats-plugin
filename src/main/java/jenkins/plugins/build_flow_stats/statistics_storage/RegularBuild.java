package jenkins.plugins.build_flow_stats;

import hudson.model.Build;

/**
 * Contains the relevant information for a regular build
 */
public class RegularBuild extends BuildInfo {

	protected String failureCause;

	public RegularBuild(Build build, FailureAnalyser analyser) {
		super(build, analyser);
		failureCause = "";
		if (!result.equals("SUCCESS")) {
			failureCause = analyser.matches(build).getName();
		}
	}

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