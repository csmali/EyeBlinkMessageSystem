package com.example.ibrah.first;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button sil, ekle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!isOnline()) {
            try {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("İnternet Bağlantı Hatası");
                alertDialogBuilder.setMessage("İnternet bağlantınız bulunmamaktadır.Uygulamanın doğru çalışabilmesi için internet bağlantınızı açınız.").setCancelable(false).setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } catch (Exception e) {
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sil = (Button) findViewById(R.id.HastaSilButton);
        ekle = (Button) findViewById(R.id.HastaEkleButton);

        sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent silIntent = new Intent(MainActivity.this, Sil.class);
                startActivity(silIntent);
            }
        });

        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ekleIntent = new Intent(MainActivity.this, Ekle.class);
                startActivity(ekleIntent);
            }
        });
    }

    private boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        return !(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable());
    }

}
