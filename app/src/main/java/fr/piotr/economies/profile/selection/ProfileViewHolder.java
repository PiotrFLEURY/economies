package fr.piotr.economies.profile.selection;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.piotr.economies.R;

/**
 * Created by piotr_000 on 20/08/2016.
 */
public class ProfileViewHolder extends RecyclerView.ViewHolder {

    View layout;
    ImageView avatar;
    TextView profileNom;

    public ProfileViewHolder(View itemView) {
        super(itemView);
        this.layout = itemView;
        this.avatar = (ImageView) layout.findViewById(R.id.profileavatar);
        this.profileNom = (TextView) layout.findViewById(R.id.profilenom);
    }

    public void setAvatar(int resId){
        avatar.setImageResource(resId);
    }

    public void setText(int resId){
        profileNom.setText(profileNom.getContext().getString(resId));
    }

    public void setText(String text){
        profileNom.setText(text);
    }

}
