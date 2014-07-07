package jenkins.plugins.build_flow_stats;

import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;
import org.kohsuke.stapler.StaplerRequest;

/**
* Dumb object for passing around input Options
* @author Kim Torberntsson
*
*/
@ExportedBean
public class InputOptions {
	private int range;
	private String rangeUnits;
	private String jobName;

	public InputOptions(StaplerRequest req) {
		range = Integer.parseInt(req.getParameter("range"));
		rangeUnits = req.getParameter("rangeUnits");
		jobName = req.getParameter("jobName");
	}

	@Exported
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	
	@Exported
	public String getRangeUnits() {
		return rangeUnits;
	}
	public void setRangeUnits(String rangeUnits) {
		this.rangeUnits = rangeUnits;
	}

	@Exported
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

}
