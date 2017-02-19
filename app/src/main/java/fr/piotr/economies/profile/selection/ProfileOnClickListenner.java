package fr.piotr.economies.profile.selection;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import fr.piotr.economies.ProfileSelectionActivity;
import fr.piotr.economies.managers.ProfileManager;
import fr.piotr.economies.persistance.serializable.Profile;

public class ProfileOnClickListenner implements OnClickListener {

	private Profile profile;

	public ProfileOnClickListenner(final Profile aProfile){
		profile=aProfile;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		ProfileManager.getInstance().linkOn(profile);
		LocalBroadcastManager.getInstance(v.getContext()).sendBroadcast(new Intent(ProfileSelectionActivity.EVENT_GOTO_LINKED_PROFILE));
	}

}
