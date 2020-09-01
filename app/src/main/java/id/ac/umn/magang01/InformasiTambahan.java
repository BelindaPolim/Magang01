package id.ac.umn.magang01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class InformasiTambahan extends AppCompatActivity {

    private String TAG = InformasiTambahan.class.getSimpleName();

    private ProgressDialog prog;
    private ListView lv;
    ImageView imgBack;

    ArrayList<InfoModel> informasi = new ArrayList<>();

    String apiUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informasi_tambahan);

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParent();
                finish();
            }
        });

        lv = findViewById(R.id.listView);

        Intent getPosition = getIntent();
        String url = getPosition.getStringExtra("url");
        String param = getPosition.getStringExtra("param");
        String code = getPosition.getStringExtra("code");

        apiUrl = url + param + code;
        Log.d(TAG, "onCreate - apiUrl : " + apiUrl);
        new GetInfo().execute();
    }

    private void  showProgressDialog(){
        if(prog == null){
            prog = new ProgressDialog(InformasiTambahan.this);
            prog.setMessage("Fetching data...");
            prog.setIndeterminate(false);
            prog.setCancelable(false);
        }
        prog.show();
    }

    private void dismissProgressDialog() {
        if (prog != null && prog.isShowing()) {
            prog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private class GetInfo extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected Void doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();

            String url = apiUrl;
            String jsonStr = sh.makeServiceCall(url);

//            DecimalFormat pemisahRibuan = (DecimalFormat) DecimalFormat.getCurrencyInstance();
//            DecimalFormatSymbols formatPemisah = new DecimalFormatSymbols();
//
//            formatPemisah.setCurrencySymbol("");
//            formatPemisah.setMonetaryDecimalSeparator(',');
//            formatPemisah.setGroupingSeparator('.');
//
//            pemisahRibuan.setDecimalFormatSymbols(formatPemisah);

            if(jsonStr == null) Log.e(TAG, "JSON null");

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("data");
                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String name = c.getString("Name");
                        String info = c.getString("Info");
                        informasi.add(new InfoModel(name, info));
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            // Dismiss the progress dialog
            if (InformasiTambahan.this.isDestroyed()){
                return;
            }
            dismissProgressDialog();

//            lv.setAdapter(new InfoAdapter(InformasiTambahan.this, R.layout.list_info_tambahan, informasi));

            InfoAdapter adapter = new InfoAdapter(getApplicationContext(), informasi);
            lv.setAdapter(adapter);
        }
    }
}