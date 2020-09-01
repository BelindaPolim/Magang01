package id.ac.umn.magang01;

import android.content.Context;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class InfoAdapter extends ArrayAdapter<InfoModel>{

    private String TAG = InfoAdapter.class.getSimpleName();


    public InfoAdapter(Context context, List<InfoModel> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        InfoModel info = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_info_tambahan, parent, false);
        }

        TextView infoName = convertView.findViewById(R.id.infoName);
        TextView infoVal = convertView.findViewById(R.id.infoValue);

        infoName.setText(info.getName());
        infoVal.setText(info.getInfo());

        return convertView;
    }
}
