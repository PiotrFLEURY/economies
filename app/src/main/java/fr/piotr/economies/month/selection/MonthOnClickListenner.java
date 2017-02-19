package fr.piotr.economies.month.selection;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import fr.piotr.economies.MonthSelectionActivity;
import fr.piotr.economies.managers.ProfileManager;
import fr.piotr.economies.persistance.serializable.Month;

public class MonthOnClickListenner implements OnClickListener {

	private Month month;

	public MonthOnClickListenner(final Month aMonth){
		month=aMonth;
	}

	public void onClick(View v) {
		ProfileManager.getInstance().linkonMonth(month);
		LocalBroadcastManager.getInstance(v.getContext()).sendBroadcast(new Intent(MonthSelectionActivity.EVENT_GOTO_LINKED_MONTH));
	}

}
