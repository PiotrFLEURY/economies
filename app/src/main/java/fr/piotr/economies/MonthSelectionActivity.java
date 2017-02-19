package fr.piotr.economies;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.piotr.economies.managers.ProfileManager;
import fr.piotr.economies.month.selection.MonthAdapter;
import fr.piotr.economies.other.LastMonth;
import fr.piotr.economies.persistance.PersistanceHelper;
import fr.piotr.economies.persistance.serializable.Buy;
import fr.piotr.economies.persistance.serializable.Month;
import fr.piotr.economies.persistance.serializable.Profile;

public class MonthSelectionActivity extends Activity {

	public static final String EVENT_GOTO_LINKED_MONTH = "EVENT_GOTO_LINKED_MONTH";
	public static final String EVENT_REFRESH = "EVENT_REFRESH";

	private static final int	MENU_ANTICIPER_MOIS	= 1;
	private PersistanceHelper	persistanceHelper;

	LocalBroadcastManager localBroadcastManager;

	MonthAdapter monthAdapter;

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			switch (intent.getAction()){
				case EVENT_GOTO_LINKED_MONTH:gotoLinkedMonth();break;
				case EVENT_REFRESH:refresh();break;
				default:break;
			}
		}
	};

	private void refresh() {
		monthAdapter.setMonthes(getMonths());
		monthAdapter.notifyDataSetChanged();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		persistanceHelper.close();
		finish();
		startActivity(new Intent(this, ProfileSelectionActivity.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monthselectionlayout);
		persistanceHelper = PersistanceHelper.getInstance(this);

		localBroadcastManager = LocalBroadcastManager.getInstance(this);

		TextView profileTitle = (TextView) findViewById(R.id.titreprofiletextview);
		Profile linkedProfile = ProfileManager.getInstance().getLinkedProfile();
		List<Month> monthes = getMonths();
		Month newMonth = new Month(BigDecimal.ZERO);

		boolean contains = getMonth(newMonth.toString(), monthes) != null;

		if (!contains) {
			LastMonth lastMonth = new LastMonth();
			Month originalMonth = getMonth(lastMonth.toString(), monthes);
			createNewMonth(linkedProfile, originalMonth, newMonth);
			monthes.add(newMonth);
		}

		profileTitle.setText(linkedProfile.getName());

		RecyclerView rvMonth = (RecyclerView) findViewById(R.id.rvMonthSelection);

		rvMonth.setLayoutManager(new LinearLayoutManager(this));
		rvMonth.setItemAnimator(new DefaultItemAnimator());
		monthAdapter = new MonthAdapter(monthes);
		rvMonth.setAdapter(monthAdapter);

	}

	private List<Month> getMonths() {
		return persistanceHelper.getMonthes(ProfileManager.getInstance().getLinkedProfile().getId());
	}

	private void createNewMonth(Profile linkedProfile, Month originalMonth, Month newMonth) {
		if (originalMonth != null) {
			newMonth.setSalaire(originalMonth.getSalaire());
		}
		persistanceHelper.saveMonth(linkedProfile.getId(), newMonth);
		Month savedMonth = persistanceHelper.getMonth(linkedProfile.getId(), newMonth.getLabel(),
				newMonth.getYear());
		try {
			copyLastMonthSpends(originalMonth, savedMonth);
		} catch (CloneNotSupportedException e1) {
			Toast.makeText(this, e1.getMessage(), Toast.LENGTH_LONG).show();
			finish();
		}
		persistanceHelper.close();
		finish();
		startActivity(new Intent(this, MonthSelectionActivity.class));
	}

	private Month getMonth(final String toStringMonth, List<Month> monthes) {
		for (final Month m : monthes) {
			if (m.toString().equals(toStringMonth)) {
				return m;
			}
		}
		return null;
	}

	private void copyLastMonthSpends(Month originalMonth, final Month newMonth)
			throws CloneNotSupportedException {

		if (originalMonth != null) {

			List<Buy> lastMonthBuys = persistanceHelper.getBuys(originalMonth.getId());
			Date today = Calendar.getInstance().getTime();
			for (final Buy buy : lastMonthBuys) {
				if (buy.isMonthly()) {
					Buy clonedOne = buy.clone();
					clonedOne.setDate(today);
					persistanceHelper.createBuy(clonedOne, newMonth);
				}
			}
		}
	}

	public void gotoLinkedMonth() {
		persistanceHelper.close();
		finish();
		startActivity(new Intent(this, DepensesListActivity.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ANTICIPER_MOIS, Menu.NONE, R.string.anticipermoisprochain).setIcon(
				R.drawable.collectionsgototoday);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onMenuItemSelected(int, android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ANTICIPER_MOIS:
			Profile linkedProfile = ProfileManager.getInstance().getLinkedProfile();
			List<Month> monthes = persistanceHelper.getMonthes(linkedProfile.getId());
			Month newMonth = new Month(BigDecimal.ZERO);
			boolean contains = getMonth(newMonth.toString(), monthes) != null;
			int i = 1;
			while (contains) {
				newMonth.setAnticiper(i++);
				contains = getMonth(newMonth.toString(), monthes) != null;
			}
			createNewMonth(linkedProfile, getMonth(new Month(BigDecimal.ZERO).toString(), monthes),
					newMonth);
			monthes.add(newMonth);
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(EVENT_GOTO_LINKED_MONTH);
		filter.addAction(EVENT_REFRESH);
		localBroadcastManager.registerReceiver(receiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		localBroadcastManager.unregisterReceiver(receiver);
	}
}
