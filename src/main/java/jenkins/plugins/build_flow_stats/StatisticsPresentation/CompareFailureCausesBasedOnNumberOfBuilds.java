package jenkins.plugins.build_flow_stats;

import java.util.Comparator;

/**
 * Comparator implementation which sorts FailureCause objects based on number of builds.
 * The FailureCause object with most builds should come first.
 */
public class CompareFailureCausesBasedOnNumberOfBuilds implements Comparator<FailureCause> {

    public int compare(FailureCause f1, FailureCause f2) {
        return f2.getNumberOfBuilds() - f1.getNumberOfBuilds();
    }

}