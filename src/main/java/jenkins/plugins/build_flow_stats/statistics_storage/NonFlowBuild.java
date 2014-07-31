package jenkins.plugins.build_flow_stats;

import hudson.model.Build;

public class NonFlowBuild extends BuildInfo {

	protected String failureCause;

	public NonFlowBuild(Build build) {
		super(build);
		failureCause = "";
		if (!result.equals("SUCCESS")) {
			failureCause = getFailureCause(result);
		}
	}

	//The logs should eventually be analysed instead of this mumbojumbo of course
	private String getFailureCause(String result) {
		String failureCause = "";
		if (result.equals("UNSTABLE")) {
			failureCause = "Unstable Build";
		} else if (result.equals("NOT_BUILT")) {
			failureCause = "Not Built";
		} else if (result.equals("ABORTED")) {
			failureCause = "Aborted";
		} else {
			double random = Math.random();
			if (random < 0.25) {
				failureCause = "Temporary fail explanation 1"; 
			} else if (0.25 <= random && random < 0.5) {
				failureCause = "Temporary fail explanation 2"; 
			} else if (0.5 <= random && random < 0.75) {
				failureCause = "Temporary fail explanation 3"; 
			} else {
				failureCause = "Temporary fail explanation 4"; 
			}
		}
		return failureCause;
	}

	public String getString(int tabLevel) {
		String buildInfo = "";
		buildInfo += "\n" + TabLevel.getString(tabLevel) + "<Build>";
		buildInfo += super.getString(tabLevel + 1);
		if (!failureCause.equals("")) {
			buildInfo += "\n" + TabLevel.getString(tabLevel + 1) + "<FailureCause>" + failureCause + "</FailureCause>";
		}
		buildInfo += "\n" + TabLevel.getString(tabLevel) + "</Build>";
		return buildInfo;
	}
}