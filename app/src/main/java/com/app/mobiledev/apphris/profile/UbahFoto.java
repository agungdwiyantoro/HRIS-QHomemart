package com.app.mobiledev.apphris.profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.mindorks.paracamera.Camera;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UbahFoto extends AppCompatActivity {

    private TextView nama;
    private EditText pass;
    private Camera camera;
    private Button btnSimpan;
    private FaceDetector detector;
    private CheckBox checkBox;
    private SessionManager sessionmanager;
    private String kyano,namas,namaLengkap,cekStaff,password;
    private Toolbar mToolbar;
    private  TextView txtSampleDesc;
    private ProgressDialog mProgressDialog;
    private TextInputLayout tlNama;
    private CircleImageView foto_profil;
    private Uri resultUri;
    private Bitmap image_bmap;
    int currentIndex = 0;
    private int[] imageArray;
    private Uri imageUri;
    private String url_foto="";
    private String encodedimage="";
    private Bitmap editedBitmap;
    private Drawable drawable;
    private int face_count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_foto);

        btnSimpan=findViewById(R.id.btnSimpan);

        mToolbar = findViewById(R.id.toolbarUbahFoto);
        mToolbar.setTitle("Ubah Foto Profile");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtSampleDesc = findViewById(R.id.txtSampleDescription);
        foto_profil=findViewById(R.id.foto_profil);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading ...");
        AndroidNetworking.initialize(getApplicationContext());

        detector = new FaceDetector.Builder(UbahFoto.this)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setMode(FaceDetector.FAST_MODE)
                .build();
        sessionmanager = new SessionManager(UbahFoto.this);
        kyano=sessionmanager.getIdUser();
        namas=sessionmanager.getUsername();
        password=sessionmanager.getPass();
        namaLengkap=sessionmanager.getNamaLEngkap();
        cekStaff=sessionmanager.getCekStaff();

        Log.d("DATA_DIRI", "onCreate: "+password+" USER"+namaLengkap);
        mProgressDialog.show();

        getlImageProfil(kyano);

        foto_profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, 1);
                camera = new Camera.Builder()
                        .resetToCorrectOrientation(true)
                        .setTakePhotoRequestCode(1)
                        .setDirectory("pics")
                        .setName("ali_" + System.currentTimeMillis())
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .setCompression(75)
                        .setImageHeight(1000)
                        .build(UbahFoto.this);
                try {
                    camera.takePicture();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();

                face_count=processImage();
                if(face_count==1){
                    insertImage();
                }else{
                    helper.showMsg(UbahFoto.this,"Informasi","foto harus satu orang atau wajah anda tidak terdeteksi");
                    mProgressDialog.dismiss();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            mProgressDialog.show();
            if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = camera.getCameraBitmap();
                foto_profil.setImageBitmap(bitmap);
                image_bmap=bitmap;
                image_bmap=Bitmap.createScaledBitmap(image_bmap, 500, 500, false);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                image_bmap.compress(Bitmap.CompressFormat.PNG, 50, bytes);
                encodedimage= Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);

                /*face_count=processImage();
                if(face_count==1){
                    insertImage();
                }else{
                    helper2.showMsg(UbahFoto.this,"Informasi","foto harus satu orang atau wajah anda tidak terdeteksi");
                }*/

            }
            mProgressDialog.dismiss();

        }catch (Exception e){
            Log.d("TAKE_GALERI", "onActivityResult: "+e);
            mProgressDialog.dismiss();
        }


    }

    private Integer processImage() {
        int jml_face=0;
        Bitmap bitmap = image_bmap;
        if (detector.isOperational() && bitmap != null) {
            editedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                    .getHeight(), bitmap.getConfig());
            float scale = getResources().getDisplayMetrics().density;
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.GREEN);
            paint.setTextSize((int) (16 * scale));
            paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(6f);
            Canvas canvas = new Canvas(editedBitmap);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            Frame frame = new Frame.Builder().setBitmap(editedBitmap).build();
            SparseArray<Face> faces = detector.detect(frame);
            txtSampleDesc.setText(null);

            for (int index = 0; index < faces.size(); ++index) {
                Face face = faces.valueAt(index);
                canvas.drawRect(
                        face.getPosition().x,
                        face.getPosition().y,
                        face.getPosition().x + face.getWidth(),
                        face.getPosition().y + face.getHeight(), paint);
                canvas.drawText("Face " + (index + 1), face.getPosition().x + face.getWidth(), face.getPosition().y + face.getHeight(), paint);
                jml_face=index + 1;

                for (Landmark landmark : face.getLandmarks()) {
                    int cx = (int) (landmark.getPosition().x);
                    int cy = (int) (landmark.getPosition().y);
                    canvas.drawCircle(cx, cy, 8, paint);
                }


            }

            if (faces.size() == 0) {
            } else {
                foto_profil.setImageBitmap(editedBitmap);

            }
        } else {
            txtSampleDesc.setText("");
        }
        return jml_face;
    }

    private void insertImage(){
        AndroidNetworking.post(api.URL_insert_foto_profil)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("key", api.key)
                .addBodyParameter("img_fp", "data:image/png;base64,"+encodedimage)
                .addBodyParameter("tgl", getDateNow())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("status");
                            String ket = response.getString("ket");
                            if(success){
                                helper.showMsg(UbahFoto.this,"informasi",""+ket);

                                finish();
                            }else{
                                helper.showMsg(UbahFoto.this,"informasi",""+ket);
                            }
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(UbahFoto.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(UbahFoto.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();
                    }
                });
    }

    public String getDateNow(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return  formattedDate;
    }

    private void getlImageProfil(final String kyano){
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
                                url_foto=data;
                                RequestOptions requestOptions = new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true);
                                Glide.with(UbahFoto.this).load(api.get_url_foto_profil(kyano,url_foto)).thumbnail(Glide.with(UbahFoto.this).load(R.drawable.loading)).apply(requestOptions).into(foto_profil);
                            } else {
                                Log.d("", "onResponse: "+data);
                            }

                            //getPassword(kyano);
                            mProgressDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);
                            helper.showMsg(UbahFoto.this, "Peringatan", ""+helper.PESAN_SERVER, helper.ERROR_TYPE);
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        helper.showMsg(UbahFoto.this, "Peringatan", ""+helper.PESAN_KONEKSI, helper.ERROR_TYPE);
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        mProgressDialog.dismiss();
                    }
                });
    }

}
