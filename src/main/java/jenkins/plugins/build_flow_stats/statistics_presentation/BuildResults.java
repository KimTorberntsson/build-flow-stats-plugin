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
		failures = 0;
		aborts = 0;
		unstables = 0;
		nobuilds = 0;
		totalbuilds = 0;
	}

	public void addSuccess() {
		successes ++;
		totalbuilds ++;
	}

	public void addFailure() {
		failures ++;
		totalbuilds ++;
	}

	public void addAbort() {
		aborts ++;
		totalbuilds ++;
	}

	public void addUnstable() {
		unstables ++;
		totalbuilds ++;
	}

	public void addNoBuild() {
		nobuilds ++;
		totalbuilds ++;
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