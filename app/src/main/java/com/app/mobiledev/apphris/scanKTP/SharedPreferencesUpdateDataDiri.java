package com.app.mobiledev.apphris.scanKTP;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUpdateDataDiri {
    private Context context;
    public final String SHARED_PREFERENCE = "SHARED_PREFERENCE";
    public final String NIK = "NIK";
    public final String NAMA = "NAMA";
    public final String TEMPAT_LAHIR = "TEMPAT_LAHIR";
    public final String TANGGAL_LAHIR = "TANGGAL_LAHIR";
    public final String ALAMAT_KTP = "ALAMAT_KTP";
    public final String ALAMAT_SEKARANG = "ALAMAT_SEKARANG";
    public final String NO_HP = "NO_HP";
    public final String ALAMAT_EMAIL = "ALAMAT_EMAIL";
    public final String IMAGE_URI = "IMAGE_URI";
    public final String NPWP = "NPWP";

    public SharedPreferencesUpdateDataDiri(Context context){
        this.context = context;
    }

    public SharedPreferences.Editor myEditor(){
        SharedPreferences prefs = context.getSharedPreferences(
                SHARED_PREFERENCE, MODE_PRIVATE);
        return prefs.edit();
    }


    public void saveDataID(SharedPreferences.Editor myEditor, String nama, String NIK, String tempat_lahir, String tanggal_lahir, String alamat_ktp, String alamat_sekarang, String image_uri){
        myEditor.putString(NAMA, nama);
        myEditor.putString(this.NIK, NIK);
        myEditor.putString(TEMPAT_LAHIR, tempat_lahir);
        myEditor.putString(TANGGAL_LAHIR, tanggal_lahir);
        myEditor.putString(ALAMAT_KTP, alamat_ktp);
        myEditor.putString(ALAMAT_SEKARANG, alamat_sekarang);
        myEditor.putString(IMAGE_URI, image_uri);

        myEditor.apply();
    }

    public void saveKontakPribadi(SharedPreferences.Editor myEditor, String no_hp, String email){
        myEditor.putString(NO_HP, no_hp);
        myEditor.putString(ALAMAT_EMAIL, email);

        myEditor.apply();
    }

}
