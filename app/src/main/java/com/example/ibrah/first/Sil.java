package com.example.ibrah.first;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.List;

public class Sil extends AppCompatActivity {

    private static final String TAG = "Sil";
    ListView listView;
    final Context context = this;
    HashMap<String, String> serverdanGelenHastalar;
    Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sil);
        server = new Server();
        serverdanGelenHastalar = server.getPatients();//ad -> qr
        if (serverdanGelenHastalar == null) {
            Toast.makeText(this, "Server Bağlantısında Bir Hata Oluştu. ", Toast.LENGTH_LONG).show();
        } else {
            final List<String> hastaAdlari = new ArrayList<String>();
            final List<String> hastaQrlari = new ArrayList<String>();
            if (serverdanGelenHastalar.size() == 0) {
                hastaAdlari.add("Hastanız Bulunmamaktadır.");
            } else {
                for (String key : serverdanGelenHastalar.keySet()) {
                    hastaQrlari.add(serverdanGelenHastalar.get(key));
                    hastaAdlari.add(key);
                }
            }
            TextView textView = new TextView(context);
            textView.setText("İşlem yapmak istediğiniz hastanın bildirimlerini görmek için kısa, hastayı silmek için uzun basınız!");
            listView = (ListView) findViewById(R.id.silListView);
            listView.addHeaderView(textView);
            final ArrayAdapter<String> veriAdaptoru = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, hastaAdlari);
            listView.setAdapter(veriAdaptoru);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {

                    String qr = hastaQrlari.get(position);
                    Intent bildirimIntent = new Intent(Sil.this, Bildir.class);
                    bildirimIntent.putExtra("HastaQR", qr);
                    startActivity(bildirimIntent);
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setTitle("Hasta Sil");
                    alertDialogBuilder.setMessage("Hastayı silmek istediğinize emin misiniz?").setCancelable(false).setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            boolean b = server.removeQR(hastaQrlari.get(position));
                            hastaAdlari.remove(position);
                            hastaQrlari.remove(position);
                            veriAdaptoru.notifyDataSetChanged();
                            if (b) {
                                Toast.makeText(parent.getContext(), "Hasta Başarıyla Silinmiştir. ", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(parent.getContext(), "Hasta Silmede Hata Oluşmuştur. ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return true;
                }
            });
        }
    }

}
