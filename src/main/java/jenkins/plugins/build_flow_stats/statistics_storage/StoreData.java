package jenkins.plugins.build_flow_stats;

import java.util.Calendar;
import java.util.GregorianCalendar;

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

public class StoreData {

	public static void storeBuildInfoToXML(PrintStream stream, String jobName) {

		stream.println("Collect and store data to XML-file from" + jobName);

		// Create time object that points to the 00:00 of today
		Calendar startDate = new GregorianCalendar();
		startDate.add(Calendar.DAY_OF_MONTH, -12);
		Calendar endDate = new GregorianCalendar();

		//Create path for storing data
		Jenkins jenkins = Jenkins.getInstance();
		String rootDir = jenkins.getRootDir().toString();
		String storePath = rootDir + "/userContent/build-flow-stats/";

		//TODO: This should be made in a more general way with different names for different dates being generated automatically
		String filename = "Builds-Plugin.xml";

		//Create directories if they does not exist
		//TODO:CHeck if this really is a good way to create the directories if they do not exist
		new File(storePath).mkdirs();

		try {
 			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

  			//Create root element
			Document doc = docBuilder.newDocument();
			Element builds = doc.createElement("Builds");
			doc.appendChild(builds);

			//Adds the data as xml recursively
			/*
			project.getBuilds().byTimestamp(startDate.getTime(),endDate.getTime()).each {
		  		writeBuildToXML(doc, builds, it);
			}
			*/

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
}