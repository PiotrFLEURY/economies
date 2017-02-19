package fr.piotr.economies;

import java.math.BigDecimal;

import android.widget.TextView;

public class NewApportActivity extends NewDepenseActivity {

	@Override
	protected BigDecimal getTypedAmount() {
		return super.getTypedAmount().negate();
	}

	@Override
	protected void setLayoutTitle() {
		((TextView) findViewById(R.id.newDepenseTitleTextView)).setText(R.string.nouvelapport);
	}

}
