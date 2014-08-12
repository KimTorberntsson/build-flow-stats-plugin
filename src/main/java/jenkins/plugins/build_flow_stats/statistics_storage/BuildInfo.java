package jenkins.plugins.build_flow_stats;

import java.text.SimpleDateFormat;
import hudson.model.Build;

/**
 * Parent class for FlowBuild and RegularBuild
 */
public abstract class BuildInfo {

	protected FailureAnalyser analyser;
	protected String jobName;
	protected int buildNumber;
	protected String date;
	protected String result;

	public BuildInfo(Build build, FailureAnalyser analyser) {
		this.analyser = analyser;
		jobName = build.getParent().getFullName();
		buildNumber = build.getNumber();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		date = sdf.format(build.getTime());
		result = build.getResult().toString();
	}

	public String getString(int tabLevel) {
		String buildInfo = "";
		buildInfo += "\n" + Globals.getTabLevelString(tabLevel) + "<JobName>" + jobName + "</JobName>";
		buildInfo += "\n" + Globals.getTabLevelString(tabLevel) + "<BuildNumber>" + buildNumber + "</BuildNumber>";
		buildInfo += "\n" + Globals.getTabLevelString(tabLevel) + "<Date>" + date + "</Date>";
		buildInfo += "\n" + Globals.getTabLevelString(tabLevel) + "<Result>" + result + "</Result>";
		return buildInfo;
	}

	public String getDate() {
		return date;
	}

}