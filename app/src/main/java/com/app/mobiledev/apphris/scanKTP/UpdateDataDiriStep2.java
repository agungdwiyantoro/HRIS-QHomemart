package com.app.mobiledev.apphris.scanKTP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.mobiledev.apphris.R;

public class UpdateDataDiriStep2 extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "UpdateDataDiriStep2";

    private String nama, nik, tempatLahir, tanggalLahir, alamatKTP, alamatSekarang, imageUri;

    private SharedPreferencesUpdateDataDiri sharedUpdateDataDiri;
    private Class kelas;
    private EditText noHp, alamatEmail;
    private Button btKembali, betSelanjutnya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data_diri_step2);
        noHp = findViewById(R.id.et_no_hp);
        alamatEmail = findViewById(R.id.et_alamat_email);
        btKembali = findViewById(R.id.bt_kembali);
        betSelanjutnya = findViewById(R.id.bt_selanjutnya);
        betSelanjutnya.setOnClickListener(this);

        sharedUpdateDataDiri = new SharedPreferencesUpdateDataDiri(UpdateDataDiriStep2.this);

        SharedPreferences sharedPreference = getSharedPreferences(sharedUpdateDataDiri.SHARED_PREFERENCE, MODE_PRIVATE);

        nama = sharedPreference.getString(sharedUpdateDataDiri.NAMA, "");
        nik = sharedPreference.getString(sharedUpdateDataDiri.NIK, "");
        tempatLahir = sharedPreference.getString(sharedUpdateDataDiri.TEMPAT_LAHIR, "");
        tanggalLahir = sharedPreference.getString(sharedUpdateDataDiri.TANGGAL_LAHIR, "");
        alamatKTP = sharedPreference.getString(sharedUpdateDataDiri.ALAMAT_KTP, "");
        alamatSekarang = sharedPreference.getString(sharedUpdateDataDiri.ALAMAT_SEKARANG, "");
        imageUri = sharedPreference.getString(sharedUpdateDataDiri.IMAGE_URI, "");

        Log.d(TAG, "NAMA : " + nama);
        Log.d(TAG, "NIK : " + nik);
        Log.d(TAG, "TEMPAT LAHIR : " + tempatLahir);
        Log.d(TAG, "TANGGAL LAHIR : " + tanggalLahir);
        Log.d(TAG, "ALAMAT KTP : " + alamatKTP);
        Log.d(TAG, "ALAMAT SEKARANG : " + alamatSekarang);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_kembali :

                break;

            case R.id.bt_selanjutnya :
                kelas = UpdateDataDiriStep3.class;

                sharedUpdateDataDiri.saveKontakPribadi(
                        sharedUpdateDataDiri.myEditor(),
                        noHp.getText().toString(),
                        alamatEmail.getText().toString());
                break;
        }

        Intent changeClass = new Intent(UpdateDataDiriStep2.this, kelas);
        startActivity(changeClass);
    }
}