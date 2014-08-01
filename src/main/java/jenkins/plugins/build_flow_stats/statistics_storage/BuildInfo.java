package jenkins.plugins.build_flow_stats;

import java.text.SimpleDateFormat;
import hudson.model.Build;

public abstract class BuildInfo {

	protected String jobName;
	protected int buildNumber;
	protected String date;
	protected String result;

	public BuildInfo(Build build) {
		jobName = build.getParent().getFullName();
		buildNumber = build.getNumber();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		date = sdf.format(build.getTime());
		result = build.getResult().toString();
	}

	public String getString(int tabLevel) {
		String buildInfo = "";
		buildInfo += "\n" + TabLevel.getString(tabLevel) + "<JobName>" + jobName + "</JobName>";
		buildInfo += "\n" + TabLevel.getString(tabLevel) + "<BuildNumber>" + buildNumber + "</BuildNumber>";
		buildInfo += "\n" + TabLevel.getString(tabLevel) + "<Date>" + date + "</Date>";
		buildInfo += "\n" + TabLevel.getString(tabLevel) + "<Result>" + result + "</Result>";
		return buildInfo;
	}

	public String getDate() {
		return date;
	}

}