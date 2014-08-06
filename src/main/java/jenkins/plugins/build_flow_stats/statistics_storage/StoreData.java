package jenkins.plugins.build_flow_stats;

import java.io.*;
import java.util.*;
import java.util.Iterator;
import java.util.ListIterator;
import hudson.model.Build;
import hudson.model.Project;
import jenkins.model.Jenkins;
import hudson.util.RunList;

/**
 * Main class for storing data. Each StoreData-object is responsible for
 * the data storage of the job defined by the variable jobName.
 */
public class StoreData {

	private PrintStream stream;
	private String jobName;
	private Jenkins jenkins;
	private Project project;
	private String rootDir;
	private String storePath;
	private File storePathFile;
	private String[] oldFiles;
	private File buildsPath;

	/**
 	* Constructor for the StoreData class
 	* @param stream A Printstream object for the log
 	* @param jobName Defines the job that information should be collected for
 	*/
	public StoreData(PrintStream stream, String jobName) {
		this.stream = stream;
		this.jobName = jobName;
		stream.println("Collecting and storing data to XML-file for " + jobName);
		jenkins = Jenkins.getInstance();
		project = (Project) jenkins.getItem(jobName);
		rootDir = jenkins.getRootDir().toString();
		storePath = rootDir + "/build-flow-stats/" + jobName + "/"; //TODO: Decide path for storage.
		storePathFile = new File(storePath);
		storePathFile.mkdirs();
		oldFiles = storePathFile.list();
		buildsPath = new File(rootDir + "/jobs/" + jobName + "/builds");
	}

	/**
	 * Store information about a job. It detects if information
	 * has previously been stored for the job. If so, it continues the 
	 * data storage from the date of the latest stored build
	 * @param userStartDate The start date defined by the user
	 */
	public void storeBuildInfo(CalendarWrapper userStartDate) {
		CalendarWrapper startDate;
		if (oldFiles == null || oldFiles.length == 0) {
			startDate = userStartDate;
			stream.println("No previous data found for " + jobName + ". Saving logs from " + startDate.getDate());
		} else {
			String latestStoredFile = oldFiles[oldFiles.length - 1].replace(".xml", "");
			if (latestStoredFile.compareTo(userStartDate.toString()) < 0) {
				startDate = userStartDate;
				stream.println("Previous data found for " + jobName + ". Saving logs from " + startDate.getDate());
			} else {
				startDate = new CalendarWrapper(latestStoredFile);
				stream.println("Data has already been collected from " + userStartDate.getDate());
				stream.println("Continue storing of logs from " + startDate.getDate());
				appendToLatestFile(startDate);
				startDate.setTimeToZero();
				startDate.add();
			}
		}
		CalendarWrapper endDate = new CalendarWrapper();
		CalendarWrapper tempStartDate = new CalendarWrapper(startDate.toString());
		CalendarWrapper tempEndDate = new CalendarWrapper(startDate.toString());
		tempEndDate.add();
		while (tempStartDate.compareTo(endDate) <= 0) {
			storeBuildInfoToXML(tempStartDate, tempEndDate, false);
			tempStartDate.add();
			tempEndDate.add();
		}
	}

	/**
	 * Append build information to XML-file
	 * @param startDate Builds built during this day will be appended
	 */
	private void appendToLatestFile(CalendarWrapper startDate) {
		CalendarWrapper endDate = new CalendarWrapper(startDate.toString());
		endDate.setTimeToZero();
		endDate.add();
		storeBuildInfoToXML(startDate, endDate, true);
	}

	/**
	 * Store data from startDate to endDate. The method can append or
	 * create new files based on user options.
	 * @param startDate The first date from which information is stored
	 * @param endDate The last date from which information is stored
	 * @param append If data should be appended or not
	 */
	private void storeBuildInfoToXML(CalendarWrapper startDate, CalendarWrapper endDate, boolean append) {
		BuildList builds = getBuilds(getBuildNumbers(startDate, endDate));
		if (builds.isEmpty()) {
			stream.println("No data for " + startDate.getDate());
		} else {
			writeToFile(builds, append);
		}
	}

