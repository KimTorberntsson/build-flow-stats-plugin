package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;

/**
 * Just a glorified ArrayList for storing all build tree elements. The
 * build tree classes were created in order to present the data so that
 * the build flow can be visualized. This could probably be done in jelly,
 * but I used this approach instead since I'm not familiar with html or jelly.
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