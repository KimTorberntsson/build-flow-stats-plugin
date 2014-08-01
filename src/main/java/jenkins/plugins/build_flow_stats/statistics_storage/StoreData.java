package jenkins.plugins.build_flow_stats;

import java.io.*;
import java.util.*;
import java.util.Iterator;
import java.util.ListIterator;
import hudson.model.Build;
import hudson.model.Project;
import jenkins.model.Jenkins;
import hudson.util.RunList;

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

	public StoreData(PrintStream stream, String jobName) {
		this.stream = stream;
		this.jobName = jobName;
		stream.println("Collecting and storing data to XML-file for " + jobName);
		jenkins = Jenkins.getInstance();
		project = (Project) jenkins.getItem(jobName);
		rootDir = jenkins.getRootDir().toString();
		storePath = rootDir + "/userContent/build-flow-stats/" + jobName + "/"; //TODO: Decide path for storage.
		storePathFile = new File(storePath);
		storePathFile.mkdirs();
		oldFiles = storePathFile.list();
		buildsPath = new File(rootDir + "/jobs/" + jobName + "/builds");
	}

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

	private void appendToLatestFile(CalendarWrapper startDate) {
		CalendarWrapper endDate = new CalendarWrapper(startDate.toString());
		endDate.setTimeToZero();
		endDate.add();
		storeBuildInfoToXML(startDate, endDate, true);
	}

	private void storeBuildInfoToXML(CalendarWrapper startDate, CalendarWrapper endDate, boolean append) {
		ArrayList<Integer> buildNumbers = getBuildNumbers(startDate, endDate);
		if (buildNumbers.isEmpty()) {
			stream.println("No data for " + startDate.getDate());
		} else {
			BuildList builds = getBuilds(buildNumbers);
			writeToFile(builds, append);
		}
	}

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

	private BuildList getBuilds(ArrayList<Integer> buildNumbers) {
		BuildList builds = new BuildList();
		Iterator<Integer> iterator = buildNumbers.iterator();
		while (iterator.hasNext()) {
			Build build = (Build) project.getBuildByNumber(iterator.next());
			BuildInfo buildInfo;
			if (build.getClass().toString().equals("class com.cloudbees.plugins.flow.FlowRun")) {
				buildInfo = new FlowBuild(build);
			} else {
				buildInfo = new NonFlowBuild(build);
			}
			builds.addBuildInfo(buildInfo);
		}
		return builds;
	}

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