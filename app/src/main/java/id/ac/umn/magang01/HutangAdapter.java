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

public class HutangAdapter extends ArrayAdapter {
    private ArrayList<HutangModel> items;
    private ArrayList<HutangModel> items2;
    private HutangAdapter.HutangFilter filter;

    @NonNull
    @Override
    public Filter getFilter() {
        if(filter==null){
            filter = new HutangAdapter.HutangFilter();
        }
        return filter;
    }

    HutangAdapter(Context context, int layout, ArrayList<HutangModel> items){
        super(context, layout);
        this.items = items;
        this.items2 =items;

    }
    public static class ViewHolder {
        TextView nama;
        TextView hutang;
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
        HutangAdapter.ViewHolder viewHolder;

        if (row == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.list_hutang, parent, false);
            viewHolder = new HutangAdapter.ViewHolder();

            viewHolder.nama = (TextView) row.findViewById(R.id.suppName);
            viewHolder.hutang = (TextView) row.findViewById(R.id.sisaHutang);
            row.setTag(viewHolder);
        } else {
            viewHolder = (HutangAdapter.ViewHolder) row.getTag();
        }

        HutangModel supplier = items2.get(position);
        viewHolder.nama.setText(supplier.getName());
        viewHolder.hutang.setText(supplier.getSisa());

        return row;
    }

    private class HutangFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint.toString().length() > 0) {
                ArrayList<HutangModel> filteredItems = new ArrayList<HutangModel>();

                for (int i = 0, l = items.size(); i < l; i++) {
                    HutangModel hutangSupp = items.get(i);
                    if (hutangSupp.toString().toLowerCase().contains(constraint))
                        filteredItems.add(hutangSupp);
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
            items2 = (ArrayList<HutangModel>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = items2.size(); i < l; i++) {
                add(items2.get(i));
                notifyDataSetInvalidated();
            }
        }
    }
}
