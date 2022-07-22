package com.app.mobiledev.apphris.scanKTP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.mobiledev.apphris.R;
import com.bumptech.glide.Glide;

public class UpdateDataDiriStep3 extends AppCompatActivity {

    private final String TAG = "UpdateDataDiriStep3";
    private EditText etNPWP;
    private Button btLihatDetail;
    private ImageView ivFotoKTP;

    Intent changeClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data_diri_step3);
        etNPWP = findViewById(R.id.et_npwp);
        ivFotoKTP = findViewById(R.id.iv_foto_ktp);
        btLihatDetail = findViewById(R.id.bt_lihat_detail);


        SharedPreferencesUpdateDataDiri sharedUpdateDataDiri = new SharedPreferencesUpdateDataDiri(UpdateDataDiriStep3.this);

        SharedPreferences sharedPreference = getSharedPreferences(sharedUpdateDataDiri.SHARED_PREFERENCE, MODE_PRIVATE);

        String URI = sharedPreference.getString(sharedUpdateDataDiri.IMAGE_URI, "");

        Log.d(TAG, "Image URI : " + URI);
        Glide.with(UpdateDataDiriStep3.this).load(URI).into(ivFotoKTP);

        btLihatDetail.setOnClickListener(v -> {
            sharedUpdateDataDiri.myEditor().putString(sharedUpdateDataDiri.NPWP, etNPWP.getText().toString());
            changeClass = new Intent(UpdateDataDiriStep3.this, DetailUpdateData.class);
            startActivity(changeClass);
        });
    }
}