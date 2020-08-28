package id.ac.umn.magang01;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

public class DatePickerActivity extends AppCompatActivity {

    private static final String TAG = "Date Picker";

    private TextView fromDate, toDate;
    private Button btnSendDate;
    private DatePickerDialog.OnDateSetListener fromDateListener;
    private DatePickerDialog.OnDateSetListener toDateListener;
    String periodeAwal, periodeAkhir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        fromDate = findViewById(R.id.fromDate);
//        fromDate.setText(Setting.FROM_DATE);
//        if (Setting.DISPLAY_FROM_DATE != "")
        fromDate.setText(Setting.DISPLAY_FROM_DATE);

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DatePickerActivity.this,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                fromDateListener,
                                year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().findViewById(getResources().getIdentifier("day","id","android")).setVisibility(View.GONE);
                dialog.show();
            }
        });
        fromDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date;
                String sendDate;

                if (month < 10){
                    date = "0" + month + "/" + year;
                    sendDate = year + "0" + month;
                }
                else{
                    date = month + "/" + year;
                    sendDate = year + String.valueOf(month);
                }

                Setting.FROM_DATE = sendDate;

//                DateFormat inFormat = new SimpleDateFormat("yyyyMM");
//                DateFormat outFormat = new SimpleDateFormat("MMM yyyy");
//                try {
//                    Date dateFormat = inFormat.parse(Setting.FROM_DATE);
//                    periodeAwal = outFormat.format(dateFormat);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

                Setting.DISPLAY_FROM_DATE = date;
                fromDate.setText(Setting.DISPLAY_FROM_DATE);
            }
        };

        toDate = findViewById(R.id.toDate);
//        toDate.setText(Setting.TO_DATE);
//        if (Setting.DISPLAY_TO_DATE != "")
        toDate.setText(Setting.DISPLAY_TO_DATE);

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        DatePickerActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        toDateListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getDatePicker().findViewById(getResources().getIdentifier("day","id","android")).setVisibility(View.GONE);
                dialog.show();
            }
        });
        toDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date;
                String sendDate;

                if (month < 10){
                    date = "0" + month + "/" + year;
                    sendDate = year + "0" + month;
                }
                else{
                    date = month + "/" + year;
                    sendDate = year + String.valueOf(month);
                }
                Setting.TO_DATE = sendDate;

//                DateFormat inFormat = new SimpleDateFormat("yyyyMM");
//                DateFormat outFormat = new SimpleDateFormat("MMM yyyy");
//                try {
//                    Date dateFormat = inFormat.parse(Setting.TO_DATE);
//                    periodeAkhir = outFormat.format(dateFormat);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

//                toDate.setText(date);

                Setting.DISPLAY_TO_DATE = date;
                toDate.setText(Setting.DISPLAY_TO_DATE);
            }
        };

        btnSendDate = findViewById(R.id.SendDateBtn);
        btnSendDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(TAG, "INI VALUE FROM : " + Setting.FROM_DATE + " TO : " + Setting.TO_DATE);
//                Toast.makeText(DatePickerActivity.this, "sent", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getApplicationContext(), PenjualanActivity.class));
//                finish();
                DateFormat inFormat = new SimpleDateFormat("yyyyMM");
                DateFormat outFormat = new SimpleDateFormat("MMM yyyy");
                try {
                    Date dateFormat = inFormat.parse(Setting.FROM_DATE);
                    periodeAwal = outFormat.format(dateFormat);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    Date dateFormat = inFormat.parse(Setting.TO_DATE);
                    periodeAkhir = outFormat.format(dateFormat);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Setting.DISPLAY_PERIODE = "Periode " + periodeAwal + " - " + periodeAkhir;
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}