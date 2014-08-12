package jenkins.plugins.build_flow_stats;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import hudson.model.AbstractBuild;

/**
 * Analyses builds for failures. Every build can only have one failure 
 * (at least with this current implementation). 
 * @author Kim Torberntsson
 */
public class FailureAnalyser {

	/**
	 * The failure cause rules for the analyser.
	 */
	private ArrayList<FailureCauseRule> failureCauseRules;

	/**
	 * Base constructor
	 */
	public FailureAnalyser() {
		failureCauseRules = new ArrayList<FailureCauseRule>();
	}

	/**
	 * Constructor that creates the object from an XML-file.
	 * @param  xmlFile the XML File
	 */
	public FailureAnalyser(File xmlFile) {
		this();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			Element rootElement = doc.getDocumentElement();
			NodeList failureCauseRuleNodeList = rootElement.getElementsByTagName("FailureCause");
			for (int i = 0; i < failureCauseRuleNodeList.getLength(); i++) {
				failureCauseRules.add(new FailureCauseRule((Element) failureCauseRuleNodeList.item(i)));
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not instantiate failure analyser from XML-file " + xmlFile); //TODO: Fix this exception
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Could not instantiate failure analyser from XML-file " + xmlFile); //TODO: Fix this exception
		} catch (SAXException e) {
			throw new RuntimeException("Could not instantiate failure analyser from XML-file " + xmlFile); //TODO: Fix this exception
		}
	}

	/**
	 * Adds a failure cause rule to the analyser.
	 * @param failureCauseRule the failure cause rule to add.
	 */
	public void addFailureCauseRule(FailureCauseRule failureCauseRule) {
		failureCauseRules.add(failureCauseRule);
	}

	public ArrayList<FailureCauseRule> getFailureCauseRules() {
		return failureCauseRules;
	}

	/**
	 * Checks a build for failure cause matches. If no match is found a special
	 * failure cause that explains that no match was found is returned. The 
	 * function goes through the build log one line at a time and checks for 
	 * matches. The first match that is found is considered to be the failure 
	 * cause for that build. This means that there can only be one failure 
	 * cause for each build.
	 * @param  build the build that should be analysed
	 * @return the matching failure cause
	 */
	public FailureCauseRule matches(AbstractBuild build) {
		try {
			BufferedReader reader = new BufferedReader(build.getLogReader());
			String line;
			while ((line = reader.readLine()) != null) {
				Iterator<FailureCauseRule> iterator = failureCauseRules.iterator();
				while (iterator.hasNext()) {
					FailureCauseRule rule = iterator.next();
					if (rule.matches(line)) {
						return rule;
					}
				}
			}
			return new FailureCauseRule("Unknown failure cause", "Add failure cause rules to match build failure to failure cause");
		} catch (IOException e) {
			throw new RuntimeException("Could not analyse log for " + build);
		}
	}

	/**
	 * For storing the failure cause rules to XML-file so that the data 
	 * can be read later.
	 * @param fileName the filename for the file that the data should be stored to.
	 */
	public void writeToXML(String fileName) {
		File file = new File(fileName);
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			output.write("\n<FailureCauses>");
			Iterator<FailureCauseRule> iterator = failureCauseRules.iterator();
			while(iterator.hasNext()) {
				output.write(iterator.next().toString()); 
			}
			output.write("\n</FailureCauses>");
			output.close();
		} catch (IOException e) {
			e.printStackTrace(); //Fix This Exception
		}
	}

}