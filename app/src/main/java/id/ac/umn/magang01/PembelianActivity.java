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

public class PembelianActivity extends AppCompatActivity {

    private String TAG = PembelianActivity.class.getSimpleName();

    private ProgressDialog prog;
    private ListView lv;
    EditText etSearch;
    ImageView imgBack, imgRefresh, imgSearch, imgDate;

    ArrayList<PembelianModel> pembelian = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembelian);

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
                    pembelian.clear();
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
                pembelian.clear();
                String cari = etSearch.getText().toString().trim();
                new GetContacts().execute(cari);
            }
        });

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent chartPembelian = new Intent(PembelianActivity.this, ChartPembelian.class);
//                String nama = pembelian.get(position).getName();
//                chartPembelian.putExtra("nama", nama);
////                Toast.makeText(PembelianActivity.this, nama, Toast.LENGTH_SHORT).show();
//                startActivity(chartPembelian);
//            }
//        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String[] choices = {"Informasi tambahan", "Grafik pembelian per bulan"};

                AlertDialog.Builder builder = new AlertDialog.Builder(PembelianActivity.this);
                builder.setItems(choices, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            Intent infoTambahan = new Intent(PembelianActivity.this, InformasiTambahan.class);
                            String code = pembelian.get(position).getID();
                            infoTambahan.putExtra("url", Setting.API_Informasi_Supplier);
                            infoTambahan.putExtra("param", "?SuppCode=");
                            infoTambahan.putExtra("code", code);
                            startActivity(infoTambahan);
                        }
                        if (which == 1){
                            Intent chartPembelian = new Intent(PembelianActivity.this, ChartPembelian.class);
                            Setting.SELECTED_NAME = pembelian.get(position).getName();
                            Setting.PER_BULAN = 1;
                            startActivity(chartPembelian);
                        }
                    }
                });
                builder.show();
                return true;
            }
        });
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
        startActivity(new Intent(PembelianActivity.this, PembelianActivity.class));
        finish();
    }

    private void  showProgressDialog(){
        if(prog == null){
            prog = new ProgressDialog(PembelianActivity.this);
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

            String url = Setting.API_Pembelian_Dagang + "?FromTahunBulan=" + Setting.FROM_DATE + "&ToTahunBulan=" + Setting.TO_DATE + "&PerBulan=" + Setting.PER_BULAN;
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

                        String id = c.getString("SuppCode");
                        String name = c.getString("FullName");
                        int nilaiPembelian = c.getInt("NilaiPembelian");

                        String nilai = Setting.pemisahRibuan(nilaiPembelian);

                        String search = strings[0];
                        if(search.isEmpty()){
                            pembelian.add(new PembelianModel(id, name, Setting.pemisahRibuan(nilaiPembelian).substring(0, nilai.length()-3)));
                        }
                        else if(name.contains(search.toUpperCase())) {
                            pembelian.add(new PembelianModel(id, name, Setting.pemisahRibuan(nilaiPembelian).substring(0, nilai.length()-3)));
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
            if (PembelianActivity.this.isDestroyed()){
                return;
            }
            dismissProgressDialog();

            lv.setAdapter(new PembelianAdapter(PembelianActivity.this, R.layout.list_pembelian, pembelian));
        }
    }
}
