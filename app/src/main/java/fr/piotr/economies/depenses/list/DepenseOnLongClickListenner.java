package fr.piotr.economies.depenses.list;

import android.app.Activity;
import android.view.View;
import android.view.View.OnLongClickListener;

public class DepenseOnLongClickListenner implements OnLongClickListener {

	Activity	context;
	int			buyId;

	public DepenseOnLongClickListenner(final Activity ctxt, final int aBuyId) {
		context = ctxt;
		buyId = aBuyId;
	}

	public boolean onLongClick(View v) {
		DepenseDeleteAlertDialog dialog = new DepenseDeleteAlertDialog(context, buyId);
		dialog.show();
		return true;
	}

}
