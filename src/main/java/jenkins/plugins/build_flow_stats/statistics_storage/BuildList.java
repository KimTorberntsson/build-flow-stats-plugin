package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * List that contain build objects. This class is used for flowbuilds to 
 * be able to store their subbuilds and also by the store data class when
 * storing a group of builds to a file.
 * @author Kim Torberntsson
 */
public class BuildList {

	/**
	 * list of builds
	 */
	private ArrayList<BuildInfo> builds;

	/**
	 * Base constructor for creating an empty list.
	 */
	public BuildList() {
		builds = new ArrayList<BuildInfo>();
	}

	/**
	 * Adds a build info object to the list.
	 * @param buildInfo
	 */
	public void addBuildInfo(BuildInfo buildInfo) {
		builds.add(buildInfo);
	}

	/**
	 * Returns whether the list is empty or not
	 * @return true if the list is empty, false otherwise
	 */
	public boolean isEmpty() {
		return builds.isEmpty();
	}

	/**
	 * Returns an iterator object with the builds
	 * @return iterator with the builds
	 */
	public Iterator<BuildInfo> iterator() {
		return builds.iterator();
	}

	/**
	 * Returns the buld object at the end of the list
	 * @return the last build object
	 */
	public BuildInfo getLastBuild() {
		return builds.get(builds.size()-1);
	}

	/**
	 * Returns a string with the information that is needed for storage to XML-file.
	 * @param  tabLevel the tab level that should be used
	 * @return string with the information for XML-storage
	 */
	public String getString(int tabLevel) {
		String buildInfo = "";
		Iterator<BuildInfo> builds = iterator();
		while (builds.hasNext()) {
			buildInfo += builds.next().getString(tabLevel);
		}
		return buildInfo;
	}

}