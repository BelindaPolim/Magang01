package id.ac.umn.magang01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChartPenjualan extends AppCompatActivity {

    private String TAG = ChartPenjualan.class.getSimpleName();

    private ProgressDialog prog;
    ImageView imgBack;
    TextView namaCust;
    String label;

    ArrayList<BarEntry> penjualan = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_penjualan);

        imgBack = findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParent();
                finish();
            }
        });

        Intent getPosition = getIntent();
        String name = getPosition.getStringExtra("nama");
        new GetContacts().execute(name);

        namaCust = findViewById(R.id.custName);
        namaCust.setText(name);
    }

    public class LabelFormatter implements IAxisValueFormatter {
        //        private final String[] mLabels;
//        private final String[] mLabels;
//
//        public LabelFormatter(String[] labels) {
//            mLabels = labels;
//        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            Log.d(TAG, "getFormattedValue: " + value);
            String val = String.valueOf(value);

            DateFormat inputFormat = new SimpleDateFormat("yyyyMM");
            DateFormat outputFormat = new SimpleDateFormat("MMM yyyy");
            try {
                Date date = inputFormat.parse(val);
                label = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return label;
        }
    }

    private void callBarChart(){
        float barWidth = 0.45f;

        BarChart barChart = findViewById(R.id.chart);

        BarDataSet barDataSet = new BarDataSet(penjualan, "Nilai penjualan per bulan");
        String[] labels = {};

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColor(ColorTemplate.MATERIAL_COLORS[1]);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        barChart.getXAxis().setValueFormatter(new LabelFormatter());

        // Pengaturan sumbu X
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(false);

        // Agar ketika di zoom tidak menjadi pecahan
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);

        //Menghilangkan sumbu Y yang ada di sebelah kanan
        barChart.getAxisRight().setEnabled(false);

        // Menghilankan deskripsi pada Chart
        barChart.getDescription().setEnabled(false);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getBarData().setBarWidth(barWidth);
        barChart.animateY(2000);
        barChart.setDragEnabled(true);
        barChart.setPinchZoom(true);
        barChart.invalidate();
    }

    private void  showProgressDialog(){
        if(prog == null){
            prog = new ProgressDialog(ChartPenjualan.this);
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

            Setting.PER_BULAN = 1;
        }

        @Override
        protected Void doInBackground(String... strings) {
            HttpHandler sh = new HttpHandler();

            String url = Setting.API_Penjualan_Dagang + "?FromTahunBulan=" + Setting.FROM_DATE + "&ToTahunBulan=" + Setting.TO_DATE + "&PerBulan=" + Setting.PER_BULAN;
            String jsonStr = sh.makeServiceCall(url);

            DecimalFormat pemisahRibuan = (DecimalFormat) DecimalFormat.getCurrencyInstance();
            DecimalFormatSymbols formatPemisah = new DecimalFormatSymbols();

            formatPemisah.setCurrencySymbol("");
            formatPemisah.setMonetaryDecimalSeparator(',');
            formatPemisah.setGroupingSeparator('.');

            pemisahRibuan.setDecimalFormatSymbols(formatPemisah);

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
                        String yrMonth = c.getString("TahunBulan");
                        long nilaiPenjualan = c.getLong("NilaiPenjualan");

                        String search = strings[0];

                        if(name.equals(search.toUpperCase())){
                            penjualan.add(new BarEntry(Float.parseFloat(yrMonth), nilaiPenjualan));
                        }
//                        else if(name.contains(search.toUpperCase())) {
//                            penjualan.add(new PenjualanModel(id, name, pemisahRibuan.format(nilaiPenjualan).substring(0, nilai.length()-3)));
//                        }
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
            if (ChartPenjualan.this.isDestroyed()){
                return;
            }
            dismissProgressDialog();

            callBarChart();
        }
    }
}
