package fr.piotr.economies;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import fr.piotr.economies.persistance.PersistanceHelper;

public class CreateNewProfileActivity extends Activity {

	EditText			textField;
	ImageView			btnValidate;

	PersistanceHelper	persistanceHelper;

	@Override
	public void onBackPressed() {
		if (persistanceHelper.getProfiles().isEmpty()) {
			finish();
		} else {
			finish();
			startActivity(new Intent(this, ProfileSelectionActivity.class));
		}
	}

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.createnewprofile);

		persistanceHelper = PersistanceHelper.getInstance(this);

		textField = (EditText) findViewById(R.id.textNewProfileName);
		btnValidate = (ImageView) findViewById(R.id.buttonValidateNewProfile);

		textField.setOnEditorActionListener((textView, i, keyEvent) -> {
            onValidate(textView);
            return true;
        });

		btnValidate.setOnClickListener(this::onValidate);

	}

	public void onValidate(View v) {
		if(textField.getText().toString().isEmpty()){
			Toast.makeText(this, R.string.please_type_profile_name, Toast.LENGTH_LONG).show();
		} else {
			createNewProfile();
			gotoProfileList();
		}
	}

	public void createNewProfile() {
		String profileName = textField.getText().toString();
		persistanceHelper.saveProfile(profileName);
	}

	public void gotoProfileList() {
		persistanceHelper.close();
		finish();
		startActivity(new Intent(this, ProfileSelectionActivity.class));
	}

}
