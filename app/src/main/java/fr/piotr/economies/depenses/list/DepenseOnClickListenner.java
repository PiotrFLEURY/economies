package fr.piotr.economies.depenses.list;

import fr.piotr.economies.DepensesListActivity;
import fr.piotr.economies.NewDepenseActivity;
import fr.piotr.economies.persistance.serializable.Buy;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class DepenseOnClickListenner implements OnClickListener {

	private DepensesListActivity	context;
	private Buy						buy;

	public DepenseOnClickListenner(final DepensesListActivity ctxt, final Buy aBuys) {
		context = ctxt;
		buy = aBuys;
	}

	public void onClick(View v) {
		Intent intent = new Intent(context, NewDepenseActivity.class);
		intent.putExtra("buyToEdit", buy.getId());
		context.startActivity(intent);
		context.finish();
	}

}
