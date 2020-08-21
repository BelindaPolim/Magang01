package id.ac.umn.magang01;

import android.app.Application;
import android.content.ContextWrapper;

import com.androidnetworking.AndroidNetworking;
import com.pixplicity.easyprefs.library.Prefs;

public class MyApp extends Application {
    private static MyApp c;

    public MyApp getInstance(){
        return c;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        c = this;
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.enableLogging();
//        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//        StrictMode.setVmPolicy(builder.build());
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }
}
