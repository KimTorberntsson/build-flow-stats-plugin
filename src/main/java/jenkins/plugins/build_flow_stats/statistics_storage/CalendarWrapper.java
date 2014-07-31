package jenkins.plugins.build_flow_stats;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class CalendarWrapper extends GregorianCalendar{

	private SimpleDateFormat sdf;

	public CalendarWrapper() {
		super();
		super.set(Calendar.HOUR_OF_DAY, 0);
		super.set(Calendar.MINUTE, 0);
		super.set(Calendar.SECOND, 0);
		super.set(Calendar.MILLISECOND, 0);
		sdf = new SimpleDateFormat("yyyy-MM-dd");
	}

	public CalendarWrapper(String date) {
		this();
		try {
            this.setTime(sdf.parse(date));
		} catch (ParseException e) {
			throw new RuntimeException("Could not parse start date");
		}
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

}