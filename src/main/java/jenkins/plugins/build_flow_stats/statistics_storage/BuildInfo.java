package jenkins.plugins.build_flow_stats;

import java.text.SimpleDateFormat;
import hudson.model.Build;

/**
 * Parent class for FlowBuild and RegularBuild
 * @author Kim Torberntsson
 */
public abstract class BuildInfo {

	/**
	 * the analyser that is used for analysing the logs. It is needed both 
	 * for regular builds and flowbuilds, since the flowbuilds need it
	 * for their subbuilds.
	 */
	protected FailureAnalyser analyser;

	/**
	 * the name of the job that the build is from
	 */
	protected String jobName;

	/**
	 * the buildnumber for the build
	 */
	protected int buildNumber;

	/**
	 * the date that the build was built on
	 */
	protected String date;
	
	/**
	 * the result of the build
	 */
	protected String result;

	/**
	 * Creates a build info object from the jenkins build object. Since we are
	 * only interested in some information we just take that info and then use 
	 * the new much smaller objects.
	 * @param  build the build from which the info is gathered
	 * @param  analyser the analyser that is used when analysing the failure causes
	 */
	public BuildInfo(Build build, FailureAnalyser analyser) {
		this.analyser = analyser;
		jobName = build.getParent().getFullName();
		buildNumber = build.getNumber();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		date = sdf.format(build.getTime());
		result = build.getResult().toString();
	}

	/**
	 * Returns a string with the information that is needed for storage to XML-file.
	 * @param  tabLevel the tab level that should be used
	 * @return string with the information for XML-storage
	 */
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