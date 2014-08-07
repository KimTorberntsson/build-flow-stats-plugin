package jenkins.plugins.build_flow_stats;

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
import java.io.File;
import java.io.IOException;
import hudson.model.AbstractBuild;
public class FailureAnalyser {

	private ArrayList<FailureClassificationRule> rules;

	public FailureAnalyser(String fileName) {
		rules = new ArrayList<FailureClassificationRule>();
		try {
			File xmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			Element rootElement = doc.getDocumentElement();
			NodeList rulesNodeList = rootElement.getElementsByTagName("FailureCause");
			for (int i = 0; i < rulesNodeList.getLength(); i++) {
				rules.add(new FailureClassificationRule((Element) rulesNodeList.item(i)));
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not instantiate failure analyser from XML-file " + fileName); //TODO: Fix this exception
		} catch (ParserConfigurationException e) {
			throw new RuntimeException("Could not instantiate failure analyser from XML-file " + fileName); //TODO: Fix this exception
		} catch (SAXException e) {
			throw new RuntimeException("Could not instantiate failure analyser from XML-file " + fileName); //TODO: Fix this exception
		}
	}

	public String toString() {
		String ret = "";
		Iterator<FailureClassificationRule> iterator = rules.iterator();
		while(iterator.hasNext()) {
			ret += iterator.next() + "\n";
		}
		return ret;
	}

	public FailureClassificationRule analyseBuildForFailures(AbstractBuild build) {
		Iterator<FailureClassificationRule> iterator = rules.iterator();
		while (iterator.hasNext()) {
			FailureClassificationRule rule = iterator.next();
			if (rule.matches(build)) {
				return rule;
			}
		}
		return new FailureClassificationRule("Unknown failure cause",
											 "Add failure rules to match build failure to failure cause");
	}
}