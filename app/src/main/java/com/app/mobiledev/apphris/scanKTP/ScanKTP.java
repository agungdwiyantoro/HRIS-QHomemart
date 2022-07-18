package com.app.mobiledev.apphris.scanKTP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.app.mobiledev.apphris.R;


public class ScanKTP extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ktp);
        setTitle(R.string.update_data_diri);
        Button mulaiScanning = findViewById(R.id.bt_scan);
        mulaiScanning.setOnClickListener(v -> {
           Intent scanID = new Intent(ScanKTP.this, MainActivitySplashScreen.class);
           startActivity(scanID);
        });
    }
}