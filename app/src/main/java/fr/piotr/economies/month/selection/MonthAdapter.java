package fr.piotr.economies.month.selection;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import fr.piotr.economies.ProfileSelectionActivity;
import fr.piotr.economies.R;
import fr.piotr.economies.persistance.serializable.Month;
import fr.piotr.economies.persistance.serializable.Profile;
import fr.piotr.economies.profile.selection.ProfileLongClickListenner;
import fr.piotr.economies.profile.selection.ProfileOnClickListenner;

/**
 * Created by piotr_000 on 20/08/2016.
 */
public class MonthAdapter extends RecyclerView.Adapter<MonthViewHolder> {

    List<Month> monthes;

    public MonthAdapter(List<Month> monthes) {
        this.monthes = monthes;
    }

    public void setMonthes(List<Month> monthes) {
        this.monthes = monthes;
    }

    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MonthViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.monthlayout, parent, false));
    }

    @Override
    public void onBindViewHolder(MonthViewHolder holder, int position) {
        Context context = holder.layout.getContext();
        Month aMonth = getMonthAt(position);

        holder.setText(aMonth.toString() + "\n" + context.getString(R.string.restant) + " "
                + aMonth.getRest() + " " + context.getString(R.string.currency));

        holder.layout.setOnClickListener(new MonthOnClickListenner(aMonth));
        holder.layout.setOnLongClickListener(new MonthOnLongClickListenner(aMonth));
    }

    private Month getMonthAt(int position){
        return monthes.get(position);
    }

    @Override
    public int getItemCount() {
        return monthes.size();
    }
}
