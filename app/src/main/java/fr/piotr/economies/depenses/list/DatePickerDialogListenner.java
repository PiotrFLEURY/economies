package fr.piotr.economies.depenses.list;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerDialogListenner implements OnDateSetListener {

	private final Calendar	calendar	= Calendar.getInstance();

	private TextView		textToSet;

	public DatePickerDialogListenner(final TextView target) {
		textToSet = target;
	}

	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, monthOfYear);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		textToSet.setText(getDate("dd/MM/yyyy"));
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public String getDate(final String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(calendar.getTime());
	}

}