	/**
	 * Return a list of the build numbers from which data should be collected.
	 * The build numbers are collected using the file system, instead of from 
	 * build objects for better performance and less memory usage.
	 * @param startDate The first date from which information is stored
	 * @param endDate The last date from which information is stored
	 * @return Array with the build numbers.
	 */
	private ArrayList<Integer> getBuildNumbers(CalendarWrapper startDate, CalendarWrapper endDate) {
		FilenameFilter filter = new MyFileFilter();
		File[] files = buildsPath.listFiles(filter);
		ArrayList<Integer> buildNumbers = new ArrayList<Integer>();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			try {
				String buildDate = file.getCanonicalPath().replaceAll(".*/", "");
				if (buildDate.compareTo(startDate.toString()) > 0 && buildDate.compareTo(endDate.toString()) < 0) {
					buildNumbers.add(Integer.parseInt(file.toString().replaceAll(".*/", "")));
				}
			} catch (IOException e) {
				e.printStackTrace(); //TODO:Fix this exception
			}
		}
		return buildNumbers;
	}

	/**
	 * Takes a list of build numbers as input and creates the objects.
	 * then returns the build objects in a BuildList.
	 * @param buildNumbers List of all the builds numbers that should be included
	 * @return BuildList that contains all the build objects.
	 */
	private BuildList getBuilds(ArrayList<Integer> buildNumbers) {
		BuildList builds = new BuildList();
		Iterator<Integer> iterator = buildNumbers.iterator();
		while (iterator.hasNext()) {
			Build build = (Build) project.getBuildByNumber(iterator.next());
			if (build != null) {
				BuildInfo buildInfo;
				if (build.getClass().toString().equals("class com.cloudbees.plugins.flow.FlowRun")) {
					buildInfo = new FlowBuild(build);
				} else {
					buildInfo = new RegularBuild(build);
				}
				build = null;
				builds.addBuildInfo(buildInfo);
			}
		}
		return builds;
	}

	/**
	 * Write the build information to file. Data can be appended or written to a 
	 * new file based on user options. Each file contain builds for one day
	 * and the file is named after the last build in that file. This is in order 
	 * to make it easy to append data to files from the right date and time.
	 * @param builds The builds that should be written to file
	 * @param doAppend If data should be appended or not
	 */
	private void writeToFile(BuildList builds, boolean doAppend) {
		String lines = "";
		if (doAppend) {
			lines = getFileInfoAndDelete(oldFiles[oldFiles.length - 1]);
		}
		String fileName = builds.getLastBuild().getDate() + ".xml";
		File file = new File(storePath + fileName);
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			if (doAppend) {
				output.write(lines);
			} else {
				output.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
				output.write("\n<Builds>");
			}
			Iterator<BuildInfo> iterator = builds.iterator();
			while(iterator.hasNext()) {
				output.write(iterator.next().getString(1));
			}
			output.write("\n</Builds>");
			output.close();
			if (doAppend) {
				stream.println("Appended data to " + fileName + "from " + oldFiles[oldFiles.length - 1]);
			} else {
				stream.println("Wrote data to " + fileName);
			}
			
		} catch (IOException e) {
			e.printStackTrace(); //Fix This Exception
		}
	}

	/**
	 * Get the content of one file as a string. This is used
	 * when appending data.
	 * @param fileName The file from which the content is gathered
	 * @return The content of the file
	 */
	private String getFileInfoAndDelete(String fileName) {
		File file = new File(storePath + fileName);
		String lines = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				lines += line + "\n";
			}
			reader.close();
			file.delete();
			lines = lines.substring(0, lines.length()-11); //Remove the end tag
		} catch(Exception e){
			System.out.println("Error while reading file line by line:" + e.getMessage()); //TODO: Fix this exception
		}
		return lines;
	}

}