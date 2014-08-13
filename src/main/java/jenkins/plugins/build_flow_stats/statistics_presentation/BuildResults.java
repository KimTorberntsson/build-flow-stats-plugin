package jenkins.plugins.build_flow_stats;

import java.text.DecimalFormat;

/**
 * Contains information about build results. It has methods for 
 * increasing each result type and a toString method that 
 * is used for the presentation.
 * @author Kim Torberntsson
 */
public class BuildResults {

	/**
	 * number of successful builds
	 */
	private int successes;

	/**
	 * number of failured builds
	 */
	private int failures;

	/**
	 * number of aborted builds
	 */
	private int aborts;

	/**
	 * number of unstable builds
	 */
	private int unstables;

	/**
	 * number of nobuilds
	 */
	private int nobuilds;
	
	/**
	 * the total number of builds
	 */
	private int totalbuilds;

	/**
	 * Constructor for creating an build result object
	 * without any data
	 */
	public BuildResults() {
		successes = 0;
		unstables = 0;
		failures = 0;
		nobuilds = 0;
		aborts = 0;
		totalbuilds = 0;
	}

	/**
	 * Adds results for the job from a result string originated from a build.
	 * @param resultString string with the result info
	 */
	public void addResultForBuild(String resultString) {
		if (resultString.equals("SUCCESS")) {
			successes += 1;
			totalbuilds += 1;
		} else if (resultString.equals("UNSTABLE")) {
			unstables += 1;
			totalbuilds += 1;
		} else if (resultString.equals("FAILURE")) {
			failures += 1;
			totalbuilds += 1;
		} else if (resultString.equals("NOT_BUILT")) {
			nobuilds += 1;
			totalbuilds += 1;
		} else if (resultString.equals("ABORTED")) {
			aborts += 1;
			totalbuilds += 1;
		}
	}

	/**
	 * Calculates the failure rate for the build results.
	 * @return the failed builds percentage
	 */
	public double getFailureRate() {
		if (totalbuilds == 0) {
			return 0;
		} else {
			return Double.valueOf(new DecimalFormat("#.##").format((double)failures/totalbuilds*100)); //For max 2 decimals
		}	
	}

	/**
	 * Creates a string with the data
	 * @return the data string
	 */
	public String toString() {
		return "[Total Builds: " + totalbuilds + ", Successes: " + successes 
		+ ", Failures: " + failures + ", Aborts: "+ aborts 
		+ ", Unstables: " + unstables + ", Not Built: " + nobuilds 
		+ ", Failure Rate: " + getFailureRate() + "%]";
	}
}