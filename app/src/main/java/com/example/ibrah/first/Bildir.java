package com.example.ibrah.first;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Bildir extends AppCompatActivity {

    ListView listView;
    final Context context = this;
    Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bildir);
        server = new Server();
        HashMap<String, String> serverdanGelenBildirimler = new HashMap<>();//id -> mesaj
        final ArrayList<String> bildirimler = new ArrayList<String>();
        final ArrayList<String> idler = new ArrayList<String>();
        Bundle extras = getIntent().getExtras();
        String qr = extras.getString("HastaQR");
        final String qrTemp = qr;
        serverdanGelenBildirimler = server.getNotifications(qr);
        if (serverdanGelenBildirimler == null) {
            Toast.makeText(this, "Server Bağlantısında Bir Hata Oluştu. ", Toast.LENGTH_LONG).show();
        } else {
            for (String key : serverdanGelenBildirimler.keySet()) {
                bildirimler.add(serverdanGelenBildirimler.get(key));
                idler.add(key);
            }
            TextView textView = new TextView(context);
            textView.setText("Hastanın ihtiyacını karşıladığınızı bildirmek için bildirime tıklayınız!");
            listView = (ListView) findViewById(R.id.bildirimListView);
            listView.addHeaderView(textView);
            final ArrayAdapter<String> adaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, bildirimler);
            listView.setAdapter(adaptor);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Bildirim Onaylama");
                    alertDialogBuilder.setMessage("Seçili bildirimi yaptığınızı sunucuya bildirmek istediğinize emin misiniz?").setCancelable(false).setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            server.confirmNotification(idler.get(position), qrTemp);
                            bildirimler.remove(position);
                            idler.remove(position);
                            adaptor.notifyDataSetChanged();
                        }
                    }).setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        }
    }
}
