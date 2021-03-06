package com.app.mobiledev.apphris.bonus.bonus_promosi_proyek;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.helperPackage.DataSoalSQLite;
import com.app.mobiledev.apphris.helperPackage.helper;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mindorks.paracamera.Camera;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;

public class edit_form_bonus_proyek extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private Boolean mLocationPermissionGranted = false;
    private Location mLastLocation;
    private TextView tvLokasi,txIdTrkunjungan;
    private String lokasi;
    private String idform="";
    private ArrayList<String> list_jenis_proyek;
    private ArrayList<String> list_jenis_cust;
    private double lat = 0;
    private String TAG = "FORM_BONUS_PROYEK";
    private String kyano="";
    private double lon = 0;
    private Button btnTeam,btnSimpan;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 34;
    private Spinner spinJenis,spin_status_proyek;
    private RecyclerView rc_data_karyawan;
    private DataSoalSQLite db;
    private List<model_add_karywan> model_add_karyawans;
    private add_karyawan dialog_add_karyawan;
    private CoordinatorLayout coor_form;
    private ImageView image_dp,image_so;
    private Bitmap imageFoto_dp,imageFoto_so;
    private Camera camera;
    private String encodedimage_dp = "";
    private String encodedimage_so = "";
    private EditText edTelp,edTelpPic,edTahapan,edTgl,edJam,edNamaProyek,edCust,edPic,edStatus_proyek;
    private SessionManager sessionmanager;
    private String arrayData;
    private LinearLayout progress_wait;
    private Toolbar toolbar_bonusProyek;
    private String kjnkd="",jenis_proyek="",status_proyek,url_image_dp="",url_image_so="";
    private String[] jenis_pembayaran = new String[]{"Booking", "Dp","Lunas"};
    private String jenis_foto="";
    private String[] status_pryk = new String[]{"Prospek", "Deal","Kunjungan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_bonus_proyek);
        edTgl=findViewById(R.id.edTgl);
        edJam=findViewById(R.id.edJam);
        edCust=findViewById(R.id.edCust);
        txIdTrkunjungan=findViewById(R.id.txIdTrkunjungan);
        edTelp=findViewById(R.id.edTelp);
        edNamaProyek=findViewById(R.id.edNamaProyek);
        edTelpPic=findViewById(R.id.edTelpPic);
        progress_wait=findViewById(R.id.progress_wait);
        edTahapan=findViewById(R.id.edTahapan);
        toolbar_bonusProyek=findViewById(R.id.toolbar_bonusProyek);
        toolbar_bonusProyek.setTitle("Form Edit Bonus Proyek");
        setSupportActionBar(toolbar_bonusProyek);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edPic=findViewById(R.id.edPic);
        sessionmanager = new SessionManager(edit_form_bonus_proyek.this);
        kyano = sessionmanager.getIdUser();
        tvLokasi=findViewById(R.id.tvLokasi);
        spinJenis=findViewById(R.id.spinJenis);
        spin_status_proyek=findViewById(R.id.spin_status_proyek);
        rc_data_karyawan=findViewById(R.id.rc_data_karyawan);
        coor_form=findViewById(R.id.form_bonus_proyek);
        btnSimpan=findViewById(R.id.btnSimpan);
        edJam.setEnabled(false);
        edTgl.setEnabled(false);
        btnTeam=findViewById(R.id.btnTeam);
        image_dp = findViewById(R.id.image_dp);
        image_so = findViewById(R.id.image_so);
        db = new DataSoalSQLite(edit_form_bonus_proyek.this);
        dialog_add_karyawan = new add_karyawan();
        model_add_karyawans = new ArrayList<>();
        btnSimpan.setText("UPDATE");
        getLocationPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (locationProviders == null || locationProviders.equals("")) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        btnTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_add_karyawan.show(getSupportFragmentManager(), "Input karyawan");
            }
        });





        toolbar_bonusProyek.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    try {
                        mMap.clear();
                        mLastLocation = location;
                        LatLng mylokasi = new LatLng(location.getLatitude(), location.getLongitude());
                        getLokasi(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylokasi));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                        mMap.addMarker(new MarkerOptions().position(mylokasi)
                                .title("My Lokasi").icon(helper.markerMap(edit_form_bonus_proyek.this, R.drawable.ic_marker_map)));



                    } catch (Exception e) {
                        Log.d("LOCATION_CALL_BACK", "onLocationResult: " + e);
                    }

                }
            }
        };


        image_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status_proyek.equals("Deal")){
                    helper.snackBar(coor_form,"form sudah deal anda tidak dapat merubah data..!!");
                }else{
                    camera = new Camera.Builder()
                            .resetToCorrectOrientation(true)
                            .setTakePhotoRequestCode(1)
                            .setDirectory("pics")
                            .setName("ali_" + System.currentTimeMillis())
                            .setImageFormat(Camera.IMAGE_JPEG)
                            .setCompression(75)
                            .setImageHeight(1000)
                            .build(edit_form_bonus_proyek.this);
                    try {
                        camera.takePicture();
                        jenis_foto="foto_dp";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        image_so.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status_proyek.equals("Deal")){
                    helper.snackBar(coor_form,"form sudah deal anda tidak dapat merubah data..!!");
                }else{
                    camera = new Camera.Builder()
                            .resetToCorrectOrientation(true)
                            .setTakePhotoRequestCode(1)
                            .setDirectory("pics")
                            .setName("ali_" + System.currentTimeMillis())
                            .setImageFormat(Camera.IMAGE_JPEG)
                            .setCompression(75)
                            .setImageHeight(1000)
                            .build(edit_form_bonus_proyek.this);
                    try {
                        camera.takePicture();
                        jenis_foto="foto_so";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status_proyek.equals("Deal")){
                    helper.snackBar(coor_form,"form sudah deal anda tidak dapat merubah data..!!");
                }else{
                    cekvalidasi();
                }

            }
        });
        getintent_kjnkd();
        sp_status_proyek();
        spjenisProyek();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
                Bitmap bitmap = camera.getCameraBitmap();

                if(jenis_foto.equals("foto_dp")){

                    if (bitmap != null) {
                        image_dp.setImageBitmap(bitmap);
                        imageFoto_dp = bitmap;
                        imageFoto_dp = Bitmap.createScaledBitmap(imageFoto_dp, 500, 500, false);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        imageFoto_dp.compress(Bitmap.CompressFormat.PNG, 50, bytes);
                        encodedimage_dp = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);


                    } else {
                        Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
                    }

                }else{

                    if (bitmap != null) {
                        image_so.setImageBitmap(bitmap);
                        imageFoto_so = bitmap;
                        imageFoto_so = Bitmap.createScaledBitmap(imageFoto_so, 500, 500, false);
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        imageFoto_so.compress(Bitmap.CompressFormat.PNG, 50, bytes);
                        encodedimage_so = Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);
                    } else {
                        Toast.makeText(this.getApplicationContext(), "Picture not taken!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        } catch (Exception e) {
            Log.d("TAKE_CAMERA", "onActivityResult: " + e);
        }

    }




    public void getLokasi(double llat, double llon) {
        try {
            String lok_lat = "";
            String lok_lon = "";
            if (llat == 0) {
                Log.d("CEK_LOKASI", "getLokasi: " + llat);
            } else {
                if(idform.equals("")){
                    lat = llat;
                    lon = llon;
                }else{
                    String[] split_lokasi = lokasi.split(",");
                    lok_lat = split_lokasi[0];
                    lok_lon = split_lokasi[1];
                    Log.d(TAG, "getLokasi lok_lon: "+lok_lon);
                }

                Log.d("LOKASI", "getLokasi: " + llat);
                Geocoder geocoder = new Geocoder(edit_form_bonus_proyek.this, Locale.getDefault());
                String result = null;
                List<Address> addressList=null;

                if(idform.equals("")){
                    addressList = geocoder.getFromLocation(lat, lon, 1);
                }else{
                    addressList = geocoder.getFromLocation(Double.parseDouble(lok_lat), Double.parseDouble(lok_lon), 1);
                }


                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)); //.append("\n");
                    }
                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    Log.d("getLOKASI", "getLokasi: " + address.getAddressLine(0));
                    //tvLokasi.setText(""+address.getAddressLine(0));
                    tvLokasi.setText("" + address.getAddressLine(0));
                    result = sb.toString();
                }


            }
        } catch (IOException e) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        } catch (NullPointerException e) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        }
    }
    private void cekvalidasi(){
        String jam=edJam.getText().toString();
        String tgl=edTgl.getText().toString();
        String tahapan=edTahapan.getText().toString();
        String telp=edTelp.getText().toString();
        String telp_pic=edTelpPic.getText().toString();
        int jns_proyek=spinJenis.getSelectedItemPosition()+1;
        String nama_proyek=edNamaProyek.getText().toString();
        String alamat=tvLokasi.getText().toString();
        String cust=edCust.getText().toString();
        String nama_pic=edPic.getText().toString();
        String idtrkunjungan=txIdTrkunjungan.getText().toString();
        String status_proyek=spin_status_proyek.getSelectedItem().toString();

        try {
            if(jam.equals("")){
                helper.snackBar(coor_form,"jam belum diisi..!!");
            }else if(tgl.equals("")){
                helper.snackBar(coor_form,"tgl belum diisi..!!");

            }else if(tahapan.equals("")){
                helper.snackBar(coor_form,"tahapan belum diisi..!!");

            }else if(telp.equals("")){
                helper.snackBar(coor_form,"no telp owner belum diisi..!!");

            }else if(telp_pic.equals("")){
                helper.snackBar(coor_form,"nama PIC owner belum diisi..!!");
            }
            else if(nama_proyek.equals("")){
                helper.snackBar(coor_form,"nama proyek belum diisi..!!");

            } else if(alamat.equals("")){
                helper.snackBar(coor_form,"alamat proyek belum diisi..!!");

            } else if(cust.equals("")){
                helper.snackBar(coor_form,"nama customer proyek belum diisi..!!");

            } else if(nama_pic.equals("")){
                helper.snackBar(coor_form,"nama PIC proyek belum diisi..!!");

            }



            else {
                if(status_proyek.equals("Deal")){

                        if(imageFoto_so==null){
                            helper.snackBar(coor_form,"foto bukti so belum diisi.....!!");
                        }

                        else if (imageFoto_dp==null){
                            helper.snackBar(coor_form,"foto bukti dp belum diisi.....!!");
                        }else{
                            progress_wait.setVisibility(View.VISIBLE);
                            updateTrkunjungan(kjnkd,nama_proyek,telp,telp_pic,telp_pic,nama_pic,encodedimage_dp,encodedimage_so,"YES",String.valueOf(jns_proyek),status_proyek,tgl,jam);
                        }
                }
                else{
                    if(imageFoto_so==null&&imageFoto_dp==null){
                        progress_wait.setVisibility(View.VISIBLE);
                        updateTrkunjungan(kjnkd,nama_proyek,telp,telp_pic,telp_pic,nama_pic,encodedimage_dp,encodedimage_so,"NO",String.valueOf(jns_proyek),status_proyek,tgl,jam);
                    }else{
                        progress_wait.setVisibility(View.VISIBLE);
                        updateTrkunjungan(kjnkd,nama_proyek,telp,telp_pic,telp_pic,nama_pic,encodedimage_dp,encodedimage_so,"YES",String.valueOf(jns_proyek),status_proyek,tgl,jam);
                    }

                }
            }



        }catch (NullPointerException e){
            Log.d(TAG, "cekvalidasi: "+e);
        }


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private String dateFormatServer(String date) {
        String finalDateString="";
        String strDate = date;
        SimpleDateFormat sdfnewformat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate;
        try {
            convertedDate = sdfnewformat.parse(strDate);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            finalDateString = dateFormat.format(convertedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return finalDateString;

    }

    public String getQueryKarywan(String tgl,String idTrKunjungan) {
        SQLiteDatabase ReadData = db.getReadableDatabase();
        Cursor cursor = ReadData.rawQuery("SELECT * FROM tblkaryawan", null);
        cursor.moveToFirst();
        ArrayList<String> dataLoop = new ArrayList<String>();
        String sql="INSERT INTO msteam(kjnkd,tmseq,kyano,kynm,tmupdt)" +
                "VALUES ";
        String value="";
        String data="";
        String  n="N";
        for (int count = 0; count < cursor.getCount(); count++) {
            cursor.moveToPosition(count);
            value += "("
                    + "'" + idTrKunjungan + "',"
                    + "'" + cursor.getString(0) + "',"
                    + "'" + cursor.getString(1) + "',"
                    + "'" + cursor.getString(2) + "',"
                    + "'" + tgl + "'"
                    + ")";
            value += ",";
        }
        if(value.equals("")){
            arrayData="";
        }else{
            value = removeLastChar(value);
            value += ";";
            arrayData=sql+value;
        }

        return arrayData;

    }

    public String removeLastChar(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return s.substring(0, s.length() - 1);
    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }


    private void getDtailTrkunjungan(final String kjnkds) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post(api.URL_getDetailTrkunjungan)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kjnkd", kjnkds)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            RequestOptions requestOptions = new RequestOptions()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true);
                            if(success){
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String kjnkd=data.getString("kjnkd");
                                    String nmProyek=data.getString("nmProyek");
                                    String alamtProyek=data.getString("alamtProyek");
                                    String customer=data.getString("customer");
                                    String noTelp=data.getString("noTelp");
                                    String pic=data.getString("pic");
                                    String noTelppic=data.getString("noTelppic");
                                    String jam=data.getString("jam");
                                    String tgl=data.getString("tgl");
                                    String lokasi=data.getString("lokasi");
                                    url_image_dp=data.getString("image_dp");
                                    url_image_so=data.getString("image_so");
                                    String status=data.getString("status");
                                    String status_proyek=data.getString("status_proyek");
                                    String thpProyek=data.getString("thpProyek");


                                    edNamaProyek.setText(""+nmProyek);
                                    edCust.setText(""+customer);
                                    edTelp.setText(""+noTelp);
                                    edTelpPic.setText(""+noTelppic);
                                    edJam.setText(""+jam);
                                    edPic.setText(""+pic);
                                    edTahapan.setText(""+thpProyek);
                                    edTgl.setText(""+tgl);
                                    tvLokasi.setText(""+alamtProyek);
                                    edStatus_proyek.setText(""+status_proyek);

                                    Log.d("URL_IMAGE_CEK", "onResponse: "+api.URL_foto_form_bonus_proyek+""+url_image_dp);
                                }

//                                if(!ktgproyek.equals("")){
//                                    int setktg=Integer.parseInt(ktgproyek);
//                                    spinKtg.setSelection(setktg);
//                                }else{
//                                    spinKtg.setSelection(0);
//                                }
//
//                                if(!jenis.equals("")){
//                                    int setjns=Integer.parseInt(jenis);
//                                    spinJenis.setSelection(setjns);
//                                }else{
//                                    spinJenis.setSelection(0);
//                                }



                            }else{
                                helper.snackBar(coor_form,"data tidak ditemukan...!!!!! "+success);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            helper.snackBar(coor_form,""+helper.PESAN_SERVER);
                            Log.d(TAG, "onResponse: "+e);
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }
                        catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        helper.snackBar(coor_form,""+helper.PESAN_KONEKSI);
                    }
                });
    }


    private void getTeam(final String kjnkds) {
        AndroidNetworking.post(api.URL_getDetailTeam)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kjnkd", kjnkds)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("success");
                            model_add_karyawans.clear();
                            if(success){
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String kjnkd=data.getString("kjnkd");
                                    String tmseq=data.getString("tmseq");
                                    String kyano=data.getString("kyano");
                                    String kynm=data.getString("kynm");
                                    String dvnama=data.getString("dvnama");
                                    String tmupdt=data.getString("tmupdt");

                                    model_add_karywan model = new model_add_karywan();
                                    model.setKynm(kynm);
                                    model.setKyano(kyano);
                                    model.setDvnama(dvnama);
                                    model_add_karyawans.add(model);

                                }
                                adapter_add_karyawan mAdapter;
                                mAdapter = new adapter_add_karyawan(model_add_karyawans, edit_form_bonus_proyek.this,"view");
                                rc_data_karyawan.setLayoutManager(new LinearLayoutManager(edit_form_bonus_proyek.this));
                                rc_data_karyawan.setItemAnimator(new DefaultItemAnimator());
                                rc_data_karyawan.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();


                            }else{
                                helper.snackBar(coor_form,"data tidak ditemukan...!!!!! "+success);
                            }

                        }catch (JSONException e) {
                            e.printStackTrace();
                            helper.snackBar(coor_form,""+helper.PESAN_SERVER);
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }
                        catch (IndexOutOfBoundsException e){
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);
                        helper.snackBar(coor_form,""+helper.PESAN_KONEKSI);
                    }
                });
    }
    private void getintent_kjnkd() {
        kjnkd=getIntent().getExtras().getString("kjnkd");
        jenis_proyek=getIntent().getExtras().getString("jenis_proyek");
        status_proyek=getIntent().getExtras().getString("status_proyek");
        if(!kjnkd.equals("")){
            getDtailTrkunjungan(kjnkd);
            getTeam(kjnkd);
            btnTeam.setClickable(false);
            Log.d(TAG, "getintent: "+lokasi);
        }
    }

    private void updateTrkunjungan( final String kjnkd,final String nm_proyek,final String customer,final String no_telp, final String no_telp_pic,
                                    final String pic,final String img_dp,final String img_so,final String upload_foto,final String jns_proyek,final String stat_proyek,final String tgl_proyek,final String jam_proyek){
        AndroidNetworking.post(api.URL_updateTrkunjungan)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kjnkd", kjnkd)
                .addBodyParameter("kyano", kyano)
                .addBodyParameter("nm_Proyek", nm_proyek)
                .addBodyParameter("customer", customer)
                .addBodyParameter("no_telp", no_telp)
                .addBodyParameter("no_telp_pic", no_telp_pic)
                .addBodyParameter("pic", pic)
                .addBodyParameter("jenis_proyek", jns_proyek)
                .addBodyParameter("status_proyek", stat_proyek)
                .addBodyParameter("jam_Proyek", jam_proyek)
                .addBodyParameter("tgl_Proyek", tgl_proyek)
                .addBodyParameter("upload_foto", upload_foto)
                .addBodyParameter("img_dp", "data:image/PNG;base64,"+img_dp)
                .addBodyParameter("img_so", "data:image/PNG;base64,"+img_so)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        model_add_karyawans.clear();
                        Log.d("RESPONSE_ERROR", "onResponse: "+response);
                        try {


                            Boolean success = response.getBoolean("success");
                            if (success) {
                                String ket = response.getString("ket");
                                new SweetAlertDialog(edit_form_bonus_proyek.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Informasi")
                                        .setContentText(""+ket)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        })
                                        .show();
                            } else{
                                helper.snackBar(coor_form,"gagal disimpan");

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORTRKUNJUNGAN", "onResponse: "+e);
                            helper.snackBar(coor_form,""+helper.PESAN_SERVER);
                        }catch (NullPointerException e){
                            Log.d("JSONERORTRKUNJUNGAN", "onResponse: "+e);
                            helper.snackBar(coor_form,""+helper.PESAN_SERVER);
                        }

                        progress_wait.setVisibility(View.GONE);
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTION", "onError: "+anError.getMessage());
                        helper.snackBar(coor_form,""+helper.PESAN_KONEKSI);
                        progress_wait.setVisibility(View.GONE);

                    }
                });




        image_dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera = new Camera.Builder()
                        .resetToCorrectOrientation(true)
                        .setTakePhotoRequestCode(1)
                        .setDirectory("pics")
                        .setName("ali_" + System.currentTimeMillis())
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .setCompression(75)
                        .setImageHeight(1000)
                        .build(edit_form_bonus_proyek.this);
                try {
                    camera.takePicture();
                    jenis_foto="foto_dp";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        image_so.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera = new Camera.Builder()
                        .resetToCorrectOrientation(true)
                        .setTakePhotoRequestCode(1)
                        .setDirectory("pics")
                        .setName("ali_" + System.currentTimeMillis())
                        .setImageFormat(Camera.IMAGE_JPEG)
                        .setCompression(75)
                        .setImageHeight(1000)
                        .build(edit_form_bonus_proyek.this);
                try {
                    camera.takePicture();
                    jenis_foto="foto_so";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekvalidasi();
            }
        });

    }

    private void sp_status_proyek() {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter(this, R.layout.spinner_item, status_pryk);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spin_status_proyek.setAdapter(spinnerArrayAdapter);
    }


    private void spjenisProyek() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        AndroidNetworking.post(api.URL_jenis_proyek)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .setOkHttpClient(okHttpClient)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            list_jenis_proyek = new ArrayList<>();
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String kode = data.getString("jnkd");
                                    String jenis = data.getString("jenis");
                                    list_jenis_proyek.add(kode+"-"+jenis);
                                }
                            }
                            spinJenis.setAdapter(new ArrayAdapter<String>(edit_form_bonus_proyek.this, R.layout.llissimple, list_jenis_proyek));


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONJenis_proyek", "onResponse: " + e);


                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Log.d("JSONJenis_proyek", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_Jenis_proyek", "onError: " + anError);

                    }
                });
    }



}
