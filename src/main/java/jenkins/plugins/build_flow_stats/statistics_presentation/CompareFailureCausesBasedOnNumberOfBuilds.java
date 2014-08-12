package jenkins.plugins.build_flow_stats;

import java.util.Comparator;

/**
 * Comparator implementation which sorts FailureCause objects based on number of builds.
 * The FailureCause object with most builds should come first.
 * @author Kim Torberntsson
 */
public class CompareFailureCausesBasedOnNumberOfBuilds implements Comparator<FailureCause> {

	/**
	 * Compares two failure causes. The one with most number of 
	 * builds is considered "bigger".
	 * @param  the first failure cause
	 * @param  the second failure cause
	 * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
	 */
	public int compare(FailureCause f1, FailureCause f2) {
	return f2.getNumberOfBuilds() - f1.getNumberOfBuilds();
	}

}