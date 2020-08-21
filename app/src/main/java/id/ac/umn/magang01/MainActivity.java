package id.ac.umn.magang01;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

//import com.google.firebase.auth.FirebaseAuth;

import com.pixplicity.easyprefs.library.Prefs;

import static id.ac.umn.magang01.Setting.SP_TOKEN;

public class MainActivity extends AppCompatActivity {
    LinearLayout llPenjualan, llPembelian, llPiutang, llHutang;
    ImageView btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        recyclerView = findViewById(R.id.recyclerView);

//        MainRecyclerAdapter adapter = new MainRecyclerAdapter(this);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnLogout = findViewById(R.id.imgLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, btnLogout);
                popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        Prefs.remove(SP_TOKEN);
                        finish();
                        return true;
                    }
                });
                popup.show();
            }
        });

        llPenjualan = findViewById(R.id.layoutPenjualan);
        llPenjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PenjualanActivity.class));
            }
        });

        llPembelian = findViewById(R.id.layoutPembelian);
        llPembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PembelianActivity.class));
            }
        });

        llPiutang = findViewById(R.id.layoutPiutang);
        llPiutang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PiutangActivity.class));
            }
        });

        llHutang = findViewById(R.id.layoutHutang);
        llHutang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HutangActivity.class));
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.logOut) {
////            FirebaseAuth.getInstance().signOut();
//            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
