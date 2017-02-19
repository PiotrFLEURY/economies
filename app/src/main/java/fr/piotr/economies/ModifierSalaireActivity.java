package fr.piotr.economies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;

import fr.piotr.economies.managers.ProfileManager;
import fr.piotr.economies.persistance.PersistanceHelper;
import fr.piotr.economies.persistance.serializable.Month;

public class ModifierSalaireActivity extends Activity {

	TextView			titre;
	EditText			montantSalaire;
	ImageView			okButton;
	ImageView			cancelButton;

	PersistanceHelper	persistanceHelper;

	@Override
	public void onBackPressed() {
		back();
	}

	protected void back() {
		persistanceHelper.close();
		finish();
		startActivity(new Intent(ModifierSalaireActivity.this, DepensesListActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modifiersalairelayout);

		persistanceHelper = PersistanceHelper.getInstance(this);

		titre = (TextView) findViewById(R.id.modifierSalaireTitle);
		String text = getText(R.string.salaireof) + "\n"
				+ ProfileManager.getInstance().getLinkedMonth().toString();
		titre.setText(text);
		titre.setTextSize(25);

		montantSalaire = (EditText) findViewById(R.id.modifierSalaireEditText);
		montantSalaire.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//
			}

			public void afterTextChanged(Editable s) {
				verifyCanValidate();
			}
		});

		okButton = (ImageView) findViewById(R.id.modifierSalaireOkButton);
		okButton.setOnClickListener(v -> {
            ProfileManager profileManager = ProfileManager.getInstance();
            Month month = profileManager.getLinkedMonth();
            month.setSalaire(new BigDecimal(montantSalaire.getText().toString()));

            persistanceHelper.updateSalaire(month);
            back();
        });

		cancelButton = (ImageView) findViewById(R.id.modifierSalaireAnnulerButton);
		cancelButton.setOnClickListener(v -> back());
	}

	protected void verifyCanValidate() {
		okButton.setEnabled(montantSalaire.getText().length() > 0);
	}

}
