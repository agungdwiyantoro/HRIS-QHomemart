package com.app.mobiledev.apphris.scanKTP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.mobiledev.apphris.R;
import com.bumptech.glide.Glide;


public class DetailUpdateData extends AppCompatActivity implements View.OnClickListener {

    private TextView tvNama, tvNIK, tvTempatLahir, tvTanggalLahir, tvAlamatKTP, tvAlamatSekarang, tvNoHP, tvAlamatEmail, tvNPWP;
    private Button btKembali, btKirimData;
    private ImageView ivFotoKTP, ivFotoKK, ivFotoNPWP;

    private SharedPreferencesUpdateDataDiri sharedUpdataDataDiri;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedUpdataDataDiri = new SharedPreferencesUpdateDataDiri(DetailUpdateData.this);
        sharedPreferences = getSharedPreferences(sharedUpdataDataDiri.SHARED_PREFERENCE, MODE_PRIVATE);

        setContentView(R.layout.activity_detail_update_data);
        tvNama = findViewById(R.id.tv_value_name);
        tvNIK = findViewById(R.id.tv_value_no_ktp);
        tvTempatLahir = findViewById(R.id.tv_value_tempat_lahir);
        tvTanggalLahir = findViewById(R.id.tv_value_tanggal_lahir);
        tvAlamatKTP = findViewById(R.id.tv_value_alamat_ktp);
        tvAlamatSekarang = findViewById(R.id.tv_value_alamat_sekarang);
        tvNoHP = findViewById(R.id.tv_value_no_hp);
        tvAlamatEmail = findViewById(R.id.tv_value_alamat_email);
        tvNPWP = findViewById(R.id.tv_value_npwp);

        ivFotoKTP = findViewById(R.id.iv_foto_ktp);

        btKembali = findViewById(R.id.bt_kembali);
        btKirimData = findViewById(R.id.bt_kirim_data);

        setData();

        btKembali.setOnClickListener(this);
        btKirimData.setOnClickListener(this);

    }

    private void setData(){
        tvNama.setText(sharedPreferences.getString(sharedUpdataDataDiri.NAMA, ""));
        tvNIK.setText(sharedPreferences.getString(sharedUpdataDataDiri.NIK, ""));
        tvTempatLahir.setText(sharedPreferences.getString(sharedUpdataDataDiri.TEMPAT_LAHIR, ""));
        tvTanggalLahir.setText(sharedPreferences.getString(sharedUpdataDataDiri.TANGGAL_LAHIR, ""));
        tvAlamatKTP.setText(sharedPreferences.getString(sharedUpdataDataDiri.ALAMAT_KTP, ""));
        tvAlamatSekarang.setText(sharedPreferences.getString(sharedUpdataDataDiri.ALAMAT_SEKARANG, ""));
        tvNoHP.setText(sharedPreferences.getString(sharedUpdataDataDiri.NO_HP,""));
        tvAlamatEmail.setText(sharedPreferences.getString(sharedUpdataDataDiri.ALAMAT_EMAIL, ""));
        tvNPWP.setText(sharedPreferences.getString(sharedUpdataDataDiri.NPWP, ""));
        Glide.with(DetailUpdateData.this).load(sharedPreferences.getString(sharedUpdataDataDiri.IMAGE_URI,"")).into(ivFotoKTP);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_kembali:

                break;

            case R.id.bt_kirim_data:

                break;
        }
    }
}