package jenkins.plugins.build_flow_stats;

import hudson.Plugin;
import hudson.Extension;
import jenkins.*;
import jenkins.model.*;
import org.kohsuke.stapler.export.ExportedBean;
import hudson.model.ManagementLink;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Starting point for the Build Flow Stats Plugin
 * @author Kim Torberntsson
 */
@ExportedBean
public class BuildFlowStatsPlugin extends Plugin {
	
	/**
	 * Add a link in the administration panel linking to the build flow stats index page
	 */
	@Extension
	public static class BuildFlowStatsPluginManagementLink extends ManagementLink {

		public String getIconFileName() {
			return "/plugin/build-flow-stats/icons/build-flow-stats.png";
		}

		public String getDisplayName() {
			return "Build Flow Stats";
		}

		public String getUrlName() {
			return "plugin/build-flow-stats/";
		}

		@Override
		public String getDescription() {
			return "Stores and presents data about builds and subbuilds from the Build Flow Plugin";
		}
	}

	/**
	 * Present data with the options selected by the user
	 */
	public void doPresentData(StaplerRequest req, StaplerResponse res) throws ServletException, IOException {
		req.setAttribute("jobName", req.getParameter("jobName"));
		CalendarWrapper startDate = getStartDate(Integer.parseInt(req.getParameter("range")), req.getParameter("rangeUnits"));
		req.setAttribute("startDate", startDate);
		req.setAttribute("endDate", new CalendarWrapper());
		BuildTree[] presentationData = getPresentationData(req.getParameter("jobName"), startDate);
		req.setAttribute("buildsTree", presentationData[0]);
		req.setAttribute("allFailureCauses", presentationData[1]);
		req.getView(this, "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/presentData.jelly").forward(req, res);
	}

	/**
	 * Delete data with the options selected by the user
	 */
	public void doDeleteData(StaplerRequest req, StaplerResponse res) throws ServletException, IOException {
		String jobNameToDelete = req.getParameter("jobNameToDelete");
		req.setAttribute("jobNameToDelete", jobNameToDelete);
		String deleteAll = req.getParameter("deleteAll");
		if (deleteAll != null) {
			req.setAttribute("validDateFormat", true);
			req.setAttribute("filesWereDeleted", true);
			req.setAttribute("deletedFiles", deleteData(jobNameToDelete));
		} else {
			String startDate = req.getParameter("startDate");
			String endDate = req.getParameter("endDate");
			if (startDate.matches("\\d{4}-\\d{2}-\\d{2}") && endDate.matches("\\d{4}-\\d{2}-\\d{2}") && endDate.compareTo(startDate) > 0) {
				req.setAttribute("validDateFormat", true);
				ArrayList<String> deletedFiles = deleteData(jobNameToDelete, startDate, endDate);
				if (!deletedFiles.isEmpty()) {
					req.setAttribute("filesWereDeleted", true);
					req.setAttribute("deletedFiles", deletedFiles);
				} else {
					req.setAttribute("filesWereDeleted", false);
				}
			} else {
				req.setAttribute("validDateFormat", false);
			}
		}
		req.getView(this, "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/deleteData.jelly").forward(req, res);
	}

	/**
	 * Calculates the start date that the user has selected and returns a CalendarWrapper object
	 * @return the start date as a CalendarWrapper object
	 */
	public CalendarWrapper getStartDate(int range, String rangeUnits) {
		CalendarWrapper startDate = new CalendarWrapper();
		startDate.setTimeToZero();
		if (rangeUnits.equals("Days")) {
			startDate.add(Calendar.DAY_OF_MONTH, -range);
		} else if(rangeUnits.equals("Weeks")) {
			startDate.add(Calendar.WEEK_OF_MONTH, -range);
		} else if(rangeUnits.equals("Months")) {
			startDate.add(Calendar.MONTH, -range);
		} else {
			startDate.add(Calendar.YEAR, -range);
		}
		return startDate;
	}

	/**
	 * Create the Build Tree with the options defined by the user
	 */
	public BuildTree[] getPresentationData(String jobName, CalendarWrapper startDate) {
		return XMLJobFactory.getPresentationDataFromFile(getStoregePath() + jobName, startDate);
	}

	/**
	 * Find all jobs with information stored by the plugin
	 * @return array containing all jobs that have information stored
	 */
	public String[] getStoredJobs() {
		return new File(getStoregePath()).list();
	}

	/**
	 * Delete data between startDate and endDate for jobName
	 */
	public ArrayList<String> deleteData(String jobName, String startDate, String endDate) {
		String jobFolderName = getStoregePath() + jobName;
		File jobFolder = new File(jobFolderName);
		String[] allFiles = jobFolder.list();
		ArrayList<String> deletedFiles = new ArrayList<String>();	
		for (int i = 0; i < allFiles.length; i++) {
			String file = allFiles[i];
			String fileDate = file.replaceAll("_\\d{2}-\\d{2}-\\d{2}.xml", "");
			if (fileDate.compareTo(startDate) >= 0 && fileDate.compareTo(endDate) <= 0) {
				new File(jobFolder + "/" + file).delete();
				deletedFiles.add(file);
			}
		}
		String[] allFilesAfterDeletion = jobFolder.list();
		if (allFilesAfterDeletion == null || allFilesAfterDeletion.length == 0) {
			new File(jobFolderName).delete();
		}
		return deletedFiles;
	}

	/**
	 * Delete all data for jobName
	 */
	public ArrayList<String> deleteData(String jobName) {
		String jobFolderName = getStoregePath() + jobName;
		File jobFolder = new File(jobFolderName);
		String[] allFiles = jobFolder.list();
		ArrayList<String> deletedFiles = new ArrayList<String>();	
		for (int i = 0; i < allFiles.length; i++) {
			String file = allFiles[i];
			new File(jobFolder + "/" + file).delete();
			deletedFiles.add(file);
		}
		new File(jobFolderName).delete();
		return deletedFiles;
	}

	/**
	 * Get the storage path for this plugin
	 * @return the storage path
	 */
	public static String getStoregePath() {
		return Jenkins.getInstance().getRootDir().toString() + "/build-flow-stats/data/"; //TODO: Decide storage path
	}

}