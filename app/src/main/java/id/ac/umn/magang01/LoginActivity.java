package id.ac.umn.magang01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
//import android.support.design.widget.TextInputEditText;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import id.ac.umn.magang01.MainActivity;
import id.ac.umn.magang01.R;
import id.ac.umn.magang01.Setting;
import id.ac.umn.magang01.models.login.PLogin;

import com.google.android.material.textfield.TextInputEditText;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

import static id.ac.umn.magang01.Setting.SP_TOKEN;
import static id.ac.umn.magang01.Setting.SP_USER;

public class LoginActivity extends AppCompatActivity {
    public final static int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    Context c;
    Button btnLogin;

    TextInputEditText etUserId, etPassword;
    TextView forgotPwd;
    ProgressDialog pd;
    String username,password;
    String nik;
    ImageView ivClear1, ivClear2;
    CheckBox chkShowHide;
    String imei="";
    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setContentView(R.layout.activity_login);

        etUserId = findViewById(R.id.inputUsername);
        etPassword = findViewById(R.id.inputPass);

        ivClear1 = findViewById(R.id.ivClear);
        ivClear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etUserId.setText("");
            }
        });

        ivClear2 = findViewById(R.id.ivClear2);
        ivClear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etPassword.setText("");
            }
        });
        chkShowHide = findViewById(R.id.chkShowHide);
        chkShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkShowHide.setActivated(!chkShowHide.isActivated());
                showHide();
            }
        });
        etUserId.setText("");
        etPassword.setText("");
        c=this;
        loadIMEI();
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prosesLogin(v);
            }
        });

        forgotPwd = findViewById(R.id.textForgotPwd);
        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPasswordbyEmail(v);
            }
        });

    }
    public void showHide(){
        if(chkShowHide.isChecked())
        {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        else
        {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

//    public void forgotPasswordbySMS(View view) {
//        nik = etUserId.getText().toString();
//
//        etUserId.setText(nik);
//
//        if(!nik.isEmpty()) {
//            Prefs.putString(SP_USER,nik);
//            startActivity(new Intent(this, ForgotSMSActivity.class));
//        }else {
//            Toast.makeText(this, "Silahkan masukan username / nik", Toast.LENGTH_SHORT).show();
//        }
//    }

    public void forgotPasswordbyEmail(View view) {
        nik = etUserId.getText().toString();
        etUserId.setText(nik);

        if(!nik.isEmpty()) {
            Prefs.putString(SP_USER,nik);
            startActivity(new Intent(this, ForgotEmailActivity.class));
        }else {
            Toast.makeText(this, "Silahkan masukan username / nik", Toast.LENGTH_SHORT).show();
        }

    }

    public void prosesLogin(View view) {
        username = etUserId.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (username.length() == 0) {
            etUserId.setError(getString(R.string.username_required));
            etUserId.requestFocus();
            return;
        }
        if (password.length() == 0) {
            etPassword.setError(getString(R.string.password_required));
            etPassword.requestFocus();
            return;
        }

        pd = new ProgressDialog(this);
        pd.setTitle(getString(R.string.processing));
        pd.setMessage(getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();
//        checkimei();
    }
//    private void checkimei() {
//        AndroidNetworking.post(Setting.API_Check_Imei)
//                .addHeaders("Content-Type","application/json")
//                .addBodyParameter("empcode",username)
//                .addBodyParameter("imei", imei)
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsObject(GImei.class, new ParsedRequestListener<GImei>() {
//                    @Override
//                    public void onResponse(GImei r) {
//                        if (r.getData().size() > 0) {
//                            Log.d("cek data imei",r.getData().get(0).getCheckImei());
//                            if ((r.getData().get(0).getCheckImei()).equals("1")) {
//
//                                if (Build.VERSION.SDK_INT >= 23) {
//                                    if(checkPermissions()){
//                                        initLogin();
//                                    }else {
//                                        checkPermissions();
//                                    }
//                                }else {
//                                    initLogin();
//                                }
//
//                            }else{
//                                pd.dismiss();
//
//                                Toast.makeText(LoginActivity.this, r.getData().get(0).getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        pd.dismiss();
//
//                        Toast.makeText(LoginActivity.this, anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
//                        Log.d("cekUrl", "onError errorCode : " + anError.getErrorCode());
//                        Log.d("cekUrl", "onError errorBody : " + anError.getErrorBody());
//                        Log.d("cekUrl", "onError errorDetail : " + anError.getErrorDetail());
//
//                    }
//                });
//    }

    private void initLogin() {
        AndroidNetworking.post(Setting.API_Login)
                .addHeaders("Content-Type","application/json")
                .addBodyParameter("username",username)
                .addBodyParameter("password", password)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(PLogin.class, new ParsedRequestListener<PLogin>() {
                    @Override
                    public void onResponse(PLogin r) {
                        pd.dismiss();
                        if(r.getStatus().equals("ok")){
                            Prefs.putString(SP_USER, username);
                            Prefs.putString(SP_TOKEN, r.getToken());
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, r.getStatus(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        if (anError.getErrorCode()==403){
                            Toast.makeText(LoginActivity.this, "Password atau NIK salah", Toast.LENGTH_SHORT).show();
                            Log.d("cekUrl", "onError errorCode : " + anError.getErrorCode());
                            Log.d("cekUrl", "onError errorBody : " + anError.getErrorBody());
                            Log.d("cekUrl", "onError errorDetail : " + anError.getErrorDetail());
                        }
                        else {
                            Toast.makeText(LoginActivity.this, anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                            Log.d("cekUrl", "onError errorCode : " + anError.getErrorCode());
                            Log.d("cekUrl", "onError errorBody : " + anError.getErrorBody());
                            Log.d("cekUrl", "onError errorDetail : " + anError.getErrorDetail());
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
                initLogin();
            }
            return;
        }
    }
    public void loadIMEI() {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has not been granted.
            requestReadPhoneStatePermission();
        } else {
            // READ_PHONE_STATE permission is already been granted.
//            doPermissionGrantedStuffs();
        }
    }
    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            new AlertDialog.Builder(c)
                    .setTitle("Permission Request")
                    .setMessage("Allow permission to read device id")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(LoginActivity.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                    })
                    .setIcon(R.mipmap.ic_launcher)
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    private void alertAlert(String msg) {
        new AlertDialog.Builder(c)
                .setTitle("Permission Request")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do somthing here
                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

    public void doPermissionGrantedStuffs() {
        //Have an  object of TelephonyManager
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Get IMEI Number of Phone  //////////////// for this example i only need the IMEI
//        String IMEINumber=tm.getDeviceId();

        /************************************************
         * **********************************************
         *
         //Get Subscriber ID
         String subscriberID=tm.getDeviceId();

         //Get SIM Serial Number
         String SIMSerialNumber=tm.getSimSerialNumber();

         //Get Network Country ISO Code
         String networkCountryISO=tm.getNetworkCountryIso();

         //Get SIM Country ISO Code
         String SIMCountryISO=tm.getSimCountryIso();

         //Get the device software version
         String softwareVersion=tm.getDeviceSoftwareVersion()

         //Get the Voice mail number
         String voiceMailNumber=tm.getVoiceMailNumber();


         //Get the Phone Type CDMA/GSM/NONE
         int phoneType=tm.getPhoneType();

         switch (phoneType)
         {
         case (TelephonyManager.PHONE_TYPE_CDMA):
         // your code
         break;
         case (TelephonyManager.PHONE_TYPE_GSM)
         // your code
         break;
         case (TelephonyManager.PHONE_TYPE_NONE):
         // your code
         break;
         }

         //Find whether the Phone is in Roaming, returns true if in roaming
         boolean isRoaming=tm.isNetworkRoaming();
         if(isRoaming)
         phoneDetails+="\nIs In Roaming : "+"YES";
         else
         phoneDetails+="\nIs In Roaming : "+"NO";


         //Get the SIM state
         int SIMState=tm.getSimState();
         switch(SIMState)
         {
         case TelephonyManager.SIM_STATE_ABSENT :
         // your code
         break;
         case TelephonyManager.SIM_STATE_NETWORK_LOCKED :
         // your code
         break;
         case TelephonyManager.SIM_STATE_PIN_REQUIRED :
         // your code
         break;
         case TelephonyManager.SIM_STATE_PUK_REQUIRED :
         // your code
         break;
         case TelephonyManager.SIM_STATE_READY :
         // your code
         break;
         case TelephonyManager.SIM_STATE_UNKNOWN :
         // your code
         break;

         }
         */

        String deviceUniqueIdentifier = null;
        if (null != tm) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestReadPhoneStatePermission();
            }
//            deviceUniqueIdentifier = tm.getDeviceId();
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        imei=deviceUniqueIdentifier;
        Log.d("imei =",imei);
    }
}
