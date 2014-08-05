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
* Starting point for the Build Flow Stats Plugin.
* @author Kim Torberntsson
* @plugin
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

	public void doDeleteData(StaplerRequest req, StaplerResponse res) throws ServletException, IOException {
		String jobName = req.getParameter("jobNameToErase");
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
		if (startDate.matches("\\d{4}-\\d{2}-\\d{2}") && endDate.matches("\\d{4}-\\d{2}-\\d{2}") && endDate.compareTo(startDate) > 0) {
			req.setAttribute("validDateFormat", true);
			ArrayList<String> deletedFiles = deleteData(jobName, startDate, endDate);
			if (!deletedFiles.isEmpty()) {
				req.setAttribute("filesWereDeleted", true);
				req.setAttribute("deletedFiles", deletedFiles);
			} else {
				req.setAttribute("filesWereDeleted", false);
			}
		} else {
			req.setAttribute("validDateFormat", false);
		}
		req.getView(this, "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/deleteData.jelly").forward(req, res);
	}

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

	public BuildTree[] getPresentationData(String jobName, CalendarWrapper startDate) {
		String rootDir = Jenkins.getInstance().getRootDir().toString();
		String filePath = rootDir + "/build-flow-stats/" + jobName; //TODO: Decide path for storage.
		return XMLJobFactory.getPresentationDataFromFile(filePath, startDate);
	}

	public String[] getStoredJobs() {
		String rootDir = Jenkins.getInstance().getRootDir().toString();
		String filePath = rootDir + "/build-flow-stats/"; //TODO: Decide path for storage.
		File folder = new File(filePath);
		return folder.list();
	}

	public ArrayList<String> deleteData(String jobName, String startDate, String endDate) {
		String rootDir = Jenkins.getInstance().getRootDir().toString();
		String filePath = rootDir + "/build-flow-stats/" + jobName;//TODO: Decide path for storage.
		File jobFolder = new File(filePath);
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
		return deletedFiles;
	}

}