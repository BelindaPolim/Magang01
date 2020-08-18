package id.ac.umn.magang01;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MainRecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView tv1,tv2;
    ImageView imageView;
    CardView cardView;

    public MainRecyclerViewHolder(View itemView) {
        super(itemView);

        tv1 = itemView.findViewById(R.id.namaMenu);
//        tv2 = itemView.findViewById(R.id.descMenu);
        imageView = itemView.findViewById(R.id.menuIcon);
        cardView = itemView.findViewById(R.id.cardView);
    }
}
