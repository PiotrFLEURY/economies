package fr.piotr.economies.month.selection;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fr.piotr.economies.R;

/**
 * Created by piotr_000 on 20/08/2016.
 */
public class MonthViewHolder extends RecyclerView.ViewHolder {

    View layout;
    TextView monthTextView;

    public MonthViewHolder(View itemView) {
        super(itemView);
        this.layout = itemView;
        this.monthTextView = (TextView) layout.findViewById(R.id.monthlayouttextview);
    }

    public void setText(String text){
        monthTextView.setText(text);
    }

}
