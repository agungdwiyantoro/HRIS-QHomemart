package com.app.mobiledev.apphris.profile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;

import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.profile.dataKeluarga.DataKeluarga;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.app.mobiledev.apphris.profile.alamat.AlamatIndonesia;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
public class UpdateDataDiri extends AppCompatActivity {
    private TextInputLayout tilNama, tilKtp, tilJabatan, tilTgl_lahir, tilMasukKerja, tilAlamatKtp, tilAlamatNow, tilNoHp, tilEmail, tilNpwp, tIlKeluarga;
    private EditText etNama, etKtp, etJabatan, etTgl_lahir, etMasukKerja, etAlamatKtp, etAlamatNow, etNoHp, etEmail, etNpwp;
    private ImageView imgKtp, imgKK, imgNpwp;
    private Button btnUpdateData;
    private TextView tx_size_ktp;
    private TextView tx_size_kk;
    private TextView tx_size_npwp;
    private String nama,ktp,tgl_lahir,tgl_masuk_kerja,alamat_ktp,alamat_now,no_hp,email,npwp,kyano,jbano,nik,tempat_lahir;
    private SessionManager session;
    private ProgressDialog mProgressDialog;
    private EditText etTempatLahir;
    private TextInputLayout tIlTempatLahir;
    private ImageView imgAlamat1;
    private ImageView imgAlamat2;
    private ImageView btn_tgl_masuk_kerja;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private ImageView btn_tgl_lahir;
    private Uri resultUri;
    private Bitmap image_bmap;
    private File compressedImages;
    private File compressedImage_ktp;
    private File compressedImage_kk;
    private File compressedImage_npwp;
    private File actualImage;
    private Bitmap bitmap_ktp;
    private Bitmap bitmap_kk;
    private Bitmap bitmap_npwp;
    private Boolean bool_imgKk=false;
    private Boolean bool_imgKtp=false;
    private Boolean bool_imgNpwp=false;
    private int size_imgKK=0;
    private int size_imgKtp=0;
    private int size_imgNpwp=0;
    private String name_ktp="";
    private String name_kk="";
    private String name_npwp="";
    private Boolean size_image_ktp=false;
    private Boolean size_image_kk=false;
    private Boolean size_image_npwp=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data_diri);
        etNama=findViewById(R.id.etNama);
        tilKtp=findViewById(R.id.tIlNik);
        tilJabatan=findViewById(R.id.tIlJabDiv);
        tilTgl_lahir=findViewById(R.id.tIlTgl);
        tilMasukKerja=findViewById(R.id.tIlMasukKerja);
        tilAlamatKtp=findViewById(R.id.tIlAlamatKtp);
        tilAlamatNow=findViewById(R.id.tIlAlamatNow);
        tilNoHp=findViewById(R.id.tIlNoHP);
        tilEmail=findViewById(R.id.tIlEmail);
        tilNpwp=findViewById(R.id.tIlNpWp);
        tilNama=findViewById(R.id.tIlNama);
        tIlKeluarga=findViewById(R.id.tIlKeluarga);
        etTempatLahir=findViewById(R.id.etTempatLahir);
        btnUpdateData=findViewById(R.id.btnUpdateData);
        btn_tgl_masuk_kerja=findViewById(R.id.btn_tgl_masuk_kerja);
        tx_size_ktp=findViewById(R.id.tx_size_ktp);
        tx_size_kk=findViewById(R.id.tx_size_kk);
        tx_size_npwp=findViewById(R.id.tx_size_npwp);


        etNama=findViewById(R.id.etNama);
        etKtp=findViewById(R.id.etNik);
        tIlTempatLahir=findViewById(R.id.tIlTempatLahir);
        etJabatan=findViewById(R.id.etJabDiv);
        helper.disabledEditText(etJabatan);
        etTgl_lahir=findViewById(R.id.etTgl);
        helper.disabledEditText(etTgl_lahir);
        etMasukKerja=findViewById(R.id.etMasukKerja);
        helper.disabledEditText(etMasukKerja);
        etAlamatKtp=findViewById(R.id.etAlamatKtp);
        etAlamatNow=findViewById(R.id.etAlamatNow);
        etNoHp=findViewById(R.id.etNoHP);
        etEmail=findViewById(R.id.etEmail);
        etNpwp=findViewById(R.id.etNpWp);
        btn_tgl_lahir=findViewById(R.id.btn_tgl_lahir);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        imgKtp=findViewById(R.id.imgKtp);
        imgKK=findViewById(R.id.imgKK);
        imgNpwp=findViewById(R.id.imgNpWp);
        session=new SessionManager(this);
        kyano=session.getIdUser();
        nik=session.getNik();
        jbano=session.getNoJabatan();
        loadprofile();
        imgAlamat1 = findViewById(R.id.imgAlamat1);
        imgAlamat2 = findViewById(R.id.imgAlamat2);
        getDataDiri();
        helper.verifyStoragePermissions(UpdateDataDiri.this);
        helper.disabledEditText(etAlamatNow);

        imgAlamat1.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateDataDiri.this, AlamatIndonesia.class);
            startActivity(intent);
            session.putStatusAlamat("ktp");
        });

        imgAlamat2.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateDataDiri.this, AlamatIndonesia.class);
            startActivity(intent);
            session.putStatusAlamat("now");
        });

        tIlKeluarga.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateDataDiri.this, DataKeluarga.class);
            startActivity(intent);
        });



        btnUpdateData.setOnClickListener(view -> {
            reset_validation();
            nama=etNama.getText().toString();
            alamat_ktp=etAlamatKtp.getText().toString();
            ktp=etKtp.getText().toString();
            tgl_lahir=etTgl_lahir.getText().toString();
            tgl_masuk_kerja=etMasukKerja.getText().toString();
            alamat_now=etAlamatNow.getText().toString();
            no_hp=etNoHp.getText().toString();
            email=etEmail.getText().toString();
            npwp=etNpwp.getText().toString();
            tempat_lahir=etTempatLahir.getText().toString();
            if(is_valid()){
                mProgressDialog.show();
                insert_data_kk(kyano,nik,jbano,tempat_lahir,tgl_lahir,tgl_masuk_kerja,alamat_ktp,alamat_now,no_hp,email,npwp,nama,compressedImage_ktp,compressedImage_kk,compressedImage_npwp);

            }else{
                mProgressDialog.dismiss();
            }

        });


        btn_tgl_lahir.setOnClickListener(view -> showDateDialogTglLahir());
        btn_tgl_masuk_kerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialogTglMasuk();
            }
        });

        imgKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                bool_imgKtp=true;
                bool_imgKk=false;
                bool_imgNpwp=false;
                */


            }
        });


        imgKK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                bool_imgKtp=false;
                bool_imgKk=true;
                bool_imgNpwp=false;

            }
        });


        imgNpwp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                bool_imgKtp=false;
                bool_imgKk=false;
                bool_imgNpwp=true;

            }
        });
        Log.d("CEK_KYANO", "onClick: "+kyano);
    }
    private void insert_data_kk(String kyano,String nik,String jabatan,String tempat_lahir,String tgl_lahir,String tgl_masuk_kerja,String alamat_ktp,String alamat_sekarang,String telp,String email, String npwp,String nama,File file_ktp,File file_kk,File file_npwp){
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(200, TimeUnit.MINUTES)
                .readTimeout(200, TimeUnit.MINUTES)
                .writeTimeout(200, TimeUnit.MINUTES)
                .build();
                AndroidNetworking.initialize(UpdateDataDiri.this, okHttpClient);
                AndroidNetworking.upload(api.URL_insertKK_new)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("Accept", "application/json")
                .addMultipartFile("upluoad_file_ktp",file_ktp)
                .addMultipartFile("upluoad_file_kk",file_kk)
                .addMultipartFile("upluoad_file_npwp",file_npwp)
                .addMultipartParameter("kyano", kyano)
                .addMultipartParameter("nik", nik)
                .addMultipartParameter("jbano", jabatan)
                .addMultipartParameter("tempat_lahir", tempat_lahir)
                .addMultipartParameter("tgl_lahir", tgl_lahir)
                .addMultipartParameter("tgl_masuk_kerja", tgl_masuk_kerja)
                .addMultipartParameter("alamat_ktp", alamat_ktp)
                .addMultipartParameter("alamat_sekarang", alamat_sekarang)
                .addMultipartParameter("telp", telp)
                .addMultipartParameter("email", email)
                .addMultipartParameter("npwp", npwp)
                .addMultipartParameter("nama", nama)
                .addMultipartParameter("key", api.key)
                .addMultipartParameter("image_name_ktp", name_ktp)
                .addMultipartParameter("image_name_kk", name_kk)
                .addMultipartParameter("image_name_npwp", name_npwp)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPONSE_CEK", "onResponse: "+response);

                        try {
                            Boolean success = response.getBoolean("status");
                            String ket = response.getString("ket");
                            if (success) {
                                new SweetAlertDialog(UpdateDataDiri.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("konfirmasi")
                                        .setContentText(""+ket)
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();

                                            }
                                        }).show();
                            }else{
                                helper.messageToast(UpdateDataDiri.this,""+ket);
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                        }

                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        helper.messageToast(UpdateDataDiri.this,"photo max 1 MB");
                        mProgressDialog.dismiss();

                    }
                });
    }




    private void loadprofile(){
        etNama.setText(session.getNamaLEngkap().equals("")||session.getNamaLEngkap().equals("null")? "":session.getNamaLEngkap());
        etKtp.setText(session.getNik().equals("")||session.getNik().equals("null")?"":session.getNik());
        etJabatan.setText(session.getJabatan().equals("")||session.getJabatan().equals("null")?"":session.getJabatan());
        etTgl_lahir.setText(session.getTglLahir().equals("")||session.getTglLahir().equals("null")?"":session.getTglLahir());
        etMasukKerja.setText(session.getTglmasuk().equals("")||session.getTglmasuk().equals("null")?"":session.getTglmasuk());
        Log.d("CEK_TGL_LAHIR", "loadprofile: "+session.getTglLahir());

        if (session.getAlamat().equals("")) {
            etAlamatKtp.setText("");
        } else {
            etAlamatKtp.setText(session.getAlamatKtp().equals("null")?"":session.getAlamatKtp().replace(", null", ""));
        }

        if (session.getAlamat_sekarang().equals("")) {
            etAlamatNow.setText("");
        } else {
            etAlamatNow.setText(session.getAlamatNow().equals("null")?"":session.getAlamatNow().replace(", null", ""));
        }


        //etAlamatKtp.setText(session.getAlamat().equals("")||session.getAlamat().equals("null")?"":session.getAlamat());
        //etAlamatNow.setText(session.getAlamat_sekarang().equals("")||session.getAlamat_sekarang().equals("null")?"":session.getAlamat_sekarang());
        etNoHp.setText(session.getKyhp().equals("")||session.getKyhp().equals("null")?"":session.getKyhp());
        etEmail.setText(session.getEmail().equals("")||session.getEmail().equals("null")?"":session.getEmail());
        etNpwp.setText(session.getNpwp().equals("")||session.getNpwp().equals("null")?"":session.getNpwp());


    }



    public void getDataDiri() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
        AndroidNetworking.initialize(UpdateDataDiri.this, okHttpClient);
        AndroidNetworking.post(api.URL_up_get_data_diri_temp)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kyano", kyano)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            if(success){
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String kytptlhr = data.getString("kytptlhr");
                                    String kyalamat_skrang = data.getString("kyalamat_skrang");
                                    String npwp = data.getString("npwp");
                                    etAlamatNow.setText(""+kyalamat_skrang);
                                    etNpwp.setText(""+npwp);
                                    etTempatLahir.setText(""+kytptlhr);
                                }

                            }
                            else{
                                helper.messageToast(UpdateDataDiri.this,"data masih kosong");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_Exception", "onResponse: " + e);
                        } catch (NullPointerException e) {
                            Log.d("JSON_NullPointer", "onResponse: " + e);
                        } catch (NumberFormatException e) {
                            Log.d("JSONFormatException", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(UpdateDataDiri.this, "Peringatan", "" + helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("ANERROR_EXCEPTION", "onError: " + anError);
                        //progressDialog.dismiss();
                    }
                });
    }




    private boolean is_valid() {
        boolean valid = true;
        if (etNama.getText().toString().isEmpty()) {
            tilNama.setError("Nama wajib diisi");
            tilNama.requestFocus();
            valid = false;
        }
        if (etKtp.getText().toString().isEmpty()) {
            tilKtp.setError("No Ktp wajib diisi");
            tilKtp.requestFocus();
            valid = false;
        }

//        if (etJabatan.getText().toString().isEmpty()) {
//            tilJabatan.setError("Jabatan wajib diisi");
//            tilJabatan.requestFocus();
//            valid = false;
//        }

        if (etTgl_lahir.getText().toString().isEmpty()) {
            tilTgl_lahir.setError("Tanggal Lahir wajib diisi");
            tilTgl_lahir.requestFocus();
            valid = false;
        }
        if (etMasukKerja.getText().toString().isEmpty()) {
            tilMasukKerja.setError("Tanggal masuk kerja wajib diisi");
            tilMasukKerja.requestFocus();
            valid = false;
        }
        if (etAlamatKtp.getText().toString().isEmpty()) {
            tilAlamatKtp.setError("Alamat ktp wajib diisi");
            tilAlamatKtp.requestFocus();
            valid = false;
        }

        if (etAlamatNow.getText().toString().isEmpty()) {
            tilAlamatNow.setError("Alamat sekarang wajib diisi");
            tilAlamatNow.requestFocus();
            valid = false;
        }


        if (etNoHp.getText().toString().isEmpty()) {
            tilNoHp.setError("No Hp wajib diisi");
            tilNoHp.requestFocus();
            valid = false;
        }

        if (etEmail.getText().toString().isEmpty()) {
            tilEmail.setError("email wajib diisi");
            tilEmail.requestFocus();
            valid = false;
        }

        if (etTempatLahir.getText().toString().isEmpty()) {
            tIlTempatLahir.setError("tempat lahir wajib diisi");
            tIlTempatLahir.requestFocus();
            valid = false;
        }

        if(name_ktp.equals("")){
            helper.messageToast(UpdateDataDiri.this,"foto ktp belum diisi");
            valid = false;
        }

        if(name_kk.equals("")){
            helper.messageToast(UpdateDataDiri.this,"foto kk belum diisi");
            valid = false;
        }

        if(size_image_ktp==true){
            helper.messageToast(UpdateDataDiri.this,"ukuran image harus kurang dari 2 MB");
            valid = false;
        }

        if(size_image_kk==true){
            helper.messageToast(UpdateDataDiri.this,"ukuran image harus kurang dari 2 MB");
            valid = false;
        }

        if(size_image_npwp==true){
            helper.messageToast(UpdateDataDiri.this,"ukuran image harus kurang dari 2 MB");
            valid = false;
        }



        return valid;
    }

    private void reset_validation() {
        tilNama.setErrorEnabled(false);
        tilKtp.setErrorEnabled(false);
        tilTgl_lahir.setErrorEnabled(false);
        tilMasukKerja.setErrorEnabled(false);
        tilAlamatKtp.setErrorEnabled(false);
        tilAlamatNow.setErrorEnabled(false);
        tilNoHp.setErrorEnabled(false);
        tilEmail.setErrorEnabled(false);
        tIlTempatLahir.setErrorEnabled(false);

    }
    private void showDateDialogTglLahir(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                //bulan=""+dateFormatter.format(newDate.getTime());
                etTgl_lahir.setText(""+dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private void showDateDialogTglMasuk(){
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                //bulan=""+dateFormatter.format(newDate.getTime());
                etMasukKerja.setText(""+dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }




    private void getAlamat(){
        if (session.getAlamatKtp().equals("")) {
            etAlamatKtp.setText("");
        } else {
            etAlamatKtp.setText(session.getAlamatKtp().equals("null")?"":session.getAlamatKtp().replace(", null", ""));
        }

        if (session.getAlamatNow().equals("")) {
            etAlamatNow.setText("");
        } else {
            etAlamatNow.setText(session.getAlamatNow().equals("null")?"":session.getAlamatNow().replace(", null", ""));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getAlamat();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == 1 && resultCode == Activity.RESULT_OK){
                final Uri imageUri = data.getData();
                resultUri = imageUri;
                String realPath;
                compressedImages=new File(getRealPathFromURI(imageUri,UpdateDataDiri.this));
                Log.d("ENCODED_IMAGE_1", "onActivityResult: "+compressedImages.getName());
                setCompressedImage();


            }
        }catch (Exception e){
            Log.d("TAKE_GALERI", "onActivityResult: "+e);
        }
    }


    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }




    private void setCompressedImage() {
        if(bool_imgKtp){
            compressedImage_ktp=compressedImages;
            imgKtp.setImageBitmap(BitmapFactory.decodeFile(compressedImage_ktp.getAbsolutePath()));
            name_ktp=compressedImage_ktp.getPath().substring(compressedImage_ktp.getPath().lastIndexOf("/")+1);
            size_imgKtp = Integer.parseInt(String.valueOf(compressedImage_ktp.length()/1024));
            tx_size_ktp.setVisibility(View.VISIBLE);
            tx_size_ktp.setText(""+helper.cheking_size(size_imgKtp));
            Log.d("ukuran_file_ktp", "setCompressedImage: "+size_imgKtp);
            if(size_imgKtp>2048){
                size_image_ktp=true;
            }else{
                size_image_ktp=false;
            }


        }if(bool_imgKk){
            compressedImage_kk=compressedImages;
            imgKK.setImageBitmap(BitmapFactory.decodeFile(compressedImage_kk.getAbsolutePath()));
            name_kk=compressedImage_kk.getPath().substring(compressedImage_kk.getPath().lastIndexOf("/")+1);
            size_imgKK = Integer.parseInt(String.valueOf(compressedImage_kk.length()/1024));
            tx_size_kk.setVisibility(View.VISIBLE);
            tx_size_kk.setText(""+helper.cheking_size(size_imgKK));
            Log.d("cek_value", "setCompressedImage: cek_klik_kk");
            if(size_imgKK>2048){
                size_image_kk=true;
            }else{
                size_image_kk=false;
            }

        }if(bool_imgNpwp){
            compressedImage_npwp=compressedImages;
            imgNpwp.setImageBitmap(BitmapFactory.decodeFile( compressedImage_npwp.getAbsolutePath()));
            name_npwp =compressedImage_npwp.getPath().substring(compressedImage_npwp.getPath().lastIndexOf("/")+1);
            size_imgNpwp = Integer.parseInt(String.valueOf(compressedImage_npwp.length()/1024));
            tx_size_npwp.setVisibility(View.VISIBLE);
            tx_size_npwp.setText(""+helper.cheking_size(size_imgNpwp));
            Log.d("cek_value", "setCompressedImage: cek_klik_ktp");
            if(size_imgNpwp>2048){
                size_image_npwp=true;
            }else{
                size_image_npwp=false;
            }
        }

        Log.d("CEK_BOOL", "setCompressedImage: "+bool_imgNpwp);
    }




    private void clearImage() {
        imgKK.setBackgroundColor(getRandomColor());
        imgKK.setImageDrawable(null);
        //compressedSizeTextView.setText("Size : -");
    }

    private int getRandomColor() {
        Random rand = new Random();
        return Color.argb(100, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }



    private void openWhatsApp(String numero,String mensaje){

        try{
            PackageManager packageManager = UpdateDataDiri.this.getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone="+ numero +"&text=" + URLEncoder.encode(mensaje, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }else {
                Toast.makeText(UpdateDataDiri.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            Log.e("ERROR WHATSAPP",e.toString());
            Toast.makeText(UpdateDataDiri.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
        }

    }

    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }




}