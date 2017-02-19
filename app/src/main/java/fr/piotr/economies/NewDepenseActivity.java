package fr.piotr.economies;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.piotr.economies.depenses.list.DatePickerDialogListenner;
import fr.piotr.economies.depenses.list.DepenseDeleteAlertDialog;
import fr.piotr.economies.managers.ProfileManager;
import fr.piotr.economies.persistance.PersistanceHelper;
import fr.piotr.economies.persistance.serializable.Buy;
import fr.piotr.economies.persistance.serializable.Month;

public class NewDepenseActivity extends Activity {

	private static final String	DD_MM_YYYY	= "dd/MM/yyyy";

	protected EditText			amountEditText;
    protected CheckBox			newDepenseCheck;
	TextView					dateValueTextView;
	ImageView					deleteButton;
	EditText					newDepenseDesignationEditText;
    Button					    newDepenseChangeDateImgBtn;

	PersistanceHelper			persistanceHelper;

	@Override
	public void onBackPressed() {
		persistanceHelper.close();
		finish();
		startActivity(new Intent(this, DepensesListActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		persistanceHelper = PersistanceHelper.getInstance(this);

		Buy getted = null;
		Intent intent = getIntent();
		if (intent != null) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				Object object = extras.get("buyToEdit");
				if (object != null) {
					getted = persistanceHelper.getBuy((Integer) object);
				}
			}
		}
		final Buy toEdit = getted;

		setContentView(R.layout.newdepenselayout);

		setLayoutTitle();

		newDepenseDesignationEditText = (EditText) findViewById(R.id.newDepenseDesignation);

		amountEditText = (EditText) findViewById(R.id.newDepenseAmountEditText);

		newDepenseCheck = (CheckBox) findViewById(R.id.newDepenseCheck);

		dateValueTextView = (TextView) findViewById(R.id.newDepenseDateTextView);
		final DatePickerDialogListenner dateSetListenner = new DatePickerDialogListenner(
				dateValueTextView);
		dateValueTextView.setText(dateSetListenner.getDate(DD_MM_YYYY));

		newDepenseChangeDateImgBtn = (Button) findViewById(R.id.newDepenseChangeDateBtn);
		newDepenseChangeDateImgBtn.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            if (toEdit != null) {
                calendar.setTime(toEdit.getDate());
            } else if (!dateValueTextView.getText().toString().equals("")) {
                try {
                    calendar.setTime(new SimpleDateFormat("dd/MM/yyyy").parse(dateValueTextView
                            .getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(NewDepenseActivity.this,
                    dateSetListenner, year, month, day);
            dialog.show();
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setText(getString(R.string.ok));
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setText(getString(R.string.cancel));

        });

        ImageView newDepenseOkButton = (ImageView) findViewById(R.id.newDepenseOkButton);
		newDepenseOkButton.setVisibility(Button.VISIBLE);
		newDepenseOkButton.setOnClickListener(v -> {
            Month linkedMonth = ProfileManager.getInstance().getLinkedMonth();

            String label = newDepenseDesignationEditText.getText().toString();
            BigDecimal amount = getTypedAmount();
            boolean monthly = newDepenseCheck.isChecked();
            Date date = dateSetListenner.getCalendar().getTime();

            if (toEdit != null) {
                toEdit.setLabel(label);
                toEdit.setAmount(amount);
                toEdit.setMonthly(monthly);
                toEdit.setDate(date);
                persistanceHelper.updateBuy(toEdit, linkedMonth);
            } else {
                Buy buy = new Buy(label, amount, null, monthly, date);
                persistanceHelper.createBuy(buy, linkedMonth);
            }
            persistanceHelper.close();
            finish();
            startActivity(new Intent(NewDepenseActivity.this, DepensesListActivity.class));
        });

//        ImageView newDepenseCancelButton = (ImageView) findViewById(R.id.newDepenseCancelButton);
//		newDepenseCancelButton.setVisibility(Button.VISIBLE);
//		newDepenseCancelButton.setOnClickListener(v -> {
//            persistanceHelper.close();
//            finish();
//            startActivity(new Intent(NewDepenseActivity.this, DepensesListActivity.class));
//        });

		if (toEdit != null) {
			newDepenseDesignationEditText.setText(toEdit.getLabel());

			amountEditText.setText(toEdit.getAmount().toString());

			newDepenseCheck.setChecked(toEdit.isMonthly());

			dateValueTextView.setText(new SimpleDateFormat("dd/MM/yyyy").format(toEdit.getDate()));

			deleteButton = (ImageView) findViewById(R.id.newDepenseDeleteBtn);
			deleteButton.setVisibility(Button.VISIBLE);
			deleteButton.setEnabled(true);
			deleteButton.setOnClickListener(v -> {
                DepenseDeleteAlertDialog dialog = new DepenseDeleteAlertDialog(
                        NewDepenseActivity.this, toEdit.getId());
                dialog.show();
            });
		} else {
			deleteButton = (ImageView) findViewById(R.id.newDepenseDeleteBtn);
			deleteButton.setVisibility(Button.INVISIBLE);
		}
	}

	protected void setLayoutTitle() {
		((TextView) findViewById(R.id.newDepenseTitleTextView)).setText(R.string.nouvelledepense);
	}

	protected BigDecimal getTypedAmount() {
		String amout = amountEditText.getText().toString();
		if(amout.isEmpty()){
			return BigDecimal.ZERO;
		}
		return new BigDecimal(amout);
	}

}
