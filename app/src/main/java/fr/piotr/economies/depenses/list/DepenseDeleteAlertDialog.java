package fr.piotr.economies.depenses.list;

import fr.piotr.economies.DepensesListActivity;
import fr.piotr.economies.R;
import fr.piotr.economies.persistance.PersistanceHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class DepenseDeleteAlertDialog extends AlertDialog {

	protected final Activity	myContext;

	public DepenseDeleteAlertDialog(Activity context, final int buyId) {
		super(context);
		myContext = context;
		setMessage(context.getText(R.string.deletedepense));
		setButton(context.getText(R.string.ok), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog1, int which) {

				PersistanceHelper.getInstance(myContext).deleteBuy(buyId);

				myContext.startActivity(new Intent(myContext, DepensesListActivity.class));
				myContext.finish();
			}
		});
		setButton2(context.getText(R.string.cancel), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog1, int which) {
				return;
			}
		});
	}

}
