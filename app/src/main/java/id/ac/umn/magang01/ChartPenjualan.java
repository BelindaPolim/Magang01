package id.ac.umn.magang01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChartPenjualan extends AppCompatActivity {

    private String TAG = ChartPenjualan.class.getSimpleName();

    private ProgressDialog prog;
    ImageView imgBack, imgDate;
    TextView namaCust, periode;
    String label;

//    ArrayList<BarEntry> penjualan = new ArrayList<>();
    ArrayList<BarEntry> valPenjualan = new ArrayList<>();
    ArrayList<String> tglPenjualan = new ArrayList<String>();

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

        imgDate = findViewById(R.id.imgDate);
        imgDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dateChange = new Intent(getApplicationContext(), DatePickerActivity.class);
                startActivityForResult(dateChange, 1);
            }
        });

//        Intent getPosition = getIntent();
//        String name = getPosition.getStringExtra("nama");
        String name = Setting.SELECTED_NAME;
        new GetContacts().execute(name);

        namaCust = findViewById(R.id.custName);
        namaCust.setText(name);

        periode = findViewById(R.id.periode);
        periode.setText(Setting.DISPLAY_PERIODE);
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
        startActivity(new Intent(ChartPenjualan.this, ChartPenjualan.class));
        finish();
    }

    public class LabelFormatter implements IAxisValueFormatter {

//        private final ArrayList<String> mLabels;
//        public LabelFormatter(ArrayList labels) {
//            mLabels = labels;
//        }


        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            Log.d(TAG, "getFormattedValue: " + value);
//            String val = String.valueOf(value);
//            String val = tglPenjualan.get((int)value);

            String val = tglPenjualan.get((int)value);

            DateFormat inputFormat = new SimpleDateFormat("yyyyMM");
            DateFormat outputFormat = new SimpleDateFormat("MMM");
            try {
                Date date = inputFormat.parse(val);
                label = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            return label;
        }
    }

    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)

            String nilai = Setting.pemisahRibuan(value);

            return Setting.pemisahRibuan(value).substring(0, nilai.length()-3);
        }
    }

    public class MyValueFormatter implements IValueFormatter {

//        public MyValueFormatter() {
//            mFormat = new DecimalFormat("###.###.###"); // use one decimal
//        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

            String nilai = Setting.pemisahRibuan(value);

            return Setting.pemisahRibuan(value).substring(0, nilai.length()-3);        }
    }


//    public class IntValueFormatter implements IValueFormatter {
//        @Override
//        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//            return String.valueOf((int) value);
//        }
//    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // ignore orientation/keyboard change
        super.onConfigurationChanged(newConfig);
    }

    private void callBarChart(){
        BarChart barChart = findViewById(R.id.chart);

//        Log.d(TAG, "callBarChart: test isi penjualan" + valPenjualan );
        BarDataSet barDataSet = new BarDataSet(valPenjualan, "Nilai penjualan per bulan");

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColor(ColorTemplate.MATERIAL_COLORS[1]);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(9f);
        barDataSet.setValueFormatter(new MyValueFormatter());
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
        yAxis.setValueFormatter(new MyYAxisValueFormatter());

        //Menghilangkan sumbu Y yang ada di sebelah kanan
        barChart.getAxisRight().setEnabled(false);
        // Menghilankan deskripsi pada Chart
        barChart.getDescription().setEnabled(false);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getBarData().setBarWidth(0.8f);
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

            if(jsonStr == null) Log.e(TAG, "JSON null");

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("data");
                    // looping through All Contacts
                    int count = 0;
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("CustCode");
                        String name = c.getString("FullName");
                        String yrMonth = c.getString("TahunBulan");
                        int nilaiPenjualan = c.getInt("NilaiPenjualan");

                        String search = strings[0];

                        if(name.equals(search.toUpperCase())){
                            tglPenjualan.add(yrMonth);
                            valPenjualan.add(new BarEntry(count, nilaiPenjualan));
                            count++;
//                            valPenjualan.add(new BarEntry(nilaiPenjualan));
//                            tglPenjualan.add(new BarEntry(Float.parseFloat(yrMonth));

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
