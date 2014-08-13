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
	 * @param  req the request
	 * @param  res the responce
	 * @throws ServletException if some server issues occur
	 * @throws IOException      if some IO issues occur
	 */
	public void doPresentData(StaplerRequest req, StaplerResponse res) throws ServletException, IOException {
		req.setAttribute("jobName", req.getParameter("jobName"));
		CalendarWrapper startDate = getStartDate(Integer.parseInt(req.getParameter("range")), req.getParameter("rangeUnits"));
		req.setAttribute("startDate", startDate);
		req.setAttribute("endDate", new CalendarWrapper());
		BuildTree[] presentationData = getPresentationData(req.getParameter("jobName"), startDate);
		req.setAttribute("buildsTree", presentationData[0]);
		req.setAttribute("allFailureCauses", presentationData[1]);
		req.getView(this, Globals.dataPresentationView).forward(req, res);
	}
	
	/**
	 * Delete data with the options selected by the use
	 * @param  req the request
	 * @param  res the responce
	 * @throws ServletException if some server issues occur
	 * @throws IOException      if some IO issues occur
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
		req.getView(this, Globals.dataDeletionView).forward(req, res);
	}
	
	/**
	 * Create FailureAnalyser from XML-file and get to the edit view
	 * @param  req the request
	 * @param  res the responce
	 * @throws ServletException if some server issues occur
	 * @throws IOException      if some IO issues occur
	 */
	public void doEditFailureCauses(StaplerRequest req, StaplerResponse res) throws ServletException, IOException {
		File failureAnalysisFile = new File(Globals.failureAnalysisFileName);
		if (failureAnalysisFile.exists()) {
			FailureAnalyser analyser = new FailureAnalyser(failureAnalysisFile); 
			req.setAttribute("analyser", analyser);
		}
		req.getView(this, Globals.editFailureCausesView).forward(req, res);
	}
	
	/**
	 * Write failure causes changes to XML and get to a page displaying the results.
	 * @param  req the request
	 * @param  res the responce
	 * @throws ServletException if some server issues occur
	 * @throws IOException      if some IO issues occur
	 */
	public void doStoreFailureCauseChanges(StaplerRequest req, StaplerResponse res) throws ServletException, IOException {
		FailureAnalyser analyser = new FailureAnalyser();
		Map<String, String[]> map = req.getParameterMap();
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String name = iterator.next();
			if (name.startsWith("name")) {
				int nr = Integer.parseInt(name.replace("name_", ""));
				analyser.addFailureCauseRule(new FailureCauseRule(map.get("name_" + nr)[0], map.get("description_" + nr)[0], map.get("patterns_" + nr)));
			}
		}
		File failureAnalysisFile = new File(Globals.failureAnalysisFileName);
		if (!failureAnalysisFile.exists()) {
			new File(Globals.failureAnalysisPath).mkdirs();
			failureAnalysisFile.createNewFile();
		}
		analyser.writeToXML(Globals.failureAnalysisFileName);
		req.getView(this, Globals.indexView).forward(req, res);
	}
	
	/**
	 * Calculate the start date by subtracting time from the current 
	 * date and time according to user options.
	 * @param  range the amount for the time subtraction
	 * @param  rangeUnits the type of unit for the time subtraction
	 * @return the calculated start date
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
	 * Create the Build Tree with the options defined by the use
	 * @param  jobName the name of the job from which data should be collected
	 * @param  startDate the start date for the data collection
	 * @return the build tree with the presentation information
	 */
	public BuildTree[] getPresentationData(String jobName, CalendarWrapper startDate) {
		return XMLJobFactory.getPresentationDataFromFile(Globals.dataPath + jobName, startDate);
	}

	/**
	 * Find all jobs with information stored by the plugin
	 * @return array containing all jobs that have information stored
	 */
	public String[] getStoredJobs() {
		return new File(Globals.dataPath).list();
	}
	
	/**
	 * Delete data between startDate and endDate for jobName
	 * @param  jobName the name of the job from which data should be deleted
	 * @param  startDate the start data for data deletion
	 * @param  endDate the end date for data deletion
	 * @return list of all the files that were deleted
	 */
	public ArrayList<String> deleteData(String jobName, String startDate, String endDate) {
		String jobFolderName = Globals.dataPath + jobName;
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
	 * Deletes all data for jobName]
	 * @param  jobName the name of the job from which data should be deleted
	 * @return list of all the files that were deleted
	 */
	public ArrayList<String> deleteData(String jobName) {
		String jobFolderName = Globals.dataPath + jobName;
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

}