package com.app.mobiledev.apphris.sesion;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.app.mobiledev.apphris.login;
import com.app.mobiledev.apphris.main_fragment;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;
    private final String TAG = "SessionManager";

    public static final String PREF_NAME = "LOGIN";
    public static final String LOGIN = "IS_LOGIN";
    public static final String ID_USER = "ID_USER"; //kyano
    public static final String NAMA_USER = "NAMA_USER";
    public static final String PASS_USER = "PASS_USER";
    public static final String NIK_USER = "NIK_USER";//y show bio
    public static final String CEK_STAFF = "CEK_STAFF";//y show bio
    public static final String CEK_NAMA_LENGKAP = "NAMA_LENGKAP";//y show bio
    public static final String HASHTAG = "HASHTAG";//y show bio
    public static final String KYJABATAN = "KYJABATAN";//y show bio
    public static final String JABATAN = "JABATAN";//y show bio
    public static final String KYDIVISI = "KYDIVISI";//y show bio
    public static final String DIVISI = "DIVISI";//y show bio
    public static final String IDTRANING="IDTRAINING";//y show id training
    public static final String TOKEN = "TOKEN";//token hris
    public static final String CUTOFF = "CUTOFF";
    public static final String LOKASI_JANTI="LOKASI_JANTI"; // session create lokasi qhome janti
    public static final String LOKASI_JANTI_LESTARI="LOKASI_JANTI_LESTARI"; // session create lokasi qhome janti
    public static final String LOKASI_PIYUNGAN ="LOKASI_PIYUNGAN"; // session create lokasi qhome janti
    public static final String LOKASI_BERBAH="LOKASI_BERBAH";// session create lokasi qhome janti
    public static final String JARAK="JARAK"; // radius presensi
    public static final String TGL_PRESENSI_MASUK="TGL_PRESENSI_MASUK";// session berhasil simpan tgl_presensi_masuk
    public static final String PRESENSI_JARAK_JAUH="PRESENSI_JARAK_JAUH";// session berhasil simpan tgl_presensi_masuk presesnsi 0=tidak bisa prsensi jarak jauh,0=bisa prsensi jarak jauh
    public static final String NO_HP_ADMIN="NO_HP_ADMIN";// session berhasil simpan tgl_presensi_masuk

    public static  final String KYJK = "KYJK";
    public static  final String KYAGAMA="KYAGAMA";
    public static  final String KYTPTLHR="KYTPTLHR";
    public static  final String KYTGLLHR="KYTGLLHR";
    public static  final String KYSTATUS_KERJA="KYSTATUS_KERJA";
    public static  final String KYALAMAT="KYALAMAT";
    public static  final String KYHP="KYHP";
    public static  final String JBANO="JBANO";
    public static  final String DVANO="DVANO";
    public static  final String NPWP="NPWP";
    public static  final String KYTGLMASUK="KYTGLMASUK";
    public static  final String KYEMAIL="KYEMAIL";
    public static  final String KYALAMAT_SKRANG="KYALAMAT_SKRANG";
    public static  final String ALAMAT_STATUS="ALAMAT_STATUS";
    public static  final String INFO_UPDATE = "INFO_UPDATE";//untuk cek info update playstore


    public static String nama_user;
    public static String pass_user;
    public static String nik_user;
    public static String id_user;
    public static String cek_staff;
    public static String namaLengkap;
    public static String hashtag;
    public static String kyDivisi;
    public static String divisi;
    public static String jabatan;
    public static String idtraining;
    public static String lokasi_janti;
    public static String lokasi_janti_lestari;
    public static String lokasi_piyungan;
    public static String lokasi_berbah;
    public static  String jarak;
    public static  String tgl_presensi_masuk;
    public static  String no_hp_admin;

    public static  String kyjk;
    public static  String kyagama;
    public static  String kytptlhr;
    public static  String kytgllhr;
    public static  String kystatus_kerja;
    public static  String kyalamat;
    public static  String kyhp;
    public static  String kyjabatan;
    public static  String dvano;
    public static  String npwp;
    public static  String kytglmasuk;
    public static  String kyemail;
    public static  String kyalamat_skrang;
    public static  String presensi_jarak_jauh;
    public static  Boolean info_update;




    public static String token;
    public static String cutOff;

    public static String statusAlamat;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }


    //token hris
    public void createToken(String token){
        editor.putString(TOKEN, token);
        editor.apply();
    }

    public  String getToken() {
        token=sharedPreferences.getString(TOKEN, "");
        return token;
    }

    public void createSession(String id_user, String email,String pass,
                              String nik,String cek_staff,String namaLengkap,
                              String hashtag,String kydivisi,String divisi,
                              String kyjabatan, String jabatan){
        editor.putBoolean(LOGIN, true);
        editor.putString(ID_USER, id_user);
        editor.putString(KYEMAIL, email);
        editor.putString(PASS_USER, pass);
        editor.putString(NIK_USER, nik);
        editor.putString(CEK_STAFF, cek_staff);
        editor.putString(CEK_NAMA_LENGKAP, namaLengkap);
        editor.putString(HASHTAG, hashtag);
        editor.putString(KYDIVISI, kydivisi);
        editor.putString(DIVISI, divisi);
        editor.putString(KYJABATAN, kyjabatan);
        editor.putString(JABATAN, jabatan);
        editor.apply();

    }



    public void  Sessionprofile(String nik, String namaLengkap, String cek_staff, String hashtag,
                                String kyjk,String kyagama,String ky_tempt_lahir,String ky_tgl_lahir, String kystatus_kerja, String kyalamat, String kyhp, String jbano,String dvano,
                                String npwp, String kytglmasuk, String kyemail,String kyalamat_skrang){

        editor.putString(NIK_USER, nik);
        editor.putString(CEK_STAFF, cek_staff);
        editor.putString(CEK_NAMA_LENGKAP, namaLengkap);
        editor.putString(HASHTAG, hashtag);
        editor.putString(KYJK, kyjk);
        editor.putString(KYAGAMA, kyagama);
        editor.putString(KYTPTLHR, ky_tempt_lahir);
        editor.putString(KYTGLLHR, ky_tgl_lahir);
        editor.putString(KYSTATUS_KERJA, kystatus_kerja);
        editor.putString(KYALAMAT, kyalamat);
        editor.putString(KYHP, kyhp);
        editor.putString(JBANO, jbano);
        editor.putString(DVANO, dvano);
        editor.putString(NPWP, npwp);
        editor.putString(KYTGLMASUK, kytglmasuk);
        editor.putString(KYEMAIL, kyemail);
        editor.putString(KYALAMAT_SKRANG, kyalamat_skrang);
        editor.apply();

    }

    public void createNoHpAdmin(String noHp){
        editor.putString(no_hp_admin,noHp);
        editor.apply();
    }


    //get simpan id training yang dikirim
    public void createIdTraining(String idtrain){
        editor.putString(IDTRANING, idtrain);
        editor.apply();
    }



    //get berhasil tgl presensi masuk
    public void createTglPresensiMasuk(String tgl){
        editor.putString(TGL_PRESENSI_MASUK, tgl);
        editor.apply();
    }


    //simpan lokasi titik presensi qhomemart
    public void createLocation(String lokasi_janti, String lokasi_janti_lestari,String lokasi_piyungan, String lokasi_berbah,String jarak,String presensi_jarak_jauh){

        if(lokasi_janti.equals(null)||lokasi_janti.equals("")||lokasi_janti_lestari.equals(null)||lokasi_janti_lestari.equals("")||lokasi_piyungan.equals(null)||lokasi_piyungan.equals("")||lokasi_berbah.equals(null)||lokasi_berbah.equals("")){
            editor.putString(LOKASI_JANTI, "-7.795378,110.409476");
            editor.putString(LOKASI_JANTI_LESTARI, "-7.792640362624446,110.40977480141044");
            editor.putString(LOKASI_BERBAH, "-7.804776,110.446526");
            editor.putString(LOKASI_PIYUNGAN, "-7.835307,110.441678");
            editor.putString(JARAK, "200");
            editor.putString(JARAK, "200");
            editor.putString(PRESENSI_JARAK_JAUH,presensi_jarak_jauh);

        }else{
            editor.putString(LOKASI_JANTI, lokasi_janti);
            editor.putString(LOKASI_JANTI_LESTARI, lokasi_janti_lestari);
            editor.putString(LOKASI_BERBAH, lokasi_berbah);
            editor.putString(LOKASI_PIYUNGAN, lokasi_piyungan);
            editor.putString(JARAK, jarak);
            editor.putString(PRESENSI_JARAK_JAUH,presensi_jarak_jauh);

        }

        Log.d("CEK_LOKASI", "createLocation: "+lokasi_janti+" lokasi_JL"+lokasi_janti_lestari);
        editor.apply();
    }

    public void createStatusUpdatePlayStore(Boolean nilai){
        editor.putBoolean(INFO_UPDATE,nilai);
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if(sharedPreferences.contains(CEK_NAMA_LENGKAP) && sharedPreferences.contains(ID_USER)){
            Intent i = new Intent(context, main_fragment.class);
           // Intent i = new Intent(context, UpdateDataDiri.class);
            i.putExtra("username",sharedPreferences.getString(CEK_NAMA_LENGKAP,"Username"));
            context.startActivity(i);
        }else{
            Intent load = new Intent(context, login.class);
            context.startActivity(load);
        }

    }

    public  String ConfigFCM(){
        // to subscribe a topic from FCM
        // FirebaseMessaging.getInstance().subscribeToTopic(topics);
        // to get token new FCM version

        return FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(token -> {
                    if (!TextUtils.isEmpty(token)) {
                        Log.d(TAG, "token successfully retrieved : " + token);
                    } else{
                        Log.w(TAG, "token should not be null...");
                    }
                })
                .addOnCompleteListener(task -> {
                    Log.v(TAG, "This is the token : " + task.getResult());
                }).getResult();
    }



    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> user = new HashMap<>();
        user.put(ID_USER, sharedPreferences.getString(ID_USER, ""));
        user.put(CEK_NAMA_LENGKAP, sharedPreferences.getString(NAMA_USER, ""));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
    }

    public String getUsername(){
        nama_user=sharedPreferences.getString(CEK_NAMA_LENGKAP, "");
        return nama_user;

    }

    public String getNo_hp_admin(){
        no_hp_admin=sharedPreferences.getString(no_hp_admin,"");
        return no_hp_admin;
    }

    public String getPass(){
        pass_user=sharedPreferences.getString(PASS_USER, "");
        return pass_user;

    }


    public String getNik(){
        nik_user=sharedPreferences.getString(NIK_USER, "");
        return nik_user;

    }

    public String getIdUser(){
        id_user=sharedPreferences.getString(ID_USER, "");
        return id_user;

    }

    public String getCekStaff(){
        cek_staff=sharedPreferences.getString(CEK_STAFF, "");
        return cek_staff;

    }


    public void remove_tgl_presensi_masuk(){
        sharedPreferences.edit().remove(TGL_PRESENSI_MASUK).commit();
    }




    public String getNamaLEngkap(){
        namaLengkap=sharedPreferences.getString(CEK_NAMA_LENGKAP, "");
        return namaLengkap;

    }
    public String getHashtag(){
        hashtag=sharedPreferences.getString(HASHTAG, "");
        return hashtag;

    }

    public String getKyDivisi(){
        kyDivisi=sharedPreferences.getString(KYDIVISI, "");
        return kyDivisi;
    }

    public String getDivisi(){
        divisi=sharedPreferences.getString(DIVISI, "");
        return divisi;

    }


    public String getJabatan(){
        jabatan=sharedPreferences.getString(JABATAN, "");
        return jabatan;
    }

    public String getIdtraning(){
        idtraining=sharedPreferences.getString(IDTRANING, "");
        return idtraining;
    }


    public String getLokasiJanti(){
        lokasi_janti=sharedPreferences.getString(LOKASI_JANTI,"");
        return  lokasi_janti;
    }


    public String getLokasiJantiLestari(){
        lokasi_janti_lestari=sharedPreferences.getString(LOKASI_JANTI_LESTARI,"");
        return  lokasi_janti_lestari;
    }


    public String getLokasiPiyungan(){
        lokasi_piyungan=sharedPreferences.getString(LOKASI_PIYUNGAN,"");
        return  lokasi_piyungan;
    }



    public String getLokasiBerbah(){
        lokasi_berbah=sharedPreferences.getString(LOKASI_BERBAH,"");
        return  lokasi_berbah;
    }

    public String getJarak(){
        jarak=sharedPreferences.getString(JARAK,"");
        return  jarak;
    }


    public String getTglPresensiMasuk(){
        tgl_presensi_masuk=sharedPreferences.getString(TGL_PRESENSI_MASUK,"");
        return tgl_presensi_masuk;
    }

    public String getNodivisi(){
        dvano=sharedPreferences.getString(DVANO,"");
        return dvano;
    }

    public String getJenisKelamin(){
        kyjk=sharedPreferences.getString(KYJK,"");
        return kyjk;
    }

    public String getAgama(){
        kyagama=sharedPreferences.getString(KYAGAMA,"");
        return kyagama;
    }

    public String getTempatLahir(){
        kytptlhr=sharedPreferences.getString(KYTPTLHR,"");
        return kytptlhr;
    }


    public String getTglLahir(){
        kytgllhr=sharedPreferences.getString(KYTGLLHR,"");
        return kytgllhr;
    }


    public String getStatusKerja(){
        kystatus_kerja=sharedPreferences.getString(KYSTATUS_KERJA,"");
        return kystatus_kerja;
    }


    public String getAlamat(){
        kyalamat=sharedPreferences.getString(KYALAMAT,"");
        return kyalamat;
    }


    public String getKyhp(){
        kyhp=sharedPreferences.getString(KYHP,"");
        return kyhp;
    }

    public String getNoJabatan(){
        kyjabatan=sharedPreferences.getString(KYJABATAN,"");
        return kyjabatan;
    }

    public String getDvano(){
        dvano=sharedPreferences.getString(DVANO,"");
        return dvano;
    }


    public String getNpwp(){
        npwp=sharedPreferences.getString(NPWP,"");
        return npwp;
    }


    public String getTglmasuk(){
        kytglmasuk=sharedPreferences.getString(KYTGLMASUK,"");
        return kytglmasuk;
    }

    public String getEmail(){
        kyemail=sharedPreferences.getString(KYEMAIL,"");
        return kyemail;
    }


    public String getAlamat_sekarang(){
        kyalamat_skrang=sharedPreferences.getString(KYALAMAT_SKRANG,"");
        return kyalamat_skrang;
    }

    public void putAlamatKtp(String alamatKtp){
        editor.putString(KYALAMAT, alamatKtp);
        editor.apply();
    }

    public String getAlamatKtp(){
        kyalamat=sharedPreferences.getString(KYALAMAT,"");
        return kyalamat;
    }

    public void putAlamatNow(String alamatNow){
        editor.putString(KYALAMAT_SKRANG, alamatNow);
        editor.apply();
    }

    public String getAlamatNow(){
        kyalamat_skrang=sharedPreferences.getString(KYALAMAT_SKRANG,"");
        return kyalamat_skrang;
    }

    public void putStatusAlamat(String statusAlamat){
        editor.putString(ALAMAT_STATUS, statusAlamat);
        editor.apply();
    }

    public String getStatusAlamat(){
        statusAlamat=sharedPreferences.getString(ALAMAT_STATUS,"");
        return statusAlamat;
    }

    public String getPresensiJarakJauh(){
        presensi_jarak_jauh=sharedPreferences.getString(PRESENSI_JARAK_JAUH,"");
        return presensi_jarak_jauh;
    }

    public Boolean getUpdatePlayStore(){
        info_update=sharedPreferences.getBoolean(INFO_UPDATE,true);
        return info_update;
    }



}

