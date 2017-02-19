package fr.piotr.economies.other;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LastMonth {

	private final String label;
	private final String year;

	public LastMonth(){
		Calendar calInstance = Calendar.getInstance();
		calInstance.add(Calendar.MONTH, -1);
		Date time = calInstance.getTime();
		label=new SimpleDateFormat("MMMMMM", Locale.FRENCH).format(time);
		year=new SimpleDateFormat("yyyy",Locale.FRENCH).format(time);
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return label+" "+year;
	}


}
