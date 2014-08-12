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

public class FailureAnalyser {

	private ArrayList<FailureCauseRule> failureCauseRules;

	public FailureAnalyser() {
		failureCauseRules = new ArrayList<FailureCauseRule>();
	}

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

	public void addFailureCauseRule(FailureCauseRule failureCauseRule) {
		failureCauseRules.add(failureCauseRule);
	}

	public ArrayList<FailureCauseRule> getFailureCauseRules() {
		return failureCauseRules;
	}

	public FailureCauseRule matches(AbstractBuild build) {
		Iterator<FailureCauseRule> iterator = failureCauseRules.iterator();
		while (iterator.hasNext()) {
			FailureCauseRule rule = iterator.next();
			if (rule.matches(build)) {
				return rule;
			}
		}
		return new FailureCauseRule("Unknown failure cause", "Add failure cause rules to match build failure to failure cause");
	}

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