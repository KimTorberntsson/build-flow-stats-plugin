package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import hudson.model.AbstractBuild;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Pattern;
import java.lang.RuntimeException;

/**
 * Contains information about a failure cause, in this case the name, 
 * description and patterns that should give a match.
 * @author Kim Torberntsson
 */
public class FailureCauseRule {

	/**
	 * the name of the failure cause
	 */
	private String name;

	/**
	 * the description for the failure cause
	 */
	private String description;

	/**
	 * a list of all the patterns associated with the failure cause
	 */
	private ArrayList<Pattern> patterns;

	/**
	 * Base constructor that stores only a description and a name
	 * @param  name the name
	 * @param  description the description
	 */
	public FailureCauseRule(String name, String description) {
		this.name = name;
		this.description = description;
		patterns = new ArrayList<Pattern>();
	}

	/**
	 * Constructor that also stores a string of patterns. The patterns are used
	 * to identify the failures for the logs.
	 * @param  name the name
	 * @param  description the description
	 * @param  patterns the patterns
	 */
	public FailureCauseRule(String name, String description, String[] patterns) {
		this(name, description);
		for (int i = 0; i < patterns.length; i++) {
			addPattern(patterns[i]);
		}
	}

	/**
	 * Constructor that creates the failure cause from an Element 
	 * object. This is used when creating objects from XML-file.
	 * @param  element the element from the XML-file
	 */
	public FailureCauseRule(Element element) {
		this(element.getElementsByTagName("Name").item(0).getTextContent(), 
			 element.getElementsByTagName("Description").item(0).getTextContent());
		NodeList patternsNodeList = element.getElementsByTagName("Pattern");
		for (int i = 0; i < patternsNodeList.getLength(); i ++) {
			patterns.add(Pattern.compile(patternsNodeList.item(i).getTextContent()));
		}
	}

	/**
	 * Adds a pattern for the failure cause
	 * @param pattern the pattern to add
	 */
	public void addPattern(String pattern) {
		patterns.add(Pattern.compile(pattern.replace("\\", "\\\\")));
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public ArrayList<Pattern> getPatterns() {
		return patterns;
	}

	/**
	 * Checks if the line matches any of the failure cause patterns
	 * @param  line the line that gets matched
	 * @return returns the result of the matching
	 */
	public boolean matches(String line) {
		Iterator<Pattern> iterator = patterns.iterator();
		while(iterator.hasNext()) {
			if (iterator.next().matcher(line).matches()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a string with information about the failure cause in a
	 * format appropiate for XML-files.
	 * @return the string with the info.
	 */
	public String toString() {
		String ret = "\n\t<FailureCause>";
		ret += "\n\t\t<Name>" + name + "</Name>";
		ret += "\n\t\t<Description>" + description + "</Description>";
		Iterator<Pattern> iterator = patterns.iterator();
		while (iterator.hasNext()) {
			ret += "\n\t\t<Pattern>" + iterator.next() + "</Pattern>"; 
		}
		return ret + "\n\t</FailureCause>";
	}

}