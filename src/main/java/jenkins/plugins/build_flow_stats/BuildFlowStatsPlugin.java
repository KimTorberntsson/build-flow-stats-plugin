package jenkins.plugins.build_flow_stats;

import hudson.Plugin;
import hudson.Extension;
import org.kohsuke.stapler.export.ExportedBean;
import hudson.model.ManagementLink;

/**
* Starting point for the Build Flow Stats Plugin.
*
* @author Kim Torberntsson
* @plugin
*/
@ExportedBean
public class BuildFlowStatsPlugin extends Plugin {
/**
* Add a link in the administration panel linking to the build metrics search page
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
}
