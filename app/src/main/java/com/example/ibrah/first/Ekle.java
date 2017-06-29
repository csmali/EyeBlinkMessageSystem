package com.example.ibrah.first;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Ekle extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int CAMERA_REQUEST = 10001;
    private ZXingScannerView mScannerView;
    private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekle);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        }
        server = new Server();
        mScannerView = new ZXingScannerView(this);
        try {
            setContentView(mScannerView);
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        } catch (Exception e) {
            Toast.makeText(this, "Camera initialization failed!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        mScannerView.stopCamera();
        super.onBackPressed();
    }

    @Override
    public void handleResult(Result result) {
        String[] knowledgeOfPatient = server.registerWithQR(result.getText());
        if (knowledgeOfPatient != null) {
            Toast.makeText(this, "Hasta Başarıyla Eklendi. ", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Hasta Eklemede Sorun Oluştu. Lütfen Geçerli Bir QR Kodu Okutunuz! ", Toast.LENGTH_LONG).show();
        }
        onBackPressed();
    }
}
