package jenkins.plugins.build_flow_stats;

import java.io.*;
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
	private CalendarWrapper endDate;
	private Jenkins jenkins;
	private String storePath;
	private Project project;

	public StoreData(PrintStream stream, String jobName, CalendarWrapper startDate) {
		this.stream = stream;
		this.jobName = jobName;
		this.startDate = startDate;
		endDate = new CalendarWrapper();
		jenkins = Jenkins.getInstance();
		project = (Project) jenkins.getItem(jobName);
		storePath = getStorePath();
	}

	private String getStorePath() {
		String rootDir = jenkins.getRootDir().toString();
		return storePath = rootDir + "/userContent/build-flow-stats/" + jobName + "/"; //TODO: Decide path for storage.
	}

	public void storeBuildInfoToXML() {
		stream.println("Collecting and storing data to XML-file for " + jobName);
		stream.println("Collecting data from " + startDate + " to " + endDate);
		new File(storePath).mkdirs();
		CalendarWrapper tempStartDate = new CalendarWrapper(startDate.toString());
		CalendarWrapper tempEndDate = new CalendarWrapper(startDate.toString());
		tempEndDate.add();
		while (tempStartDate.compareTo(endDate) < 0) {
			BuildList builds = createList(tempStartDate, tempEndDate);
			writeToFile(builds, tempStartDate);
			tempStartDate.add();
			tempEndDate.add();
		}
	}

	private BuildList createList(CalendarWrapper startDate, CalendarWrapper endDate) {
		BuildList builds = new BuildList(); 
		RunList<Build> runList = project.getBuilds().byTimestamp(startDate.getTime().getTime(), endDate.getTime().getTime());
		ListIterator<Build> runIterator = runList.listIterator(runList.size());
		while (runIterator.hasPrevious()) {
			Build build = runIterator.previous();
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
		if (builds.isEmpty()) {
			stream.println("No data for " + startDate);
		} else {
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

}