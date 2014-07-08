package jenkins.plugins.build_flow_stats;

import hudson.Plugin;
import hudson.Extension;
import jenkins.*;
import jenkins.model.*;
import org.kohsuke.stapler.export.ExportedBean;
import hudson.model.ManagementLink;

import java.io.IOException;
import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.StaplerRequest;

import java.util.*;

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

	public void doGetToDataCollectionOptions(StaplerRequest req, StaplerResponse res) throws ServletException, IOException {
    	req.getView(this, "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/dataCollectionOptions.jelly").forward(req, res);
    }

	public void doGetToDataPresentationOptions(StaplerRequest req, StaplerResponse res) throws ServletException, IOException {
    	req.getView(this, "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/dataPresentationOptions.jelly").forward(req, res);
    }

	public void doPresentData(StaplerRequest req, StaplerResponse res) throws ServletException, IOException {
			InputOptions presentDataOptions = new InputOptions(req);
			req.setAttribute("presentDataOptions", presentDataOptions);
            req.setAttribute("failedBuildsTreeForView", getFailedBuildsTreeForView());
    	req.getView(this, "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/presentData.jelly").forward(req, res);
    }

    public String getFailedBuildsTreeForView() {
        
        String rootDir = Jenkins.getInstance().getRootDir().toString();
        //TODO: This should be made in a more general way based on user options.
        String filePath = rootDir + "/userContent/testcases_statistics/Builds-2014-06-18.xml";

        JobList allJobs = XMLJobFactory.getAllJobsFromFile(filePath);
        return allJobs.getFailedBuildsTree();
    }

    public ArrayList<String> getListOfStrings() {
        ArrayList<String> theStrings = new ArrayList<String>();
        String string1 = "tn-delivery [Successes: 18, Failures: 2, Aborts: 4, Unstables: 0, Not Built: 0, Total Builds: 24, Failure Rate: 8.33%]";
        String string2 = "\ttn-PL1 [Successes: 13, Failures: 3, Aborts: 0, Unstables: 4, Not Built: 0, Total Builds: 20, Failure Rate: 15.0%]";
        String string3 = "\t\t1st. Temporary fail explanation 2 [29074]";
        String string4 = "\t\t1st. Temporary fail explanation 3 [29073]";
        String string5 = "\t\t1st. Temporary fail explanation 4 [29082]";
        String string6 = "\t\t4st. Unstable Build [29129, 29130, 29124, 29064]";
        String string7 = "\ttn-PL2 [Successes: 5, Failures: 5, Aborts: 7, Unstables: 3, Not Built: 0, Total Builds: 20, Failure Rate: 25.0%]";
        String string8 = "\t\t7st. Aborted [7077, 7079, 7076, 7078, 7074, 7080, 7073]";
        theStrings.add(string1);
        theStrings.add(string2);
        theStrings.add(string3);
        theStrings.add(string4);
        theStrings.add(string5);
        theStrings.add(string6);
        theStrings.add(string7);
        theStrings.add(string8);
        return theStrings;
    }
}