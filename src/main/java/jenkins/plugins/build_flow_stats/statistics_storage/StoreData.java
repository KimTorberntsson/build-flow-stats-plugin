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
	private CalendarWrapper startDate;
	private Jenkins jenkins;
	private Project project;
	private String rootDir;
	private String storePath;
	private File storePathFile;
	private File buildsPath;

	public StoreData(PrintStream stream, String jobName, CalendarWrapper startDate) {
		this.stream = stream;
		this.jobName = jobName;
		stream.println("Collecting and storing data to XML-file for " + jobName);
		jenkins = Jenkins.getInstance();
		project = (Project) jenkins.getItem(jobName);
		rootDir = jenkins.getRootDir().toString();
		storePath = rootDir + "/userContent/build-flow-stats/" + jobName + "/"; //TODO: Decide path for storage.
		storePathFile = new File(storePath);
		storePathFile.mkdirs();
		this.startDate = getStartDate(startDate);
		buildsPath = new File(rootDir + "/jobs/" + jobName + "/builds");
	}

	public CalendarWrapper getStartDate(CalendarWrapper userStartDate) {
		File storePathFile = new File(storePath);
		CalendarWrapper startDate;
		String[] list = storePathFile.list();
		if (list == null || list.length == 0) {
			startDate = userStartDate;
			stream.println("No previous data found for " + jobName + ". Saving logs from " + startDate.getDate());
		} else {
			String latestStoredFile = list[list.length - 1].replace(".xml", "");
			if (latestStoredFile.compareTo(userStartDate.toString()) > 0) {
				startDate = new CalendarWrapper(latestStoredFile);
				stream.println("Data has already been collected from " + userStartDate.getDate());
				stream.println("Continue storing of logs from " + startDate.getDate());
			} else {
				startDate = userStartDate;
				stream.println("Previous data found for " + jobName + ". Saving logs from " + startDate.getDate());
			}
		}
		return startDate;
	}

	public void storeBuildInfoToXML() {
		CalendarWrapper endDate = new CalendarWrapper();
		CalendarWrapper tempStartDate = new CalendarWrapper(startDate.toString());
		CalendarWrapper tempEndDate = new CalendarWrapper(startDate.toString());
		tempEndDate.add();
		while (tempStartDate.compareTo(endDate) <= 0) {
			ArrayList<Integer> buildNumbers = getBuildNumbers(tempStartDate, tempEndDate);
			if (buildNumbers.isEmpty()) {
				stream.println("No data for " + startDate.getDate());
			} else {
				BuildList builds = getBuilds(buildNumbers);
				writeToFile(builds);
			}
			tempStartDate.add();
			tempEndDate.add();
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

	private void writeToFile(BuildList builds) {
		String filename = builds.getLastBuild().getDate() + ".xml";
		File file = new File(storePath + filename);
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			output.write("\n<Builds>");
			Iterator<BuildInfo> iterator = builds.iterator();
			while(iterator.hasNext()) {
				output.write(iterator.next().getString(1));
			}
			output.write("\n</Builds>");
			output.close();
			stream.println("Wrote data to " + filename);
		} catch (IOException e) {
			e.printStackTrace(); //Fix This Exception
		}
	}

	private void appendToFile(BuildList builds, String oldFileName) {
		String filename = oldFileName + ".xml";
		File oldFile = new File(storePath + filename);
		//Reading from old file
		String lines = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(oldFile));
			String line;
			while ((line = reader.readLine()) != null) {
				lines += line + "\n";
			}
			reader.close();
			oldFile.delete();
			lines = lines.substring(0, lines.length()-11); //Remove the end tag
		} catch(Exception e){
			System.out.println("Error while reading file line by line:" + e.getMessage()); //TODO: Fix this exception
		}
		//Writing to new file
		File newFile = new File(storePath + builds.getLastBuild().getDate());
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(newFile));
			output.write(lines); //Write info from the old file
			Iterator<BuildInfo> iterator = builds.iterator();
			while(iterator.hasNext()) {
				output.write(iterator.next().getString(1));
			}
			output.write("\n</Builds>");
			output.close();
			stream.println("Appended data to " + oldFileName);
		} catch (IOException e) {
			e.printStackTrace(); //TODO: Fix This Exception
		}
	}

}