package jenkins.plugins.build_flow_stats;

import java.util.ArrayList;
import java.util.Iterator;

public class BuildList {

	private ArrayList<BuildInfo> builds;

	public BuildList() {
		builds = new ArrayList<BuildInfo>();
	}

	public void addBuildInfo(BuildInfo buildInfo) {
		builds.add(buildInfo);
	}

	public boolean isEmpty() {
		return builds.isEmpty();
	}

	public Iterator<BuildInfo> iterator() {
		return builds.iterator();
	}

	public BuildInfo getLastBuild() {
		return builds.get(builds.size()-1);
	}

	public String getString(int tabLevel) {
		String buildInfo = "";
		Iterator<BuildInfo> builds = iterator();
		while (builds.hasNext()) {
			buildInfo += builds.next().getString(tabLevel);
		}
		return buildInfo;
	}

}