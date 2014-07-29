package jenkins.plugins.build_flow_stats;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class CalendarWrapper {
	private Calendar calendar;
	private SimpleDateFormat sdf;

	public CalendarWrapper() {
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		this.calendar = new GregorianCalendar();
	}

	public CalendarWrapper(String date) {
		this();
		try {
            this.calendar.setTime(sdf.parse(date));
		} catch (ParseException e) {
			throw new RuntimeException("Could not parse start date");
		}
	}

	public void add() {
		calendar.add(Calendar.DAY_OF_MONTH, 1);
	}

	public String toString() {
		return sdf.format(calendar.getTime());
	}

	public long getTime() {
		return calendar.getTime().getTime();
	}

	public int compareTo(CalendarWrapper otherCalendar) {
		return calendar.compareTo(otherCalendar.calendar);
	}
}