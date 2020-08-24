package id.ac.umn.magang01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PenjualanAdapter extends ArrayAdapter {
    private ArrayList<PenjualanModel> items;
    private ArrayList<PenjualanModel> items2;
    private PenjualanAdapter.PenjualanFilter filter;

    @NonNull
    @Override
    public Filter getFilter() {
        if(filter==null){
            filter = new PenjualanAdapter.PenjualanFilter();
        }
        return filter;
    }

    PenjualanAdapter(Context context, int layout, ArrayList<PenjualanModel> items){
        super(context, layout);
        this.items = items;
        this.items2 =items;

    }
    public static class ViewHolder {
        TextView nama;
        TextView penjualan;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row;
        row = convertView;
        PenjualanAdapter.ViewHolder viewHolder;

        if (row == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.list_penjualan, parent, false);
            viewHolder = new PenjualanAdapter.ViewHolder();

            viewHolder.nama = (TextView) row.findViewById(R.id.custName);
            viewHolder.penjualan = (TextView) row.findViewById(R.id.nilaiPenjualan);
            row.setTag(viewHolder);
        } else {
            viewHolder = (PenjualanAdapter.ViewHolder) row.getTag();
        }

        PenjualanModel customer = items2.get(position);
        viewHolder.nama.setText(customer.getName());
        viewHolder.penjualan.setText(customer.getNilai());

        return row;
    }

    private class PenjualanFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                ArrayList<PenjualanModel> filteredItems = new ArrayList<PenjualanModel>();

                for (int i = 0, l = items.size(); i < l; i++) {
                    PenjualanModel penjualanSupp = items.get(i);
                    if (penjualanSupp.toString().toLowerCase().contains(constraint))
                        filteredItems.add(penjualanSupp);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = items;
                    result.count = items.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items2 = (ArrayList<PenjualanModel>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = items2.size(); i < l; i++) {
                add(items2.get(i));
                notifyDataSetInvalidated();
            }
        }
    }
}
