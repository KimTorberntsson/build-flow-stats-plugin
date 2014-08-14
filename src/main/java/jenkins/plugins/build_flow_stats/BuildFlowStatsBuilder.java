package jenkins.plugins.build_flow_stats;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
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
	private String job;

	/**
	 * whether regex is used for the job selection or not
	 */
	private boolean useRegEx = false;
	
	/**
	 * This defines the earliest date that data will be collected from. 
	 * After the initial data collection the script will collect data from 
	 * the previous date and time of the latest stored build, until the current 
	 * date and time.
	 */
	private String startDate;
	
	/**
	 * calendar wrapper object of the start date
	 */
	private CalendarWrapper startDateObject;

	/**
	 * Constructor for creating the build step. It fetches information from
	 * the user via the configure page of the build in jenkins.
	 * @param  job the job from which information should be stored
	 * @param  startDate the start date for collection of data
	 */
	@DataBoundConstructor
	public BuildFlowStatsBuilder(String job, boolean useRegEx, String startDate, CalendarWrapper startDateObject) {
		this.job = job;
		this.useRegEx = useRegEx;
		this.startDate = startDate;
		this.startDateObject = startDateObject;
	}

	public String getJob() {
		return job;
	}

	public boolean getUseRegEx() {
		return useRegEx;
	}

	public String getStartDate() {
		return startDate;
	}

	/**
	 * This method gets called when the build step gets invoked. If a regex is used for the
	 * job selection data will be collected for all jobs that match the regex. Otherwise data
	 * will be collected for the job selected in the list. 
	 * @param  build the build
	 * @param  launcher the launcher
	 * @param  listener the listener
	 * @return whether the build was successful or not
	 */
	@Override
	public boolean perform(AbstractBuild build, Launcher launcher, BuildListener listener) {
		PrintStream stream = listener.getLogger();
		if (useRegEx) {
			stream.println("\nUsing regex for job selection. The regex is: " + job + "\n");
			Pattern pattern = Pattern.compile(job);
			Iterator<String> jobs = getJobNames();
			while (jobs.hasNext()) {
				String job = jobs.next();
				if (pattern.matcher(job).matches()) {
					stream.println("Saving data for: " + job);
					StoreData data = new StoreData(stream, job);
					data.storeBuildInfo(startDateObject);
				}
			}
		} else {
			stream.println("Saving data for: " + job);
			StoreData data = new StoreData(stream, job);
			data.storeBuildInfo(startDateObject);
		}
		return true;
	}

	/**
	 * Returns an iterator that iterates through all the jobs that exist
	 * in the Jenkins instance.
	 * @return the iterator
	 */
	private static Iterator<String> getJobNames() {
		ArrayList<String> jobNames = new ArrayList<String>();
		Jenkins jenkins = Jenkins.getInstance();
		Iterator<String> jobs = jenkins.getJobNames().iterator();
		//Check if the jobs really exist
		while (jobs.hasNext()) {
			String job = jobs.next();
			if (jenkins.getItem(job) != null) {
				jobNames.add(job);
			}
		}
		return jobNames.iterator();
	}

	/**
	 * Descriptor for the BuildFlowStatsBuilder
	 */
	@Extension 
	public static final class BuildFlowStatsBuilderDescriptor extends BuildStepDescriptor<Builder> {

		/**
		 * Creates a BuildFlowStatsBuilder object from the parameters defined by the 
		 * user. The parameters are all available from the parameter formData.
		 * @param  req a stapler request
		 * @param  formData JSON object that contains the parameters selected by the user
		 * @return the builder object
		 */
		@Override
		public BuildFlowStatsBuilder newInstance(StaplerRequest req, JSONObject formData) {
			String job = formData.getJSONObject("useRegEx").getString("job");
			boolean useRegEx = Boolean.parseBoolean(formData.getJSONObject("useRegEx").getString("value"));
			String startDate = formData.getString("startDate");
			CalendarWrapper startDateObject;
			if (startDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
				startDateObject = new CalendarWrapper(startDate + "_00-00-00");
			} else {
				throw new RuntimeException("Wrong format for start date");
			}
			return new BuildFlowStatsBuilder(job, useRegEx, startDate, startDateObject);
		}

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
			Iterator<String> jobs = getJobNames();
			while (jobs.hasNext()) {
				items.add(jobs.next());
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