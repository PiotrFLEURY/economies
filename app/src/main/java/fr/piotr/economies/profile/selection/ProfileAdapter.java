package fr.piotr.economies.profile.selection;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import fr.piotr.economies.ProfileSelectionActivity;
import fr.piotr.economies.R;
import fr.piotr.economies.persistance.serializable.Profile;

/**
 * Created by piotr_000 on 20/08/2016.
 */
public class ProfileAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

    List<Profile> profiles;

    public ProfileAdapter(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ProfileViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.profilelayout, parent, false));
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        Context context = holder.layout.getContext();
        if (position == profiles.size()) {

            holder.setAvatar(R.drawable.socialaddperson);
            holder.setText(R.string.nouveauprofile);

            holder.layout.setOnClickListener(v -> {
                LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ProfileSelectionActivity.EVENT_GOTO_CREATE_NEW_PROFILE));
            });

        } else {
            holder.setAvatar(R.drawable.socialperson);
            Profile aProfile = getProfileAt(position);
            holder.setText(String.format("%s %s %s %s", aProfile.getName(),
                            context.getString(R.string.profilesummaryrest),
                            aProfile.getRestOfCurrentMonth(),
                            context.getString(R.string.currency)));

            holder.layout.setOnClickListener(new ProfileOnClickListenner(aProfile));
            holder.layout.setOnLongClickListener(new ProfileLongClickListenner(aProfile));
        }
    }

    private Profile getProfileAt(int position){
        return profiles.get(position);
    }

    @Override
    public int getItemCount() {
        return profiles.size()+1;
    }
}
