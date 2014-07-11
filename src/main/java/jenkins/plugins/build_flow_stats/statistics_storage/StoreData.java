package jenkins.plugins.build_flow_stats;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import jenkins.*;
import jenkins.model.*;
import hudson.*;
import hudson.model.*;

import java.io.File;
import java.io.PrintStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.cloudbees.plugins.flow.FlowRun;
import com.cloudbees.plugins.flow.JobInvocation;
import java.util.concurrent.ExecutionException;

public class StoreData {

	public static void storeBuildInfoToXML(PrintStream stream, String jobName) {

		stream.println("Collect and store data to XML-file from " + jobName);

		// TODO: This should also be configurable by the user
		Calendar startDate = new GregorianCalendar();
		startDate.add(Calendar.DAY_OF_MONTH, -10);
		Calendar endDate = new GregorianCalendar();

		//Create path for storing data
		Jenkins jenkins = Jenkins.getInstance();
		String rootDir = jenkins.getRootDir().toString();
		String storePath = rootDir + "/userContent/build-flow-stats/";

		//TODO: This should be made in a more general way with different names for different dates being generated automatically
		String filename = jobName + ".xml";

		//Create directories if they does not exist
		//TODO:CHeck if this really is a good way to create the directories if they do not exist
		new File(storePath).mkdirs();

		// Get project object 
		Project project = (Project) jenkins.getItem(jobName);;

		try {
 			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

  			//Create root element
			Document doc = docBuilder.newDocument();
			Element builds = doc.createElement("Builds");
			doc.appendChild(builds);

			//Recursively add all builds from the job
			Iterator<Build> runIterator = project.getBuilds().byTimestamp(startDate.getTime().getTime(),endDate.getTime().getTime()).iterator();
		  	while (runIterator.hasNext()) {
		  		writeBuildToXML(doc, builds, runIterator.next());
		  	}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(storePath + filename));    
			transformer.transform(source, result);
			   
			stream.println("File saved!");

		} catch (ParserConfigurationException pce) {
  			pce.printStackTrace(stream);
  		} catch (TransformerException tfe) {
  			tfe.printStackTrace(stream);
  		}
	}

	public static void writeBuildToXML(Document doc, Element parentElement, Build build) {
		if (build.getClass().toString().equals("class com.cloudbees.plugins.flow.FlowRun")) {
			FlowRun flowBuild = (FlowRun) build;
			Element flowBuildElement = doc.createElement("FlowBuild");
	    	parentElement.appendChild(flowBuildElement);
			writeFlowBuildInfoToXML(doc, flowBuildElement, flowBuild);
		} else {
	    	Element buildElement = doc.createElement("Build");
	    	parentElement.appendChild(buildElement);
	    	writeBuildInfoToXML(doc, buildElement, build);
		}
	}

	public static void writeFlowBuildInfoToXML(Document doc, Element flowBuildElement, FlowRun flowBuild) {
		addJobNameToXML(doc, flowBuildElement, flowBuild);
		addBuildNumberToXML(doc, flowBuildElement, flowBuild);
		addDateToXML(doc, flowBuildElement, flowBuild);
		addResultToXML(doc, flowBuildElement, flowBuild);

		Iterator<JobInvocation> subBuilds = flowBuild.getJobsGraph().vertexSet().iterator();
		while (subBuilds.hasNext()) {
			try {
				Build subBuild = (Build) subBuilds.next().getBuild();
				if (subBuild != null && !subBuild.getParent().getFullName().equals(flowBuild.getParent().getFullName())) {
					writeBuildToXML(doc, flowBuildElement, subBuild);
				}
			} catch (ExecutionException ee) {} //Fix the exception handling
			catch (InterruptedException ie) {} //Fix the exception handling
		}
	}

	public static void writeBuildInfoToXML(Document doc, Element buildElement, Build build) {
	  addJobNameToXML(doc, buildElement, build);
	  addBuildNumberToXML(doc, buildElement, build);
	  addDateToXML(doc, buildElement, build);
	  addResultToXML(doc, buildElement, build);
	  addFailureCauseToXML(doc, buildElement, build, build.getResult().toString());
	}

	public static void addJobNameToXML(Document doc, Element parentElement, Build build) {
	  String jobName = build.getParent().getFullName();
	  Element jobNameElement = doc.createElement("JobName");
	  jobNameElement.appendChild(doc.createTextNode(jobName));
	  parentElement.appendChild(jobNameElement);
	}

	public static void addBuildNumberToXML(Document doc, Element parentElement, Build build) {
	  String buildNumber = "" + build.getNumber();
	  Element buildNumberElement = doc.createElement("BuildNumber");
	  buildNumberElement.appendChild(doc.createTextNode(buildNumber));
	  parentElement.appendChild(buildNumberElement);
	}

	public static void addDateToXML(Document doc, Element parentElement, Build build) {
	  String date = build.getTime().toString();
	  Element dateElement = doc.createElement("Date");
	  dateElement.appendChild(doc.createTextNode(date));
	  parentElement.appendChild(dateElement);
	}

	public static void addResultToXML(Document doc, Element parentElement, Build build) {
	  String result = build.getResult().toString();
	  Element resultElement = doc.createElement("Result");
	  resultElement.appendChild(doc.createTextNode(result));
	  parentElement.appendChild(resultElement);
	}

	public static void addFailureCauseToXML(Document doc, Element parentElement, Build build, String result) {
	  //The logs should eventually be analysed instead of this mumbojumbo of course
	  if (!result.equals("SUCCESS")) {
	    String failureCause;
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
	    Element failureCauseElement = doc.createElement("FailureCause");
	    failureCauseElement.appendChild(doc.createTextNode(failureCause));
	    parentElement.appendChild(failureCauseElement);
	  }
	}
}