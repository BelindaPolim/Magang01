package id.ac.umn.magang01;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;

        import android.Manifest;
        import android.annotation.SuppressLint;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.os.Build;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.androidnetworking.AndroidNetworking;
        import com.androidnetworking.common.Priority;
        import com.androidnetworking.error.ANError;
        import com.androidnetworking.interfaces.ParsedRequestListener;
        import com.google.android.material.textfield.TextInputLayout;
        import com.pixplicity.easyprefs.library.Prefs;

        import java.util.ArrayList;
        import java.util.List;

        import id.ac.umn.magang01.models.forgotPassword.GForgotPassword;
        import id.ac.umn.magang01.profile.GEmployees;

        import static id.ac.umn.magang01.Setting.SP_USER;

public class ForgotEmailActivity extends AppCompatActivity {

    TextInputLayout emailLayout;
    //    TextInputEditText teEmail;
    TextView tvEmail;
    ProgressDialog pd;
    String email;
    ImageView btnBack;
    Button btnSendEmail;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_email);
        c = this;
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
        btnBack = findViewById(R.id.imgBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParent();
                finish();
            }
        });
//        emailLayout = findViewById(R.id.email);
//        teEmail = findViewById(R.id.teEmail);
        tvEmail = findViewById(R.id.tvEmail);
//        tvEmail = findViewById(R.id.etEmail);
        ShowProfileFragment();

    }

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

    public void sendForgot(View view) {
//        email = emailLayout.getEditText().getText().toString().trim();
        email = tvEmail.getText().toString().trim();

        if (email.length() == 0) {
//            emailLayout.getEditText().setError(getString(R.string.username_required));
//            emailLayout.getEditText().requestFocus();
            return;
        }

        pd = new ProgressDialog(this);
        pd.setTitle(getString(R.string.processing));
        pd.setMessage(getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();
//        if (Build.VERSION.SDK_INT >= 23) {
//            if(checkPermissions()){
//                sendData();
//            }else {
//                checkPermissions();
//            }
//        }else {
//            sendData();
//        }
        sendData();
    }

    private void sendData() {
        //kirim data ke API
//        Log.d("cekUrlEmail",Setting.API_Email + "?email=" + email);
        AndroidNetworking.get(Setting.API_Email + "?empcode=" + Prefs.getString(SP_USER,"") + "&email=" + email)
//                    .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(GForgotPassword.class, new ParsedRequestListener<GForgotPassword>() {
                    @Override
                    public void onResponse(GForgotPassword response) {
                        pd.dismiss();
                        String msg = response.getMessage();
                        if(response.getCode().equals("ok")){
                            Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(c, LoginActivity.class));
                            finish();
                        }else {
                            Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pd.dismiss();
                        Toast.makeText(c, anError.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        Log.d("cekUrl", "onError errorCode : " + anError.getErrorCode());
                        Log.d("cekUrl", "onError errorBody : " + anError.getErrorBody());
                        Log.d("cekUrl", "onError errorDetail : " + anError.getErrorDetail());
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
                sendData();
            }
            return;
        }
    }

    private void ShowProfileFragment() {
        // Required empty public constructor
        btnSendEmail = findViewById(R.id.btnSendEmail);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendForgot(v);
            }
        });

        final ProgressDialog pd = new ProgressDialog(c);
        pd.setTitle(getString(R.string.processing));
        pd.setMessage(getString(R.string.please_wait));
        pd.setCancelable(false);
        pd.show();

        AndroidNetworking.get(Setting.API_Profile_Forgot + "?empcode="+Prefs.getString(SP_USER,""))
//                .addHeaders("Authorization","Bearer "+Prefs.getString(SP_TOKEN,""))
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(GEmployees.class, new ParsedRequestListener<GEmployees>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onResponse(GEmployees response) {
                        pd.dismiss();
                        if(response.getData().size()>0) {
//                            teEmail.setText(response.getData().get(0).getEmail());
                            tvEmail.setText(response.getData().get(0).getEmail());
                        }else {
                            Toast.makeText(c, "Mohon maaf NIK tidak terdaftar", Toast.LENGTH_SHORT).show();
                            ForgotEmailActivity.super.onBackPressed();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        Toast.makeText(c, error.getErrorDetail(), Toast.LENGTH_SHORT).show();
                        Log.d("cekUrl", "onError errorCode : " + error.getErrorCode());
                        Log.d("cekUrl", "onError errorBody : " + error.getErrorBody());
                        Log.d("cekUrl", "onError errorDetail : " + error.getErrorDetail());
                        ForgotEmailActivity.super.onBackPressed();
                    }
                });

    }
}
