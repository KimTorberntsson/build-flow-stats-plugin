package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;

/**
 * Just a glorified ArrayList for storing all build tree elements.
 */
public class BuildTree {

	private ArrayList<BuildTreeElement> elements;

	public BuildTree() {
		elements = new ArrayList<BuildTreeElement>();
	}

	public void add(BuildTreeElement element) {
		elements.add(element);
	}

	public ArrayList<BuildTreeElement> getElements() {
		return elements;
	}

}