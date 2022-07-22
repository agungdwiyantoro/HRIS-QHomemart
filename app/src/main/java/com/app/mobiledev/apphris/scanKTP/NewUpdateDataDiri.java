package com.app.mobiledev.apphris.scanKTP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.scanKTP.productsearch.Product;

import org.json.JSONException;
import org.json.JSONObject;

public class NewUpdateDataDiri extends AppCompatActivity implements View.OnClickListener {

    private EditText etNamaLengkap, etNomorKTP, etTempatLahir, etTanggalLahir, etAlamatKTP, etAlamatSekarang;
    private Button btLihatDaftarKeluarga, btKembali, btSelanjutnya;
    private Class kelas;
    private SharedPreferencesUpdateDataDiri sharedUpdateDataDiri;

    private String nama, nik, tempatLahir, tanggalLahir, alamatKTP, alamatSekarang, imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_update_data_diri);

        init();
        sharedUpdateDataDiri = new SharedPreferencesUpdateDataDiri(NewUpdateDataDiri.this);
        Product getData = getIntent().getParcelableExtra("IDData");

        nama = getData.getTitle();
        imageUri = getData.getImageUrl();


        try {
            JSONObject extractObject = new JSONObject(getData.getSubtitle());
            nik = extractObject.getString("documentNumber");
            tempatLahir = extractObject.getString("placeOfBirth");
            tanggalLahir = extractObject.getString("dateOfBirth");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        etNamaLengkap.setText(nama);
        etNomorKTP.setText(nik);
        etTempatLahir.setText(tempatLahir);
        etTanggalLahir.setText(tanggalLahir);

        btSelanjutnya.setOnClickListener(this);
        btKembali.setOnClickListener(this);
        btLihatDaftarKeluarga.setOnClickListener(this);

    }

    private void init(){
        etNamaLengkap = findViewById(R.id.et_nama_lengkap);
        etNomorKTP = findViewById(R.id.et_nomor_ktp);
        etTempatLahir = findViewById(R.id.et_tempat_lahir);
        etTanggalLahir = findViewById(R.id.et_tanggal_lahir);
        etAlamatKTP = findViewById(R.id.et_alamat_ktp);
        etAlamatSekarang = findViewById(R.id.et_alamat_sekarang);
        btSelanjutnya = findViewById(R.id.bt_selanjutnya);
        btKembali = findViewById(R.id.bt_kembali);
        btLihatDaftarKeluarga = findViewById(R.id.bt_lihat_daftar_keluarga);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_selanjutnya:
                kelas = UpdateDataDiriStep2.class;
                alamatKTP = etAlamatKTP.getText().toString();
                alamatSekarang = etAlamatSekarang.getText().toString();

                sharedUpdateDataDiri.saveDataID(sharedUpdateDataDiri.myEditor(), nama, nik, tempatLahir, tanggalLahir, alamatKTP, alamatSekarang, imageUri);

                Log.d("NAMAX " , "SHITXXX " + getSharedPreferences(sharedUpdateDataDiri.SHARED_PREFERENCE, MODE_PRIVATE).getString(sharedUpdateDataDiri.NAMA, "") );
                break;

            case R.id.bt_kembali:
                kelas = NewUpdateDataDiri.class;
                break;

            case R.id.bt_lihat_daftar_keluarga:
                kelas = NewUpdateDataDiri.class;
                break;
        }

        Intent next = new Intent(NewUpdateDataDiri.this, kelas);
        startActivity(next);
        finish();
    }
}