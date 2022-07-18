package com.app.mobiledev.apphris;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class login extends AppCompatActivity {
    TextInputLayout validIDUser, validPassword;
    EditText txtIDUser, txtPassword, etDialogNik;
    TextView lupa_password;
    Button btnLogin;
    public static String token;
    String id_user, password;
    private CheckBox checkBox;
    SessionManager sessionManager;
    private TextView txBantuan, tx_versi;
    private ProgressDialog mProgressDialog;
    private ImageView ivVisiblePass;
    private boolean visiblePass;
    private Dialog dialogApprove;
    private CardView cvCancelDialog, cvSubmitDialog;
    private TextInputLayout tilDialogNik;

    private final String TAG = "login.java";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager = new SessionManager(this);
        validIDUser = findViewById(R.id.valid_user);
        txBantuan = findViewById(R.id.btnBantuan);
        lupa_password = findViewById(R.id.lupa_password);
        checkBox = findViewById(R.id.ck_pass);
        validPassword = findViewById(R.id.valid_pass);
        txtIDUser = findViewById(R.id.inUser);
        txtPassword = findViewById(R.id.inPass);
        btnLogin = findViewById(R.id.btnLogin);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        tx_versi = findViewById(R.id.tx_versi);
        checkBox.setChecked(false);

        ivVisiblePass = findViewById(R.id.ivVisiblePass);
        ivVisiblePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowHidePass(v);
            }
        });



        helper.ConfigFCM().addOnCompleteListener(task -> token = task.getResult());


        lupa_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifDialog();
            }
        });
        txBantuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //openWhatsApp("+6282324281046","(NIK)-(Nama Lengkap) : ");
                Intent i = new Intent(login.this, VideoUsing.class);
                startActivity(i);
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_user = txtIDUser.getText().toString().trim();
                password = txtPassword.getText().toString().trim();
                if (id_user.isEmpty()) {
                    validIDUser.setError("NIK masih kosong");
                    validIDUser.requestFocus();
                } else if (password.isEmpty()) {
                    validPassword.setError("Password masih kosong");
                    validPassword.requestFocus();
                } else {
                    //mProgressDialog.show();
                    validIDUser.setErrorEnabled(false);
                    validPassword.setErrorEnabled(false);


                        Log.d("login.java", "token login is " + token);
                        if (token.isEmpty() || token == null || token.equals("")) {
                            helper.showMsg(login.this, "Peringatan", "Login Gagal, silakan coba beberapa saat lagi", helper.WARNING_TYPE);
                        } else {
                            auth_user(id_user, password);
                        }
                    }

                }

        });
        getVersi();
    }

    //show/hide password
    public void ShowHidePass(View view) {

        if (view.getId() == R.id.ivVisiblePass) {
            if (txtPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.ic_baseline_visibility_off_24);
                //Show Password
                Log.d("TAG_SHOW", "ShowHidePass: SHOW");
                txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.ic_baseline_visibility_24);
                //Hide Password
                Log.d("TAG_HIDE", "ShowHidePass: HIDE");
                txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }

    private void auth_user(final String id_user, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, api.URL_login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //mProgressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        String email = "";
                        String password = "";
                        String noUser = "";
                        String nik = "";
                        String cekStaff = "";
                        String namaLengkap = "";
                        String hashtag = "";
                        String kydivisi = "";
                        String divisi = "";
                        String jabatan = "";
                        String kyjabatan = "";
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            email = data.getString("kyemail");
                            noUser = data.getString("kyano");
                            password = data.getString("kypassword");
                            nik = data.getString("nik");
                            cekStaff = data.getString("kyjenis");
                            namaLengkap = data.getString("kynm");
                            hashtag = data.getString("hashtag");
                            kydivisi = data.getString("kydivisi");
                            divisi = data.getString("divisi");
                            kyjabatan = data.getString("kyjabatan");
                            jabatan = data.getString("jabatan");
                            Log.d("STAFF_cek_nik", "onResponse: " + nik);

                        }

                        sessionManager.createSession(
                                noUser, email, password,
                                nik, cekStaff, namaLengkap,
                                hashtag, kydivisi, divisi,
                                kyjabatan, jabatan);
                        Toast.makeText(login.this, "Selamat datang " + namaLengkap, Toast.LENGTH_SHORT).show();
                        Intent utm = new Intent(login.this, main_fragment.class);
                        utm.putExtra("username", email);
                        startActivity(utm);
                        finish();
                    } else {
                        String data = jsonObject.getString("data");
                        helper.showMsg(login.this, "Peringatan", "" + data, helper.ERROR_TYPE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("DATA_JSONEXCEPION", "onResponse: " + e);

                    helper.showMsg(login.this, "Peringatan", "" + e.getMessage()/*helper.PESAN_KONEKSI*/, helper.ERROR_TYPE);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.dismiss();
                        Log.d("DATA_JSONEXCEPION", "onResponse: " + error);

                        helper.showMsg(login.this, "Peringatan", "" + error.toString() /*helper.PESAN_KONEKSI*/, helper.ERROR_TYPE);
                    }
                }) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                Log.d("CEK_LOGIN", "getParams: " + id_user + " cek :" + password + " macaddress: " + helper.getDeviceIMEI(login.this));
                params.put("users", id_user);
                params.put("pass", password);
                params.put("token_fcm", token);
                params.put("mc_address", helper.getDeviceId(login.this));
                params.put("key", api.key);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(login.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Konfirmasi keluar")
                .setContentText("Yakin keluar aplikasi?")
                .setConfirmText("Ya, keluar")
                .setCancelText("Tidak")
                .setConfirmClickListener(sweetAlertDialog -> {
                    finish();
                    moveTaskToBack(true);
                }).show();
    }

    private void openWhatsApp(String numero, String mensaje) {

        try {
            PackageManager packageManager = login.this.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + numero + "&text=" + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            } else {
                Toast.makeText(login.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("ERROR WHATSAPP", e.toString());
            Toast.makeText(login.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
        }

    }

    private void getVersi() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tx_versi.setText("Copyright@IT PT QHome Sukses Abadi \n (Qhomemart) Versi " + version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void notifDialog() {
        dialogApprove = new Dialog(login.this);
        dialogApprove.setContentView(R.layout.dialog_reset_password);

        cvSubmitDialog = dialogApprove.findViewById(R.id.cvSubmitDialog);
        cvCancelDialog = dialogApprove.findViewById(R.id.cvCancelDialog);

        tilDialogNik = dialogApprove.findViewById(R.id.tilDialogNik);
        etDialogNik = dialogApprove.findViewById(R.id.etDialogNik);

        dialogApprove.setCancelable(true);
        dialogApprove.setTitle("Reset Password");

        dialogApprove.show();

        cvCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogApprove.dismiss();
            }
        });

        cvSubmitDialog.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkInput() {
        if (etDialogNik.getText().toString().isEmpty()) {
            tilDialogNik.setError("NIK masih kosong");
            tilDialogNik.requestFocus();
        } else {
            resetPassword(etDialogNik.getText().toString(), helper.getDeviceIMEI(login.this));
        }
    }

    private void resetPassword(final String nik, final String mac_address) {
        AndroidNetworking.post(api.URL_reset_password)
                .addBodyParameter("mc_address", mac_address)
                .addBodyParameter("nik", nik)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("status");
                            String data = response.getString("ket");
                            if (success) {
                                new SweetAlertDialog(login.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText("" + data)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();
                            } else {
                                helper.showMsg(login.this, "Informasi", "" + data, helper.WARNING_TYPE);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            helper.showMsg(login.this, "Peringatan", "" + e.getMessage()/*helper.PESAN_SERVER*/, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(login.this, "Peringatan", "" + anError.getErrorCode()/*helper.PESAN_KONEKSI*/, helper.ERROR_TYPE);
                        mProgressDialog.dismiss();
                    }
                });
    }
}
