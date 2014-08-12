package jenkins.plugins.build_flow_stats;

import jenkins.model.Jenkins;

public class Globals {

	public final static String jenkinsRootPath = Jenkins.getInstance().getRootDir().toString();
	public final static String rootPath = jenkinsRootPath + "/build-flow-stats/";
	public final static String dataPath = rootPath + "data/";
	public final static String failureAnalysisPath = rootPath + "failure-analysis/";
	public final static String failureAnalysisFileName = failureAnalysisPath + "FailureCauses.xml";
	public final static String indexView = "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/index.jelly";
	public final static String dataPresentationView = "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/presentData.jelly";
	public final static String dataDeletionView = "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/deleteData.jelly";
	public final static String editFailureCausesView = "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/editFailureCauses.jelly";

	public static String getTabLevelString(int i) {
		String tabString = "";
		while (i > 0) {
			tabString += "\t";
			i -= 1;
		}
		return tabString;
	}

}