package id.ac.umn.magang01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PenjualanActivity extends AppCompatActivity {

    private String TAG = PenjualanActivity.class.getSimpleName();

    private ProgressDialog prog;
    private ListView lv;
    EditText etSearch;
    ImageView imgBack, imgRefresh, imgSearch, imgDate;

    ArrayList<PenjualanModel> penjualan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penjualan);

//        String thisYear = new SimpleDateFormat("yyyy").format(new Date());
//        String thisMonth = new SimpleDateFormat("MM").format(new Date());

//        Calendar cal = Calendar.getInstance();
//        int year = cal.get(Calendar.YEAR);
//        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
//        String thisYear = yearFormat.format(year);
//
//        int month = cal.get(Calendar.MONTH);

//        Setting.FROM_DATE = thisYear + "06";
//        Setting.TO_DATE = thisYear + thisMonth;
//        Setting.PER_BULAN = 0;

        lv = findViewById(R.id.listView);
        lv.setTextFilterEnabled(true);
        etSearch = findViewById(R.id.inputSearch);
        String cari = etSearch.getText().toString().trim();
        new GetContacts().execute(cari);

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParent();
                finish();
            }
        });

        imgDate = findViewById(R.id.imgDate);
        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dateChange = new Intent(getApplicationContext(), DatePickerActivity.class);
                startActivityForResult(dateChange, 1);
            }
        });

        imgRefresh = findViewById(R.id.imgRefresh);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });

        etSearch = findViewById(R.id.inputSearch);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    penjualan.clear();
                    String cari = etSearch.getText().toString().trim();
                    new GetContacts().execute(cari);
                    return true;
                }
                return false;
            }
        });

        imgSearch = findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                penjualan.clear();
                String cari = etSearch.getText().toString().trim();
                new GetContacts().execute(cari);
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent chartPenjualan = new Intent(PenjualanActivity.this, ChartPenjualan.class);
                String nama = penjualan.get(position).getName();
                chartPenjualan.putExtra("nama", nama);
                startActivity(chartPenjualan);
                return false;
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String[] choices = {"Informasi tambahan", "Grafik penjualan per bulan"};

                AlertDialog.Builder builder = new AlertDialog.Builder(PenjualanActivity.this);
                builder.setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            Intent infoTambahan = new Intent(PenjualanActivity.this, InformasiTambahan.class);
                            String code = penjualan.get(position).getID();
                            infoTambahan.putExtra("url", Setting.API_Informasi_Customer);
                            infoTambahan.putExtra("param", "?CustCode=");
                            infoTambahan.putExtra("code", code);
                            startActivity(infoTambahan);
                        }
                        if (which == 1){
                            Intent chartPenjualan = new Intent(PenjualanActivity.this, ChartPenjualan.class);
//                            String nama = penjualan.get(position).getName();
//                            chartPenjualan.putExtra("nama", nama);
                            Setting.SELECTED_NAME = penjualan.get(position).getName();
                            Setting.PER_BULAN = 1;
                            startActivity(chartPenjualan);
                        }
                    }
                });
                builder.show();
                return true;
            }
        });

//        if (Setting.DATE_CHANGED == 1){
//            refreshData();
//        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                refreshData();
            }
        }
    }


    private void refreshData() {
        startActivity(new Intent(PenjualanActivity.this, PenjualanActivity.class));
        finish();
    }


    private void  showProgressDialog(){
        if(prog == null){
            prog = new ProgressDialog(PenjualanActivity.this);
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

    private class GetContacts extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();

            Setting.PER_BULAN = 0;
        }

        @Override
        protected Void doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();
//            Log.d(TAG, "doInBackground: INI VALUE SP FROM DATE" + Setting.FROM_DATE + "SP_TODATE" + Setting.TO_DATE);
            String url = Setting.API_Penjualan_Dagang + "?FromTahunBulan=" + Setting.FROM_DATE + "&ToTahunBulan=" + Setting.TO_DATE + "&PerBulan=" + Setting.PER_BULAN;
            String jsonStr = sh.makeServiceCall(url);

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

                        String id = c.getString("CustCode");
                        String name = c.getString("FullName");
//                        String yrmonth = c.getString("TahunBulan");
                        int nilaiPenjualan = c.getInt("NilaiPenjualan");

                        String nilai = Setting.pemisahRibuan(nilaiPenjualan);

                        String search = strings[0];
                        if(search.isEmpty()){
                            penjualan.add(new PenjualanModel(id, name, Setting.pemisahRibuan(nilaiPenjualan).substring(0, nilai.length()-3)));
                        }
                        else if(name.contains(search.toUpperCase())) {
                            penjualan.add(new PenjualanModel(id, name, Setting.pemisahRibuan(nilaiPenjualan).substring(0, nilai.length()-3)));
                        }
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
            if (PenjualanActivity.this.isDestroyed()){
                return;
            }
            dismissProgressDialog();

            lv.setAdapter(new PenjualanAdapter(PenjualanActivity.this, R.layout.list_penjualan, penjualan));
        }
    }
}
