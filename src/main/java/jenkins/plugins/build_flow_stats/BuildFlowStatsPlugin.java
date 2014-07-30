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
*
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

    public BuildTree[] getPresentationData(String jobName, CalendarWrapper startDate) {
        String rootDir = Jenkins.getInstance().getRootDir().toString();
        String filePath = rootDir + "/userContent/build-flow-stats/" + jobName;
        return XMLJobFactory.getPresentationDataFromFile(filePath, startDate);
    }

    public String[] getStoredJobs() {
        String rootDir = Jenkins.getInstance().getRootDir().toString();
        String filePath = rootDir + "/userContent/build-flow-stats/";
        File folder = new File(filePath);
        return folder.list();
    }

    public CalendarWrapper getStartDate(int range, String rangeUnits) {
        CalendarWrapper startDate = new CalendarWrapper();
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

}