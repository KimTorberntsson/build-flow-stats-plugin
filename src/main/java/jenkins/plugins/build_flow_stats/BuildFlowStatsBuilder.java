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
 * This Builder provides a BuildStep that stores information about 
 * builds into XML-files. These can later be accessed by the plugin for 
 * presenting statistics.
 * @author Kim Torberntsson
 */
public class BuildFlowStatsBuilder extends Builder {

	/**
	 * the job from which data should be collected
	 */
	private final String job;
	
	/**
	 * This defines the earliest date that data will be collected from. 
	 * After the initial data collection the script will collect data from 
	 * the previous date and time of the latest stored build, until the current 
	 * date and time.
	 */
	private final String startDate;
	
	/**
	 * calendar wrapper object of the start date
	 */
	private final CalendarWrapper startDateObject;

	/**
	 * Constructor for creating the build step. It fetches information from
	 * the user via the configure page of the build in jenkins.
	 * @param  job the job from which information should be stored
	 * @param  startDate the start date for collection of data
	 */
	@DataBoundConstructor
	public BuildFlowStatsBuilder(String job, String startDate) {
		this.job = job;
		this.startDate = startDate;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (startDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
			this.startDateObject = new CalendarWrapper(startDate + "_00-00-00");
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

	/**
	 * This method gets called when the build step gets invoked. A StoreData 
	 * object is created for the job and then data is stored.
	 * @param  build the build
	 * @param  launcher the launcher
	 * @param  listener the listener
	 * @return whether the build was successful or not
	 */
	@Override
	public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
		PrintStream stream = listener.getLogger();
		StoreData data = new StoreData(stream, job);
		data.storeBuildInfo(startDateObject);
		return true;
	}

	/**
	 * Descriptor for the BuildFlowStatsBuilder
	 */
	@Extension 
	public static final class BuildFlowStatsBuilderDescriptor extends BuildStepDescriptor<Builder> {

		/**
		 * Indicates that this builder can be used with all kinds of project types 
		 * @param  aClass
		 * @return
		 */
		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		/**
		 * Returns the string that is displayed when the user browses 
		 * for build steps to add to the job.
		 * @return display name
		 */
		public String getDisplayName() {
			return "Store Flow Build Statistics";
		}

		/**
		 * Fills the list with the names of all jobs in the Jenkins instance
		 * @return the names of the jobs
		 */
		public ListBoxModel doFillJobItems() {
			ListBoxModel items = new ListBoxModel();
			Jenkins jenkins = Jenkins.getInstance();
			Iterator<String> jobNamesIterator = jenkins.getJobNames().iterator();
			while (jobNamesIterator.hasNext()) {
				String jobName = jobNamesIterator.next();
				Item item = jenkins.getItem(jobName);
				if (item != null) {
					items.add(jobName);
				}
			}
			return items;
		}

		/**
		 * Check that the start date has the right format
		 * @param  value the string that shall be evaluated
		 * @return whether the start date has the right format or not
		 * @throws IOException      if some IO issues occur
		 * @throws ServletException if some server issues occur
		 */
		public FormValidation doCheckStartDate(@QueryParameter String value) throws IOException, ServletException {
			if (value.matches("\\d{4}-\\d{2}-\\d{2}")) {
				return FormValidation.ok();
			} else {
				 return FormValidation.error("Wrong date format");
			}
		}
	}
	
}