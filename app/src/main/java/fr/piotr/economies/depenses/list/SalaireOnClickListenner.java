package fr.piotr.economies.depenses.list;

import fr.piotr.economies.DepensesListActivity;
import fr.piotr.economies.ModifierSalaireActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class SalaireOnClickListenner implements OnClickListener {

	DepensesListActivity context;

	public SalaireOnClickListenner(final DepensesListActivity ctxt){
		context=ctxt;
	}

	public void onClick(View arg0) {
		context.finish();
		context.startActivity(new Intent(context,ModifierSalaireActivity.class));
	}

}
