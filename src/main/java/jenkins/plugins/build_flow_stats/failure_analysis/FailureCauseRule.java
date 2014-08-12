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

public class FailureCauseRule {

	private String name;
	private String description;
	private ArrayList<Pattern> patterns;

	public FailureCauseRule(String name, String description) {
		this.name = name;
		this.description = description;
		patterns = new ArrayList<Pattern>();
	}

	public FailureCauseRule(String name, String description, String[] patterns) {
		this(name, description);
		for (int i = 0; i < patterns.length; i++) {
			addPattern(patterns[i]);
		}
	}

	public FailureCauseRule(Element element) {
		this(element.getElementsByTagName("Name").item(0).getTextContent(), 
			 element.getElementsByTagName("Description").item(0).getTextContent());
		NodeList patternsNodeList = element.getElementsByTagName("Pattern");
		for (int i = 0; i < patternsNodeList.getLength(); i ++) {
			patterns.add(Pattern.compile(patternsNodeList.item(i).getTextContent()));
		}
	}

	public void addPattern(String pattern) {
		patterns.add(Pattern.compile(pattern));
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

	public boolean matches(AbstractBuild build) {
		try {
			BufferedReader reader = new BufferedReader(build.getLogReader());
			String line;
			while ((line = reader.readLine()) != null) {
				Iterator<Pattern> iterator = patterns.iterator();
				while(iterator.hasNext()) {
					if (iterator.next().matcher(line).matches()) {
						return true;
					}
				}
			}
			reader.close();
			return false;
		} catch (IOException e) {
			throw new RuntimeException("Could not analyse log for " + build); //TODO:Fix This Exception
		}
	}

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