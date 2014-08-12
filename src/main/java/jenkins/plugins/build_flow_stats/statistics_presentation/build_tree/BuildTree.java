package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;

/**
 * Just a glorified ArrayList for storing all build tree elements. The
 * build tree classes were created in order to present the data so that
 * the build flow can be visualized. This could probably be done in jelly,
 * or with more clever concepts in java, but I could not figure it out.
 * The problem I had was that I wanted the tabview with one element per row, 
 * but like I wrote this should be possible with fewer classes.
 * @author Kim Torberntsson
 */
public class BuildTree {

	/**
	 * list of the elements for the tree
	 */
	private ArrayList<BuildTreeElement> elements;

	/**
	 * Base constructor
	 */
	public BuildTree() {
		elements = new ArrayList<BuildTreeElement>();
	}

	/**
	 * Adds a element to the tree.
	 * @param element the element that should get added
	 */
	public void add(BuildTreeElement element) {
		elements.add(element);
	}

	public ArrayList<BuildTreeElement> getElements() {
		return elements;
	}

}