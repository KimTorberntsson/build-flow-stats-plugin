package jenkins.plugins.build_flow_stats;

import java.io.*;
import java.util.*;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.kohsuke.stapler.*;
import net.sf.json.JSONObject;
import jenkins.*;
import jenkins.model.*;
import hudson.*;
import hudson.model.*;
import hudson.tasks.*;
import hudson.util.*;
import javax.servlet.ServletException;

/**
 * This Builder stores information about flowbuilds into XML-files that can later be accessed
 * by the Plugin for presenting statistics.
 */
public class BuildFlowStatsBuilder extends Builder {

    private final String job;
    private final String startDate;
    private final Date startDateObject;

    @DataBoundConstructor
    public BuildFlowStatsBuilder(String job, String startDate) {
        this.job = job;
        this.startDate = startDate;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (startDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            try {
                this.startDateObject = sdf.parse(startDate);
            } catch (ParseException e) {
                throw new RuntimeException("Could not parse start date");
            }
        } else {
            throw new RuntimeException("Wrong format for start date");
        }
    }

    public String getJob() {
        return job;
    }

    public String getStartDate() {
        return startDate;
    }

    @Override
    public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
        PrintStream stream = listener.getLogger();
        StoreData.storeBuildInfoToXML(stream, job, startDateObject, startDate);
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

        public ListBoxModel doFillJobItems() {
            ListBoxModel items = new ListBoxModel();
            Jenkins jenkins = Jenkins.getInstance();
            Iterator<String> jobNamesIterator = jenkins.getJobNames().iterator();
            while (jobNamesIterator.hasNext()) {
                String jobName = jobNamesIterator.next();
                Item item = jenkins.getItem(jobName);
                if (item != null && jenkins.getItem(jobName).getClass().toString().equals("class com.cloudbees.plugins.flow.BuildFlow")) {
                    items.add(jobName);
                }
            }
            return items;
        }

        public FormValidation doCheckStartDate(@QueryParameter String value) throws IOException, ServletException {
            if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return FormValidation.ok();
            } else {
                return FormValidation.error("Wrong date format");
            }
        }
    }
    
}