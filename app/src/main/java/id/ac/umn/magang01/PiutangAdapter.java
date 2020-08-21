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

public class PiutangAdapter extends ArrayAdapter {
    private ArrayList<PiutangModel> items;
    private ArrayList<PiutangModel> items2;
    private PiutangFilter filter;

    @NonNull
    @Override
    public Filter getFilter() {
        if(filter==null){
            filter = new PiutangFilter();
        }
        return filter;
    }

    PiutangAdapter(Context context, int layout, ArrayList<PiutangModel> items){
        super(context, layout);
        this.items = items;
        this.items2 =items;
//        this.items2=new ArrayList<PiutangCust>();
//        this.items2.addAll(items);
//        this.items=new ArrayList<PiutangCust>();
//        this.items.addAll(items);

    }
    public static class ViewHolder {
        TextView nama;
        TextView piutang;
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
        ViewHolder viewHolder;

        if(row == null){
            row = LayoutInflater.from(getContext()).inflate(R.layout.list_piutang, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.nama = (TextView) row.findViewById(R.id.custName);
            viewHolder.piutang = (TextView) row.findViewById(R.id.sisaPiutang);
            row.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) row.getTag();
        }

        PiutangModel customer = items2.get(position);
        viewHolder.nama.setText(customer.getName());
        viewHolder.piutang.setText(customer.getSisa());

        return row;
    }

    private class PiutangFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint.toString().length() > 0)
            {
                ArrayList<PiutangModel> filteredItems = new ArrayList<PiutangModel>();

                for(int i = 0, l = items.size(); i < l; i++)
                {
                    PiutangModel piutangCust = items.get(i);
                    if(piutangCust.toString().toLowerCase().contains(constraint))
                        filteredItems.add(piutangCust);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = items;
                    result.count = items.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            items2 = (ArrayList<PiutangModel>)results.values;
            notifyDataSetChanged();
            clear();
            for (int i=0, l = items2.size(); i < l; i++){
                add(items2.get(i));
                notifyDataSetInvalidated();
            }
        }
    }
}
