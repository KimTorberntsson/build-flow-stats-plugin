package jenkins.plugins.build_flow_stats;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class XMLJobFactory {
	
	public static JobList getAllJobsFromFile(String fileName) {
		
		JobList allJobs = new JobList();

		try {
			File xmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);

			doc.getDocumentElement().normalize();

			Element rootElement = doc.getDocumentElement();

			addBuildsFromNode(rootElement, allJobs);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return allJobs;
	}

	public static void addBuildsFromNode(Node node, JobList jobs) {
		NodeList childNodes = node.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i ++) {
			Node rootElementChild = childNodes.item(i);
			if (rootElementChild.getNodeName().equals("FlowBuild")) {
				Element flowBuildElement = (Element) rootElementChild;
				String jobName = flowBuildElement.getElementsByTagName("JobName").item(0).getTextContent();
				jobs.addFlowJob(jobName);
				jobs.getJob(jobName).addBuildFromXML(rootElementChild);
			} else if (rootElementChild.getNodeName().equals("Build")) {
				Element nonFlowBuildElement = (Element) rootElementChild;
				String jobName = nonFlowBuildElement.getElementsByTagName("JobName").item(0).getTextContent();
				jobs.addNonFlowJob(jobName);
				jobs.getJob(jobName).addBuildFromXML(rootElementChild);
			}
		}
	}

	public static String createTabLevelString(int i) {
		String tabString = "";
		while (i > 0) {
			tabString += "\t";
			i -= 1;
		}
		return tabString;
	}
}