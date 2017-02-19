package fr.piotr.economies.depenses.list;

import java.text.SimpleDateFormat;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import fr.piotr.economies.DepensesListActivity;
import fr.piotr.economies.R;
import fr.piotr.economies.managers.ProcessManager;
import fr.piotr.economies.persistance.serializable.Buy;

public class DepensesListAdapter extends BaseAdapter implements ListAdapter {

	private List<Buy>				depenses;
	private DepensesListActivity	context;
	private LayoutInflater			mInflater;

	public DepensesListAdapter(final DepensesListActivity ctxt, final List<Buy> depensesList) {
		depenses = depensesList;
		context = ctxt;
		mInflater = LayoutInflater.from(ctxt);
	}

	public int getCount() {
		return depenses.size();
	}

	public Buy getItem(int position) {
		return depenses.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		RelativeLayout layoutItem;
		if (convertView == null) {
			layoutItem = (RelativeLayout) mInflater.inflate(R.layout.depenselayout, parent, false);
		} else {
			layoutItem = (RelativeLayout) convertView;
		}
		final Buy buy = depenses.get(position);

		TextView depenseLabelTextView = (TextView) layoutItem.findViewById(R.id.depenselayoutlabel);
		depenseLabelTextView.setText(buy.getLabel());

		if (buy.isMonthly()) {
			ImageView depenseMensuelleImg = (ImageView) layoutItem
					.findViewById(R.id.depenseMensuelleImg);
			depenseMensuelleImg.setVisibility(View.VISIBLE);
		}

		TextView depenseLayoutDate = (TextView) layoutItem.findViewById(R.id.depenselayoutdate);
		depenseLayoutDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(buy.getDate()));

		TextView depenseAmountTextView = (TextView) layoutItem
				.findViewById(R.id.depenselayoutamount);
		depenseAmountTextView.setText(ProcessManager.formatAmount(buy.getAmount().negate()) + " "
				+ context.getString(R.string.currency));

		layoutItem.setOnLongClickListener(new DepenseOnLongClickListenner(context, buy.getId()));
		layoutItem.setOnClickListener(new DepenseOnClickListenner(context, buy));

		return layoutItem;

	}

}
