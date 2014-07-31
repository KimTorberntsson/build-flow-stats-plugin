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
	private File buildsPath;

	public StoreData(PrintStream stream, String jobName) {
		this.stream = stream;
		this.jobName = jobName;
		jenkins = Jenkins.getInstance();
		project = (Project) jenkins.getItem(jobName);
		rootDir = jenkins.getRootDir().toString();
		storePath = rootDir + "/userContent/build-flow-stats/" + jobName + "/"; //TODO: Decide path for storage.
		buildsPath = new File(rootDir + "/jobs/" + jobName + "/builds");
	}

	public void storeBuildInfoToXML(CalendarWrapper startDate) {
		CalendarWrapper endDate = new CalendarWrapper();
		stream.println("Collecting and storing data to XML-file for " + jobName);
		stream.println("Collecting data from " + startDate + " to " + endDate);
		new File(storePath).mkdirs();
		while (startDate.compareTo(endDate) <= 0) {
			ArrayList<Integer> buildNumbers = getBuildNumbers(startDate);
			if (buildNumbers.isEmpty()) {
				stream.println("No data for " + startDate);
			} else {
				BuildList builds = getBuilds(buildNumbers);
				writeToFile(builds, startDate);
			}
			startDate.add();
		}
	}

	private ArrayList<Integer> getBuildNumbers(CalendarWrapper date) {
		FilenameFilter filter = new MyFileFilter();
		File[] files = buildsPath.listFiles(filter);
		ArrayList<Integer> buildNumbers = new ArrayList<Integer>();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			try {
				if (file.getCanonicalPath().replaceAll(".*/", "").startsWith(date.toString())) {
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

	private void writeToFile(BuildList builds, CalendarWrapper startDate) {
		String filename = startDate + ".xml";
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

}