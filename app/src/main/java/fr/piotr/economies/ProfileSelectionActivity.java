package fr.piotr.economies;

import java.util.List;

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

import fr.piotr.economies.profile.selection.ProfileAdapter;
import fr.piotr.economies.persistance.PersistanceHelper;
import fr.piotr.economies.persistance.serializable.Profile;

public class ProfileSelectionActivity extends Activity {

	public static final String EVENT_GOTO_CREATE_NEW_PROFILE = "EVENT_GOTO_CREATE_NEW_PROFILE";
	public static final String EVENT_GOTO_LINKED_PROFILE = "EVENT_GOTO_LINKED_PROFILE";
	public static final String EVENT_REFRESH = "EVENT_REFRESH";

	public static final int		MENU_CREER_PROFILE	= 1;

	private PersistanceHelper	persistanceHelper;

	LocalBroadcastManager localBroadcastManager;

    ProfileAdapter adapter;

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			switch(intent.getAction()){
				case EVENT_GOTO_CREATE_NEW_PROFILE:gotoCreateNewProfile();break;
				case EVENT_GOTO_LINKED_PROFILE:gotoLinkedProfile();break;
				case EVENT_REFRESH:refresh();break;
				default:break;
			}
		}
	};

    private void refresh() {
        adapter.setProfiles(getProfiles());
        adapter.notifyDataSetChanged();
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profileselectionlayout);

		localBroadcastManager = LocalBroadcastManager.getInstance(this);

		persistanceHelper = PersistanceHelper.getInstance(this);

		RecyclerView rvProfiles = (RecyclerView) findViewById(R.id.rvProfileSelection);
        List<Profile> profiles = getProfiles();
        if (profiles.isEmpty()) {
            gotoCreateNewProfile();
        } else {
            rvProfiles.setLayoutManager(new LinearLayoutManager(this));
            rvProfiles.setItemAnimator(new DefaultItemAnimator());
			adapter = new ProfileAdapter(profiles);
            rvProfiles.setAdapter(adapter);
        }

	}

	/**
	 * @return the profiles file list
	 */
	private List<Profile> getProfiles() {
		return persistanceHelper.getProfiles();
	}

	public void gotoLinkedProfile() {
		persistanceHelper.close();
		finish();
		startActivity(new Intent(this, MonthSelectionActivity.class));
	}

	/**
	 * 
	 */
	public void gotoCreateNewProfile() {
		persistanceHelper.close();
		finish();
		startActivity(new Intent(this, CreateNewProfileActivity.class));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_CREER_PROFILE, Menu.NONE, R.string.nouveauprofile).setIcon(
				R.drawable.socialaddperson);
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
		case MENU_CREER_PROFILE:
			gotoCreateNewProfile();
			break;

		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(EVENT_GOTO_CREATE_NEW_PROFILE);
        filter.addAction(EVENT_GOTO_LINKED_PROFILE);
        filter.addAction(EVENT_REFRESH);
		localBroadcastManager.registerReceiver(receiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		localBroadcastManager.unregisterReceiver(receiver);
	}
}
