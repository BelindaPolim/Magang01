package id.ac.umn.magang01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

public class PiutangActivity extends AppCompatActivity {

    private String TAG = PiutangActivity.class.getSimpleName();

    private ProgressDialog prog;
    private ListView lv;
    EditText search;

    private static String url = "http://119.235.208.235:8092/piutang_dagang_per_customer";

    private ArrayList<HashMap<String, String>> contactlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piutang);


        contactlist = new ArrayList<>();
        lv = findViewById(R.id.listView);
        lv.setTextFilterEnabled(true);

        new GetContacts().execute();

        search = findViewById(R.id.inputSearch);

    }

    private class GetContacts extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            prog = new ProgressDialog(PiutangActivity.this);
            prog.setMessage("Fetching data...");
            prog.setCancelable(false);
            prog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
//            Locale localeID = new Locale("in", "ID");
//            NumberFormat formatRP = NumberFormat.getCurrencyInstance(localeID);

            DecimalFormat pemisahRibuan = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatPemisah = new DecimalFormatSymbols();

            formatPemisah.setCurrencySymbol("");
            formatPemisah.setMonetaryDecimalSeparator(',');
            formatPemisah.setGroupingSeparator('.');

            pemisahRibuan.setDecimalFormatSymbols(formatPemisah);

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);
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
                        int sisaPiutang = c.getInt("SisaPiutang");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("sisaPiutang", pemisahRibuan.format((double)sisaPiutang));

                        // adding contact to contact list
                        contactlist.add(contact);
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
            if (prog.isShowing())
                prog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    PiutangActivity.this, contactlist,
                    R.layout.list_piutang, new String[]{"ID", "name",
                    "sisaPiutang"}, new int[]{R.id.custID,
                    R.id.custName, R.id.sisaPiutang});
            lv.setAdapter(adapter);
        }
    }
}
