package id.ac.umn.magang01;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainRecyclerAdapter extends  RecyclerView.Adapter<MainRecyclerViewHolder> {

    private final Context context;

    String [] name = {"Penjualan","Pembelian","Piutang","Hutang"};
//    String [] desc = {"Deskripsi Penjualan","Deskripsi Pembelian","Deskripsi Piutang","Deskripsi Hutang"};
    private int[] img = {
        R.drawable.penjualan,
        R.drawable.pembelian,
        R.drawable.piutang,
        R.drawable.hutang
    };
    LayoutInflater inflater;
    public MainRecyclerAdapter(Context context) {
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MainRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.list_main, parent, false);

        return new MainRecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainRecyclerViewHolder holder, int position) {
        holder.tv1.setText(name[position]);
        holder.tv1.setOnClickListener(clickListener);
        holder.tv1.setTag(holder);
//        holder.tv2.setText(desc[position]);
        holder.imageView.setImageResource(img[position]);
        holder.imageView.setOnClickListener(clickListener);
        holder.imageView.setTag(holder);
        holder.cardView.setOnClickListener(clickListener);
        holder.cardView.setTag(holder);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainRecyclerViewHolder vholder = (MainRecyclerViewHolder) v.getTag();

            int position = vholder.getPosition();

            if(position == 0){
                Intent penjualan = new Intent(context, PenjualanActivity.class);
                context.startActivity(penjualan);
            }
            else if(position == 1){
                Intent pembelian = new Intent(context, PembelianActivity.class);
                context.startActivity(pembelian);
            }
            else if(position == 2){
                Intent piutang = new Intent(context, PiutangActivity.class);
                context.startActivity(piutang);
            }
            else if(position == 3){
                Intent hutang = new Intent(context, HutangActivity.class);
                context.startActivity(hutang);
            }
        }
    };

    @Override
    public int getItemCount() {
        return name.length;
    }
}
