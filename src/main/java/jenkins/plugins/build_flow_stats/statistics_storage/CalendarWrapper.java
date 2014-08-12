package jenkins.plugins.build_flow_stats;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * Wrapper for a GregorianCalendar to make date handling easier
 * @author Kim Torberntsson
 */
public class CalendarWrapper extends GregorianCalendar{

	/**
	 * simple data format object for easier handling. Makes it possible
	 * to get better toString()-like methods.
	 */
	private SimpleDateFormat sdf;

	/**
	 * Creates a object with the current date and time
	 */
	public CalendarWrapper() {
		super();
		sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	}

	/**
	 * Creates a calender wrapper object and then sets the time according 
	 * to the parameter date. The string has to use the format :
	 * "yyyy-MM-dd_HH-mm-ss" in order for the creation to work.
	 * @param  date the date string
	 */
	public CalendarWrapper(String date) {
		this();
		try {
			this.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace(); //TODO:Fix this exception
		}
	}

	/**
	 * Sets the time to zero.
	 */
	public void setTimeToZero() {
		super.set(Calendar.HOUR_OF_DAY, 0);
		super.set(Calendar.MINUTE, 0);
		super.set(Calendar.SECOND, 0);
		super.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * Adds a single day to the date
	 */
	public void add() {
		super.add(Calendar.DAY_OF_MONTH, 1);
	}

	/**
	 * Adds date and time data for the object. See API for GregorianCalendar 
	 * for information about the different fields available.
	 * @param field the type of field
	 * @param amount the amount for the field
	 */
	public void add(int field, int amount) {
		super.add(field, amount);
	}

	/**
	 * Returns a string with the information about the date and time of the
	 * calendar.
	 * @return string with the information
	 */
	public String toString() {
		return sdf.format(super.getTime().getTime());
	}

	/**
	 * Returns a string with the information about the date of the calendar.
	 * Note that no information about the time is returned.
	 * @return string with the information
	 */
	public String getDate() {
		return toString().replaceAll("_\\d{2}-\\d{2}-\\d{2}", "");
	}

}