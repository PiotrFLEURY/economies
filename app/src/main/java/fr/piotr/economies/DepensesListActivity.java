package fr.piotr.economies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import fr.piotr.economies.depenses.list.DepensesListAdapter;
import fr.piotr.economies.depenses.list.SalaireOnClickListenner;
import fr.piotr.economies.managers.ProcessManager;
import fr.piotr.economies.managers.ProfileManager;
import fr.piotr.economies.persistance.PersistanceHelper;
import fr.piotr.economies.persistance.serializable.Buy;
import fr.piotr.economies.persistance.serializable.Month;

public class DepensesListActivity extends Activity {

	Month						linkedMonth;
	PersistanceHelper			persistanceHelper;

    @Override
	public void onBackPressed() {
		persistanceHelper.close();
		finish();
		startActivity(new Intent(this, MonthSelectionActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.depenseslistlayout);

		linkedMonth = ProfileManager.getInstance().getLinkedMonth();
		persistanceHelper = PersistanceHelper.getInstance(this);
		persistanceHelper.updateMonthRest(linkedMonth);
		List<Buy> buys = persistanceHelper.getBuys(linkedMonth.getId());

		// titre
		TextView title = (TextView) findViewById(R.id.linkedmonthtextview);
		title.setText(ProfileManager.getInstance().getLinkedMonth().toString());

		// salaire
		TextView salaire = (TextView) findViewById(R.id.salaireTextView);
		salaire.setText(String.format("%s %s", ProcessManager.formatAmount(linkedMonth.getSalaire()), getText(R.string.currency)));
		salaire.setClickable(true);
		SalaireOnClickListenner salaireOnClickListenner = new SalaireOnClickListenner(this);
		ImageView btnSalaire = (ImageView) findViewById(R.id.btnSalaire);
		btnSalaire.setOnClickListener(salaireOnClickListenner);

		// total dépensé
		TextView totalDepense = (TextView) findViewById(R.id.totalDepenseTextView);
		totalDepense.setText(String.format("%s %s", ProcessManager.formatAmount(ProcessManager.getTotal(buys)), getString(R.string.currency)));

		// restant
		TextView restant = (TextView) findViewById(R.id.restantTextView);
		restant.setText(String.format("%s %s", ProcessManager.formatAmount(linkedMonth.getRest()), getText(R.string.currency)));

		// depenses
		ListView list = (ListView) findViewById(R.id.depensesListView);
		list.setAdapter(new DepensesListAdapter(this, buys));

		// bouton dépense
		ImageView btnDepense = (ImageView) findViewById(R.id.btnDepense);
		btnDepense.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(DepensesListActivity.this, NewDepenseActivity.class));
        });

		// bouton apport
		ImageView btnApport = (ImageView) findViewById(R.id.btnApport);
		btnApport.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(DepensesListActivity.this, NewApportActivity.class));
        });

	}

}
