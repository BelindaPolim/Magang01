package id.ac.umn.magang01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PiutangActivity extends AppCompatActivity {

    private String TAG = PiutangActivity.class.getSimpleName();

    private ProgressDialog prog;
    private ListView lv;
    EditText search;

    private static String url = "http://119.235.208.235:8092/piutang_dagang_per_customer";

    private ArrayList<HashMap<String, String>> contactlist;
    private PiutangAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piutang);


        contactlist = new ArrayList<>();
        lv = findViewById(R.id.listView);
        lv.setTextFilterEnabled(true);

        new GetContacts().execute();

        search = findViewById(R.id.inputSearch);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                lv.getFilter().filter(s);
//                ma.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    private void filter(String text){
        ArrayList<HashMap<String, String>> filteredlist = new ArrayList<>();

        for(HashMap<String, String> item : contactlist){
//            if(item.get)
        }
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
//                        String email = c.getString("email");
//                        String address = c.getString("address");
//                        String gender = c.getString("gender");

                        // Phone node is JSON Object
//                        JSONObject phone = c.getJSONObject("phone");
                        String sisaPiutang = c.getString("SisaPiutang");
//                        String home = phone.getString("home");
//                        String office = phone.getString("office");

                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("mobile", sisaPiutang);

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
                    "mobile"}, new int[]{R.id.custID,
                    R.id.custName, R.id.sisaPiutang});

            lv.setAdapter(adapter);
        }
    }
}
