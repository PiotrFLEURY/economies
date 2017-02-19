package fr.piotr.economies.month.selection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import fr.piotr.economies.MonthSelectionActivity;
import fr.piotr.economies.R;
import fr.piotr.economies.managers.ProfileManager;
import fr.piotr.economies.persistance.PersistanceHelper;
import fr.piotr.economies.persistance.serializable.Month;

public class MonthOnLongClickListenner implements OnLongClickListener {

	protected Month		month;

	public MonthOnLongClickListenner(final Month m) {
		month = m;
	}

	public boolean onLongClick(View v) {
		Context context = v.getContext();
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setMessage(context.getText(R.string.deletemonth));
		dialog.setButton(context.getText(R.string.ok), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog1, int which) {

				Month linkedMonth = ProfileManager.getInstance().getLinkedMonth();
				boolean linkedOnDeletingOne = linkedMonth != null
						&& linkedMonth.getId().intValue() == month.getId();

				PersistanceHelper.getInstance(context).deleteMonth(month.getId());

				if (linkedOnDeletingOne) {
					ProfileManager.getInstance().linkonMonth(null);
				}

				LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(MonthSelectionActivity.EVENT_REFRESH));
			}
		});
		dialog.setButton2(context.getText(R.string.cancel), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog1, int which) {
				return;
			}
		});
		dialog.show();
		return true;
	}

}
