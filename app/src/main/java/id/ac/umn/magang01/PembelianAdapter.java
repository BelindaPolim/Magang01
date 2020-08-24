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

public class PembelianAdapter extends ArrayAdapter {
    private ArrayList<PembelianModel> items;
    private ArrayList<PembelianModel> items2;
    private PembelianAdapter.PembelianFilter filter;

    @NonNull
    @Override
    public Filter getFilter() {
        if(filter==null){
            filter = new PembelianAdapter.PembelianFilter();
        }
        return filter;
    }

    PembelianAdapter(Context context, int layout, ArrayList<PembelianModel> items){
        super(context, layout);
        this.items = items;
        this.items2 =items;

    }
    public static class ViewHolder {
        TextView nama;
        TextView pembelian;
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
        PembelianAdapter.ViewHolder viewHolder;

        if (row == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.list_pembelian, parent, false);
            viewHolder = new PembelianAdapter.ViewHolder();

            viewHolder.nama = (TextView) row.findViewById(R.id.suppName);
            viewHolder.pembelian = (TextView) row.findViewById(R.id.nilaiPembelian);
            row.setTag(viewHolder);
        } else {
            viewHolder = (PembelianAdapter.ViewHolder) row.getTag();
        }

        PembelianModel supplier = items2.get(position);
        viewHolder.nama.setText(supplier.getName());
        viewHolder.pembelian.setText(supplier.getNilai());

        return row;
    }

    private class PembelianFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                ArrayList<PembelianModel> filteredItems = new ArrayList<PembelianModel>();

                for (int i = 0, l = items.size(); i < l; i++) {
                    PembelianModel pembelianSupp = items.get(i);
                    if (pembelianSupp.toString().toLowerCase().contains(constraint))
                        filteredItems.add(pembelianSupp);
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
            items2 = (ArrayList<PembelianModel>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = items2.size(); i < l; i++) {
                add(items2.get(i));
                notifyDataSetInvalidated();
            }
        }
    }
}
