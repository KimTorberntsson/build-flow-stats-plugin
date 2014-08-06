package jenkins.plugins.build_flow_stats;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Wrapper for a GregorianCalendar to make date handling easier
 */
public class CalendarWrapper extends GregorianCalendar{

	private SimpleDateFormat sdf;

	public CalendarWrapper() {
		super();
		sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	}

	public CalendarWrapper(String date) {
		this();
		try {
			this.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace(); //TODO:Fix this exception
		}
	}

	public void setTimeToZero() {
		super.set(Calendar.HOUR_OF_DAY, 0);
		super.set(Calendar.MINUTE, 0);
		super.set(Calendar.SECOND, 0);
		super.set(Calendar.MILLISECOND, 0);
	}

	public void add() {
		super.add(Calendar.DAY_OF_MONTH, 1);
	}

	public void add(int field, int amount) {
		super.add(field, amount);
	}

	public String toString() {
		return sdf.format(super.getTime().getTime());
	}

	public String getDate() {
		return toString().replaceAll("_\\d{2}-\\d{2}-\\d{2}", "");
	}

}