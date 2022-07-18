package com.app.mobiledev.apphris;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.profile.PerjanjianKerja.PerjanjianKerja;
import com.app.mobiledev.apphris.profile.UbahFoto;
import com.app.mobiledev.apphris.profile.UbahPass;
import com.app.mobiledev.apphris.profile.UpdateDataDiri;
import com.app.mobiledev.apphris.scanKTP.NewUpdateDataDiri;
import com.app.mobiledev.apphris.scanKTP.ScanKTP;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class fragment_profile extends Fragment {
    public fragment_profile() {
    }

    private TextView txNama, txNik, txDivisi, txHastag, txJabatan, txUpdate;

    private SessionManager sessionmanager;
    private String kyano, namas, namaLengkap, cekStaff, password, hastag, nik, divisi, jabatan, no_admin;

    private ProgressDialog mProgressDialog;
    private String url_foto = "";

    private CardView cvUpdateDataDiri, cvPerjanjianKerja, cvUbahPassword, cvBantuan;
    private ImageView foto_profil, ivClose;
    private Button btnSubmit, btnCancel, btnOpenSetting, btnGuide, btnWa;

    private LinearLayout llLogout, llOverlayKonfirmasi, llUserGuide, llHelpOption;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_profil_new, container, false);

        foto_profil = rootView.findViewById(R.id.foto_profil);
        //txNik = rootView.findViewById(R.id.txtNik);
        txNama = rootView.findViewById(R.id.txtNamaFull);
        txDivisi = rootView.findViewById(R.id.txtDivisi);
        txJabatan = rootView.findViewById(R.id.txtJabatan);
        txHastag = rootView.findViewById(R.id.txtHastag);
        txUpdate = rootView.findViewById(R.id.txtUpdateDataDiri);
        llLogout = rootView.findViewById(R.id.llLogout);
        btnSubmit = rootView.findViewById(R.id.btnSubmit);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        btnOpenSetting = rootView.findViewById(R.id.btnOpenSettingSystem);
        btnGuide = rootView.findViewById(R.id.btnGuide);
        btnWa = rootView.findViewById(R.id.btnWa);
        ivClose = rootView.findViewById(R.id.ivClose);
        llOverlayKonfirmasi = rootView.findViewById(R.id.llOverlayKonfirmasi);
        llUserGuide = rootView.findViewById(R.id.llUserGuide);
        llHelpOption = rootView.findViewById(R.id.llHelpOption);

        cvUpdateDataDiri = rootView.findViewById(R.id.cvUpdateDataDiri);
        cvUbahPassword = rootView.findViewById(R.id.cvUbahPassword);
        cvPerjanjianKerja = rootView.findViewById(R.id.cvPerjanjianKerja);
        cvBantuan = rootView.findViewById(R.id.cvBantuan);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading ...");
        AndroidNetworking.initialize(getActivity().getApplicationContext());


        sessionmanager = new SessionManager(getActivity());
        kyano = sessionmanager.getIdUser();
        nik = sessionmanager.getNik();
        namas = sessionmanager.getUsername();
        password = sessionmanager.getPass();
        namaLengkap = sessionmanager.getNamaLEngkap();
        cekStaff = sessionmanager.getCekStaff();
        hastag = sessionmanager.getHashtag();

        getInformasiKaryawan(kyano);

        //txNik.setText(nik);
        txNama.setText(namaLengkap);
        txDivisi.setText(cekStaff);
        txHastag.setText("#" + hastag);
        txHastag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setText(txHastag.getText());
                Toast.makeText(getActivity(), "Hastag berhasil dicopy", Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("DATA_DIRI", "onCreate: " + password + " USER : " + namaLengkap + " NIK : " + cekStaff + " HASTAG : " + hastag);
        mProgressDialog.show();

        getlImageProfil(kyano);

        no_admin = sessionmanager.getNo_hp_admin();

        foto_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), UbahFoto.class);
                startActivity(intent);

            }
        });

        //tilUbahPass = rootView.findViewById(R.id.tilUbahPass);
        cvUbahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UbahPass.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        cvUpdateDataDiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScanKTP.class);
                startActivity(intent);
            }
        });

        cvPerjanjianKerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PerjanjianKerja.class);
                startActivity(intent);
                //Toast.makeText(getActivity(), "Coming Soon", Toast.LENGTH_SHORT).show();
            }
        });

        llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llOverlayKonfirmasi.setVisibility(View.VISIBLE);
            }
        });

        llOverlayKonfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llOverlayKonfirmasi.setVisibility(View.GONE);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logout
                sessionmanager.logout();
                Intent intent3 = new Intent(getActivity(), splashScreen.class);
                startActivity(intent3);
                getActivity().finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dismiss
                llOverlayKonfirmasi.setVisibility(View.GONE);
            }
        });

        btnOpenSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llUserGuide.setVisibility(View.GONE);
            }
        });

        cvBantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llHelpOption.setVisibility(View.VISIBLE);
            }
        });

        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llUserGuide.setVisibility(View.VISIBLE);
            }
        });

        btnWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWhatsApp(no_admin, "NIK : "+nik+"\nNama Lengkap : "+namaLengkap+"\nKendala : \n");
            }
        });

        llHelpOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llHelpOption.setVisibility(View.GONE
                );
            }
        });

        return rootView;
    }

    private void getlImageProfil(final String kyano) {
        AndroidNetworking.post(api.URL_getfoto_profil)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("status");
                            String data = response.getString("data");
                            if (success) {
                                url_foto = data;
                                RequestOptions requestOptions = new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true);
                                Glide.with(getActivity()).load(api.get_url_foto_profil(kyano, url_foto)).apply(requestOptions).into(foto_profil);
                            } else {
                                Log.d("", "onResponse: " + data);
                            }

                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: " + e);
                            helper.showMsg(getActivity(), "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(getActivity(), "Peringatan", "" + helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: " + anError);
                        mProgressDialog.dismiss();
                    }
                });
    }

    private void getInformasiKaryawan(final String kyano) {
        AndroidNetworking.post(api.URL_informasiKaryawan)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String kyano = data.getString("kyano");
                                    String nik = data.getString("nik");
                                    String kynm = data.getString("kynm");
                                    String kyjk = data.getString("kyjk");
                                    String kyagama = data.getString("kyagama");
                                    String kytgllahir = data.getString("kytgllhr");
                                    String kystatus_kerja = data.getString("kystatus_kerja");
                                    String kyalamat = data.getString("kyalamat");
                                    String kyhp = data.getString("kyhp");
                                    String jbnama = data.getString("jbnama");
                                    String dvnama = data.getString("dvnama");
                                    String jbano = data.getString("jbano");
                                    String dvano = data.getString("dvano");

                                    if (dvnama.equals("BUMI BERLIAN MEGA INDONESIA")) {
                                        txDivisi.setText("BBMI");
                                    } else {
                                        txDivisi.setText(dvnama);
                                    }

                                    txJabatan.setText(jbnama);

                                }
                            } else {
                                Log.d("DATA_BOOLEAN", "onResponse: " + success);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: " + e);
                            helper.showMsg(getActivity(), "Peringatan", "" + helper.PESAN_SERVER, helper.ERROR_TYPE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(getActivity(), "Peringatan", "" + helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: " + anError);

                    }
                });

    }

    private void openWhatsApp(String number, String msg) {
        try {
            //mobile = /*"6288902829423"*/number;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + number + "&text=" + msg)));
        }catch (Exception e){
            //whatsapp app not install
            Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
        }
    }

}
