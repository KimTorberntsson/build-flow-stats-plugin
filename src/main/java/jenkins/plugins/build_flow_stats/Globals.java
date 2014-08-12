package jenkins.plugins.build_flow_stats;

import jenkins.model.Jenkins;

/**
 * Class with all variables and methods that should be global. I made 
 * this class in order to have all hardcoded strings etc. in one place.
 * @author Kim Torberntsson
 */
public class Globals {

	/**
	 * the root path of jenkins
	 */
	public final static String jenkinsRootPath = Jenkins.getInstance().getRootDir().toString();
	
	/**
	 * the root path of the flow build stats plugin i.e. the place where all
	 * information collected by the plugin gets stored
	 */
	public final static String rootPath = jenkinsRootPath + "/build-flow-stats/";
	
	/**
	 * the path that contains the data of all the jobs
	 */
	public final static String dataPath = rootPath + "data/";
	
	/**
	 * the path where the failure causes for the analysis part 
	 * of the plugin are stored
	 */
	public final static String failureAnalysisPath = rootPath + "failure-analysis/";
	
	/**
	 * the xml file where all the failure causes are stored. The plugin can 
	 * read and save data to the xml as long as the data follows the right format.
	 * This makes it possible to edit the causes using the GUI or manually. 
	 * As long as the file is stored in the right path, the rigth filename and using
	 * the proper saving format the file will be read and used by the plugin.
	 */
	public final static String failureAnalysisFileName = failureAnalysisPath + "FailureCauses.xml";
	
	/**
	 * the path to the index jelly file
	 */
	public final static String indexView = "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/index.jelly";
	
	/**
	 * the path to the data presentation jelly file
	 */
	public final static String dataPresentationView = "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/presentData.jelly";
	
	/**
	 * the path to the data deletion jelly file
	 */
	public final static String dataDeletionView = "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/deleteData.jelly";
	
	/**
	 * the path to the failure cause editor jelly file
	 */
	public final static String editFailureCausesView = "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/editFailureCauses.jelly";

	/**
	 * Returns a string based on the tab level
	 * @param  i the tab level that should be used
	 * @return string with the tab level string
	 */
	public static String getTabLevelString(int i) {
		String tabString = "";
		while (i > 0) {
			tabString += "\t";
			i -= 1;
		}
		return tabString;
	}

}