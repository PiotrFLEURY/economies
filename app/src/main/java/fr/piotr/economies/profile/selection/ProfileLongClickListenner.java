package fr.piotr.economies.profile.selection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnLongClickListener;
import fr.piotr.economies.ProfileSelectionActivity;
import fr.piotr.economies.R;
import fr.piotr.economies.managers.ProfileManager;
import fr.piotr.economies.persistance.PersistanceHelper;
import fr.piotr.economies.persistance.serializable.Profile;

public class ProfileLongClickListenner implements OnLongClickListener {

	protected final Profile				profile;

	public ProfileLongClickListenner(final Profile aProfile) {
		profile = aProfile;
	}

	public boolean onLongClick(final View v) {
		Context context = v.getContext();
		AlertDialog dialog = new AlertDialog.Builder(v.getContext()).create();
		dialog.setMessage(context.getText(R.string.deleteprofile));
		dialog.setButton(context.getText(R.string.ok), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog1, int which) {


				PersistanceHelper.getInstance(context).deleteProfile(profile.getId());

				ProfileManager.getInstance().linkOn(null);
				ProfileManager.getInstance().linkonMonth(null);

				LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ProfileSelectionActivity.EVENT_REFRESH));
			}
		});
		dialog.setButton2(context.getText(R.string.cancel), new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog1, int which) {
				return;
			}
		});
		dialog.show();
		return true;
	}

}
