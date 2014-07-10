package jenkins.plugins.build_flow_stats;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;
import net.sf.json.JSONObject;

/**
 * This Builder stores information about flowbuilds into XML-files that can later be accessed
 * by the Plugin for presenting statistics.
 */
public class BuildFlowStatsBuilder extends Builder {

    private final String job;

    @DataBoundConstructor
    public BuildFlowStatsBuilder(String job) {
        this.job = job;
    }

    public String getJob() {
        return job;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        listener.getLogger().println("Collecting data for " + job);
        return true;
    }

    @Extension 
    public static final class BuildFlowStatsBuilderDescriptor extends BuildStepDescriptor<Builder> {

        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        public String getDisplayName() {
            return "Store Flow Build Statistics";
        }

    }
}