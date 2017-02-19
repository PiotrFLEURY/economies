package fr.piotr.economies.depenses.list;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import fr.piotr.economies.DepensesListActivity;
import fr.piotr.economies.NewDepenseActivity;

public class NewDepenseOnClickListenner implements OnClickListener {

	private DepensesListActivity context;

	public NewDepenseOnClickListenner(final DepensesListActivity ctxt){
		context=ctxt;
	}

	public void onClick(View v) {
		context.finish();
		context.startActivity(new Intent(context,NewDepenseActivity.class));
	}

}
