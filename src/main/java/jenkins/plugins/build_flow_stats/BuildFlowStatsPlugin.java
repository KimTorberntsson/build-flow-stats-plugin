package jenkins.plugins.build_flow_stats;

import hudson.Plugin;
import hudson.Extension;
import org.kohsuke.stapler.export.ExportedBean;
import hudson.model.ManagementLink;

import java.io.IOException;
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

	public void doGetToDataCollectionOptions(StaplerRequest req, StaplerResponse res) throws ServletException, IOException {
    	req.getView(this, "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/dataCollectionOptions.jelly").forward(req, res);
    }

	public void doGetToDataPresentationOptions(StaplerRequest req, StaplerResponse res) throws ServletException, IOException {
    	req.getView(this, "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/dataPresentationOptions.jelly").forward(req, res);
    }

	public void doPresentData(StaplerRequest req, StaplerResponse res) throws ServletException, IOException {
			InputOptions presentDataOptions = new InputOptions(req);
			req.setAttribute("presentDataOptions", presentDataOptions);
    	req.getView(this, "/jenkins/plugins/build_flow_stats/BuildFlowStatsPlugin/presentData.jelly").forward(req, res);
    }

    public String getMyString() {
        return LoadFromFile.getFilePath();
    }

}
