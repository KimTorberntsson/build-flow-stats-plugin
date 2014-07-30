package jenkins.plugins.build_flow_stats;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import jenkins.*;
import jenkins.model.*;
import hudson.*;
import hudson.model.*;
import hudson.util.RunList;
import com.cloudbees.plugins.flow.FlowRun;
import com.cloudbees.plugins.flow.JobInvocation;
import java.util.concurrent.ExecutionException;

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
		CalendarWrapper tempEndDate = new CalendarWrapper(startDate.toString());
		tempEndDate.add();
		while (startDate.compareTo(endDate) < 0) {
			storeToXMLFile(startDate, tempEndDate);
			startDate.add();
			tempEndDate.add();
		}
	}

	private void storeToXMLFile(CalendarWrapper startDate, CalendarWrapper endDate) {
		RunList<Build> runList = project.getBuilds().byTimestamp(startDate.getTime().getTime(), endDate.getTime().getTime());
		ListIterator<Build> runIterator = runList.listIterator(runList.size());
		if (runIterator.hasPrevious()) {
			String filename = startDate + ".xml";
	  		File file = new File(storePath + filename);
	  		try {
				BufferedWriter output = new BufferedWriter(new FileWriter(file));
				output.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
				output.write("\n<Builds>");
				while (runIterator.hasPrevious()) {
		  			Build build = runIterator.previous();
		  			if (build.getClass().toString().equals("class com.cloudbees.plugins.flow.FlowRun")) {
		  				FlowRun flowBuild = (FlowRun) build;
		  				output.write(getFlowBuildInfoForXML(flowBuild, 1));
		  			} else {
		  				output.write(getBuildInfoForXML(build, 1));
		  			}
		 		}
			  	output.write("\n</Builds>");
			  	output.close();
			  	stream.println("Wrote data to " + filename);
			} catch (IOException e) {
				e.printStackTrace(); //TODO: Fix this exception
			}
		} else {
			stream.println("No data for " + startDate);
		}
	}

	private String getFlowBuildInfoForXML(FlowRun flowBuild, int tabLevel) {
		String flowBuildInfo = "\n" + createTabLevelString(tabLevel) + "<FlowBuild>";
		flowBuildInfo += "\n" + createTabLevelString(tabLevel + 1) + "<JobName>" + flowBuild.getParent().getFullName() + "</JobName>";
		flowBuildInfo += "\n" + createTabLevelString(tabLevel + 1) + "<BuildNumber>" + flowBuild.getNumber() + "</BuildNumber>";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		flowBuildInfo += "\n" + createTabLevelString(tabLevel + 1) + "<Date>" + sdf.format(flowBuild.getTime()) + "</Date>";
		flowBuildInfo += "\n" + createTabLevelString(tabLevel + 1) + "<Result>" + flowBuild.getResult() + "</Result>";
		Iterator<JobInvocation> subBuilds = flowBuild.getJobsGraph().vertexSet().iterator();
		while (subBuilds.hasNext()) {
			try {
				Build subBuild = (Build) subBuilds.next().getBuild();
				if (subBuild != null && !subBuild.getParent().getFullName().equals(flowBuild.getParent().getFullName())) {
					flowBuildInfo += getBuildInfoForXML(subBuild, tabLevel + 1);
				}
			} catch (ExecutionException ee) {
				//TODO: Fix the exception handling
			} catch (InterruptedException ie) {
				//TODO: Fix the exception handling
			} 
		}
		flowBuildInfo += "\n" + createTabLevelString(tabLevel) + "</FlowBuild>";
		return flowBuildInfo;
	}

	private String getBuildInfoForXML(Build build, int tabLevel) {
		String buildInfo = "\n" + createTabLevelString(tabLevel) + "<Build>";
		buildInfo += "\n" + createTabLevelString(tabLevel + 1) + "<JobName>" + build.getParent().getFullName() + "</JobName>";
		buildInfo += "\n" + createTabLevelString(tabLevel + 1) + "<BuildNumber>" + build.getNumber() + "</BuildNumber>";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
		buildInfo += "\n" + createTabLevelString(tabLevel + 1) + "<Date>" + sdf.format(build.getTime()) + "</Date>";
		buildInfo += "\n" + createTabLevelString(tabLevel + 1) + "<Result>" + build.getResult() + "</Result>";
		if (!build.getResult().toString().equals("SUCCESS")) {
			buildInfo += getFailureCauseForXML(build.getResult().toString(), tabLevel);
		}
		buildInfo += "\n" + createTabLevelString(tabLevel) + "</Build>";
		return buildInfo;
	}

	//TODO: Make this much neater. Of course it will have to be rewritten anyway because of the machiune learning part.
	public String getFailureCauseForXML(String result, int tabLevel) {
	  	//The logs should eventually be analysed instead of this mumbojumbo of course
		String failureCause = "";
	    if (result.equals("UNSTABLE")) {
	    	failureCause = "Unstable Build";
	    } else if (result.equals("NOT_BUILT")) {
	   		failureCause = "Not Built";
	    } else if (result.equals("ABORTED")) {
	    	failureCause = "Aborted";
	    } else {
	    	double random = Math.random();
	    	if (random < 0.25) {
	    		failureCause = "Temporary fail explanation 1"; 
	    	} else if (0.25 <= random && random < 0.5) {
	    		failureCause = "Temporary fail explanation 2"; 
	      	} else if (0.5 <= random && random < 0.75) {
	        	failureCause = "Temporary fail explanation 3"; 
	      	} else {
	        	failureCause = "Temporary fail explanation 4"; 
	    	}
	    }
		return "\n" + createTabLevelString(tabLevel) + "<FailureCause>" + failureCause + "</FailureCause>";
	}

	private String createTabLevelString(int i) {
		String tabString = "";
		while (i > 0) {
			tabString += "\t";
			i -= 1;
		}
		return tabString;
	}

}