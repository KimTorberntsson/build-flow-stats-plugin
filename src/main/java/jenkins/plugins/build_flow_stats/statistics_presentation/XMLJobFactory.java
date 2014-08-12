package jenkins.plugins.build_flow_stats;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

/**
 * Factory class for creating the necessary objects for the presentation. 
 * Some processing of the data has to be done before presenting, since
 * the information in the XML-files are stored per build and not per job.
 * The presentation is made on job-level and not build-level, and 
 * therefore these new objects have to be created from the stored data.
 * @author Kim Torberntsson
 */
public class XMLJobFactory {
	
	/**
	 * Create build objects for presentation. Collects data from the 
	 * specified folder from the specified start date up until now.
	 * @param filePath The path from where the data is fetched
	 * @param startDate Defines the start date for the data presentation
	 */
	public static BuildTree[] getPresentationDataFromFile(String filePath, CalendarWrapper startDate) {
		
		JobList allJobs = new JobList();
		FailureCauseList allFailureCauses = new FailureCauseList();

		File jobFolder = new File(filePath);
		String[] xmlFiles = jobFolder.list();

		for (int i = 0; i < xmlFiles.length; i++) {
			CalendarWrapper fileDate = new CalendarWrapper(xmlFiles[i].replace(".xml", ""));
			if (startDate.toString().compareTo(fileDate.toString()) <= 0) {
				try {
					File xmlFile = new File(filePath + "/" + xmlFiles[i]);
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(xmlFile);

					doc.getDocumentElement().normalize();

					Element rootElement = doc.getDocumentElement();

					addBuildsFromNode(rootElement, allJobs, allFailureCauses);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		BuildTree[] presentationData = { allJobs.createBuildTree(), allFailureCauses.createBuildTree(10) };
		return presentationData; 
	}

	/**
	 * Add build from XML Node object. This is the method that is used everytime
	 * the information of a build stored in an XML file should be added to the job
	 * objects.
	 */
	public static void addBuildsFromNode(Node node, JobList jobs, FailureCauseList allFailureCauses) {
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i ++) {
			Node rootElementChild = childNodes.item(i);
			if (rootElementChild.getNodeName().equals("FlowBuild")) {
				Element flowBuildElement = (Element) rootElementChild;
				String jobName = flowBuildElement.getElementsByTagName("JobName").item(0).getTextContent();
				jobs.addFlowJob(jobName);
				jobs.getJob(jobName).addBuildFromXML(rootElementChild, allFailureCauses);
			} else if (rootElementChild.getNodeName().equals("Build")) {
				Element regularBuildElement = (Element) rootElementChild;
				String jobName = regularBuildElement.getElementsByTagName("JobName").item(0).getTextContent();
				jobs.addRegularJob(jobName);
				jobs.getJob(jobName).addBuildFromXML(rootElementChild, allFailureCauses);
			}
		}
	}
	
}