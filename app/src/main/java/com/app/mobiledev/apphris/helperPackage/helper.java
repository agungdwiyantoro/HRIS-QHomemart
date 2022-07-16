package com.app.mobiledev.apphris.helperPackage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.app.mobiledev.apphris.R;
import com.app.mobiledev.apphris.api.api;
import com.app.mobiledev.apphris.sesion.SessionManager;
import com.app.mobiledev.apphris.update_layout;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Credentials;

public class helper extends AsyncTask {

    public static final int NORMAL_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int NOTIF_ID = 56;
    public static final int WARNING_TYPE = 3;
    public static final int CUSTOM_IMAGE_TYPE = 4;
    public static final int PROGRESS_TYPE = 5;
    static SessionManager sessionManager;
    public static final int QRcodeWidth = 500;
    public static final double latitude_bj = -7.795774;
    public static final double longtitude_bj = 110.409442;
    public static final String PESAN_KONEKSI = "Koneksi internet bermasalah";
    public static final String PESAN_SERVER = "Terdapat kesalahan pada\nserver";
    public static final String Name = "sales_edit";
    public static final int REQUEST_READ_PHONE_STATE = 8;
    SharedPreferences sharedpreferences;
    static Locale localeID = new Locale("in", "ID");
    static NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static LayoutInflater inflater;
    private static  View dialogView;
    private static AlertDialog.Builder dialog;
    private static  String TAG="HELPER";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @SuppressLint("DefaultLocale")
    public static String rp(Double value) {
        if (value % 1 > 0) {
            return String.format("%,.2f", value);
        } else {
            return String.format("%,.0f", value);
        }
    }

    public static String format_tgl(String tgl, String data) {
        if (!tgl.equals("")) {
            return tgl.substring(8, 10).replace("/", "").replace("-", "") + "/"
                    + tgl.substring(5, 7).replace("/", "").replace("-", "")
                    + "/" + tgl.substring(0, 4).replace("/", "").replace("-", "");
        } else {
            return "";
        }
    }

    public static  void disabledEditText(EditText edit){
        edit.setFocusable(false);
        edit.setCursorVisible(false);
        edit.setKeyListener(null);
    }

    public static void update(final String versi, final Context mctx) {
        AndroidNetworking.post(api.URL_get_url_version)
                .addBodyParameter("versi", versi)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Boolean success = response.getBoolean("status");
                            if (success) {
                                sessionManager = new SessionManager(mctx);
                                sessionManager.checkLogin();


                            } else {
                                Intent load1 = new Intent(mctx, update_layout.class);
                                mctx.startActivity(load1);
                            }
                            Log.d("CEK_UPDATE", "onResponse: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            helper.showMsg(mctx, "informasi", "" + helper.PESAN_SERVER);
                            Log.d("JSONUPDATE", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_UPDATE", "onError: " + anError);
                        helper.showMsg(mctx, "informasi", "" + helper.PESAN_KONEKSI);
                    }
                });
    }

    public static void showMsg(Context context, String title, String msg) {
        showMsg(context, title, msg, NORMAL_TYPE);
    }

    public static void showMsg(Context context, String title, String msg, SweetAlertDialog.OnSweetClickListener callback) {
        showMsg(context, title, msg, helper.NORMAL_TYPE, callback);
    }

    public static void showMsg(Context context, String title, String msg, int type) {
        new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setContentText(msg)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }

    public static void showMsg(Context context, String title, String msg, int type, SweetAlertDialog.OnSweetClickListener callback) {
        new SweetAlertDialog(context, type)
                .setTitleText(title)
                .setConfirmClickListener(callback)
                .setContentText(msg).show();
    }

    public static void requestPermissions(Context ctx) {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale((Activity) ctx,
                        Manifest.permission.READ_PHONE_STATE);

        if (shouldProvideRationale) {
            ActivityCompat.requestPermissions((Activity) ctx,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        } else {
            ActivityCompat.requestPermissions((Activity) ctx,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    REQUEST_READ_PHONE_STATE);
        }
    }

    public static void requestPermissionsGps(Context ctx) {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale((Activity) ctx,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            ActivityCompat.requestPermissions((Activity) ctx,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions((Activity) ctx,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    public static void snackBar(View v, String pesan) {
        Snackbar snackbar = Snackbar
                .make(v, pesan, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static String getDeviceIMEI(Context ctx) {
        String deviceUniqueIdentifier = null;
        try {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                deviceUniqueIdentifier = Settings.Secure.getString(
                        ctx.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            } else {
                final TelephonyManager mTelephony = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
                if (mTelephony.getDeviceId() != null) {
                    deviceUniqueIdentifier = mTelephony.getDeviceId();
                } else {
                    deviceUniqueIdentifier = Settings.Secure.getString(
                            ctx.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                }
            }
           // return deviceUniqueIdentifier;
        }catch (SecurityException e){
            Log.d("security_NULL", "getDeviceIMEI: "+e);

        }catch (NullPointerException e){
            Log.d("security_NULL", "getDeviceIMEI: "+e);
        }


        return deviceUniqueIdentifier;
    }


    public static  String getDeviceId(Context ctx){
        String id="";
        id= Settings.Secure.getString(ctx.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return  id;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        String newVersion = null;
        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.app.mobiledev.apphris" + "&hl=it")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select(".hAyfc .htlgb")
                    .get(7)
                    .ownText();

            Log.d("CEK_VER_APP", "doInBackground: "+ newVersion);

            return newVersion;
        } catch (Exception e) {
            return newVersion;
        }
    }
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d("CEK_VER_APP", "playstore version " + o);
    }



    public static BitmapDescriptor markerMap(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_marker_map);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static  void DialogKoneksi(Context ctx) {
        dialog = new AlertDialog.Builder(ctx);
        try {
            inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
            dialogView = inflater.inflate(R.layout.dialog_koneksi_putus, null);
            dialog.setView(dialogView);
            dialog.setCancelable(true);
            dialog.show();


        }catch (NullPointerException e){
            Log.d("NULLPointer", "DialogForm: "+e);
        }

    }
    public static void getLokasi(final Context mctx,String kyano) {
        AndroidNetworking.post(api.URL_getlocation)
                .addBodyParameter("key", api.key)
                .addBodyParameter("kyano", kyano)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        sessionManager = new SessionManager(mctx);
                        try {
                            String lokasi_janti="";
                            String lokasi_piyungan="";
                            String lokasi_janti_lestari="";
                            String lokasi_berbah="";
                            String jarak="";
                            Boolean success = response.getBoolean("success");
                            String kypresensi=response.getString("kypresensi");
                            Log.d("kyprsensi", "onResponse: "+kypresensi);
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String setano=data.getString("setano");
                                    String setchar=data.getString("setchar");


                                    if(setano.equals("lokasi1_ja")){
                                        lokasi_janti=setchar;

                                    }else if(setano.equals("lokasi2_pi")){
                                        lokasi_piyungan=setchar;

                                    }else if(setano.equals("lokasi3_jl")){
                                        lokasi_janti_lestari=setchar;

                                    }
                                    else if(setano.equals("jarak")){
                                        jarak=setchar;
                                    }

                                    else{
                                        lokasi_berbah=setchar;
                                    }

                                }
                                Log.d("CEK_LOKASI", "createLocation: janti :"+lokasi_janti+" lokasi_JL:"+lokasi_janti_lestari+" lokasi2_pi:"+lokasi_piyungan+" lokasi_berbah"+lokasi_berbah);
                                sessionManager.createLocation(lokasi_janti,lokasi_janti_lestari,lokasi_piyungan,lokasi_berbah,jarak,kypresensi);

                            }else{
                                Log.d("DATA_BOOLEAN", "onResponse: "+success);
                            }
                        } catch (JSONException e) {
                            Log.d("EROOR_JSON", "onError: " + e);
                            helper.showMsg(mctx, "informasi", "" + helper.PESAN_SERVER);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_UPDATE", "onError: " + anError);
                       // helper.showMsg(mctx, "informasi", "" + helper.PESAN_KONEKSI);
                    }
                });
    }


    public static String getDateNow(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);
        return  formattedDate;
    }

    public static Date formatDate(DateFormat dataDate, String formatDate) throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(formatDate);
        Date date = formatter.parse(String.valueOf(dataDate));
        return date;
    }

    public static void cekAkses( final Context mctx, final View view ,String menu,Class classMenu, String hakAkses) {
        AndroidNetworking.post(api.URL_getAksesMobile)
                .addBodyParameter("key", api.key)
                .addBodyParameter("menu_mobile", menu)
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
                                    sessionManager = new SessionManager(mctx);
                                    Log.d(TAG, "onResponse: Cek_Divisi_Session1 :"+sessionManager.getKyDivisi());
                                    Log.d(TAG, "onResponse: Cek_Divisi_Session2 :"+data.getString("divisi"));
                                    if(data.getString("status_menu").equals("open")){
                                        if(sessionManager.getKyDivisi().equals(data.getString("divisi"))){
                                            Intent intent = new Intent(mctx, classMenu);
                                            Bundle x = new Bundle();
                                            x.putString("lokasi_tujuan", "");
                                            x.putString("lokasi_awal", "");
                                            x.putString("hak_akses", hakAkses);
                                            intent.putExtras(x);
                                            mctx.startActivity(intent);
                                        }
                                        else if(data.getString("divisi").equals("all")){
                                            Intent intent = new Intent(mctx, classMenu);
                                            Bundle x = new Bundle();
                                            x.putString("lokasi_tujuan", "");
                                            x.putString("lokasi_awal", "");
                                            x.putString("hak_akses", hakAkses);
                                            intent.putExtras(x);
                                            mctx.startActivity(intent);
                                        } else {
                                            helper.snackBar(view,"anda tidak memiliki akses menu ini.....!!!!!!");
                                        }

                                    }else{
                                        helper.snackBar(view,"menu belum tersedia...!!!");
                                    }

                                }


                            }
                            Log.d("CEK_UPDATE", "onResponse: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            helper.showMsg(mctx, "informasi", "" + helper.PESAN_SERVER);
                            Log.d("JSONUPDATE", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_UPDATE", "onError: " + anError);
                        helper.showMsg(mctx, "informasi", "" + helper.PESAN_KONEKSI);

                    }
                });
    }




    public static String  getLokasi(double llat, double llon, Context mctx) {
        String alamat="";
        try {
            if(llat == 0){
                Log.d("CEK_LOKASI", "getLokasi: "+llat);
            }else {
                Log.d("LOKASI", "getLokasi: "+llat);
                Geocoder geocoder = new Geocoder(mctx, Locale.getDefault());
                String result = null;

                List<Address> addressList = geocoder.getFromLocation(llat, llon, 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)); //.append("\n");
                    }
                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    Log.d("getLOKASI", "getLokasi: "+address.getAddressLine(0));
                    alamat=address.getAddressLine(0);
                    result = sb.toString();
                }

            }
        } catch (IOException e) {
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        }catch (NullPointerException e){
            Log.e("Location Address Loader", "Unable connect to Geocoder", e);
        }

        return alamat;

    }




    public static String  split_date_time(String tipe_split,String date){
        //parameter tipe_split =date/time
        String result="";
        String tgl="";
        String waktu="";
        try {
            StringTokenizer tk = new StringTokenizer(date);
            tgl = tk.nextToken();  // <---  yyyy-mm-dd
            waktu = tk.nextToken();

            if(tipe_split.equals("date")){
                result=tgl;
            }
            if(tipe_split.equals("time")){
                result=waktu;
            }

        }catch (NullPointerException e){
            Log.d("NULL_POINTER", "split_date_time: "+e);
        }catch (NoSuchElementException e){
            Log.d("NULL_POINTER", "split_date_time: "+e);
        }
        return result;
    }

    public static double ParseCekDouble(String strNumber) {
        if (strNumber != null && strNumber.length() > 0) {
            try {
                return Double.parseDouble(strNumber);
            } catch(Exception e) {
                return -1;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        }
        else return 0;
    }


    public static  Boolean regexKata(String find, String word){
        Pattern pattern = Pattern.compile(find, Pattern.LITERAL);
        Matcher matcher = pattern.matcher(word);
        boolean matchFound = matcher.find();
        return matchFound;

    }

    public static void  messageToast(Context ctx,String pesan){
        Toast.makeText(ctx,""+pesan,Toast.LENGTH_LONG).show();
    }

    public static void getMsetProg(final Context ctx,String setProg){
        AndroidNetworking.post(api.URL_getmsetprog)
                .addBodyParameter("key", api.key)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SessionManager msession=new SessionManager(ctx);
                            Boolean success = response.getBoolean("success");
                            if (success) {
                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    String setano=data.getString("setano");
                                    if(setano.equals(setProg)){
                                       msession.createNoHpAdmin(data.getString("setchar"));
                                    }

                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSONERORABSEN", "onResponse: "+e);

                        }catch (NumberFormatException e){
                            Log.d("Number Format", "onResponse: "+e);
                        }


                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("EROOR_EXCEPTION", "onError: "+anError);



                    }
                });
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    public static Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private boolean isFileLessThan2MB(File file) {
        int maxFileSize = 2 * 1024 * 1024;
        Long l = file.length();
        String fileSize = l.toString();
        int finalFileSize = Integer.parseInt(fileSize);
        return  maxFileSize <= finalFileSize ;
    }

    public static String cheking_size(int size){
        String hrSize = "";
        double m = size/1024.0;
        DecimalFormat dec = new DecimalFormat("0.00");

        if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else {
            hrSize = dec.format(size).concat(" KB");
        }
        return hrSize;
    }

    public static String ConfigFCM(){
        // to subscribe a topic from FCM
       // FirebaseMessaging.getInstance().subscribeToTopic(topics);
        // to get token new FCM version
        String token="";
        try{
          //  token = FirebaseInstanceId.getInstance().getToken();
          //  Log.d("TAG_FCM_TOKEN", "onComplete: "+token);
          //  String id=FirebaseInstanceId.getInstance().getId();
            //  Log.d("TAG_FCM_TOKEN_ID", "onComplete: "+FirebaseMessaging.getInstance().subscribeToTopic(topics));

        } catch (Exception e){
            Log.d("TAG_FCM_TOKEN_FAILED", "onComplete: "+e);
        }
        return  token;
    }


    public static void getTokenHris( final Context mctx,String bariier) {
        AndroidNetworking.post(api.URL_API_TOKEN_HRIS)
                .addHeaders("Authorization", "Basic "+bariier)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("201")) {
                                String token=response.getString("token");
                                sessionManager = new SessionManager(mctx);
                                sessionManager.createToken(token);

                                Log.i("TAG_TOKEN", "onResponse: "+token);

                            } else {
                                Log.d(TAG, "token salah");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("JSON_TOKEN", "onResponse: " + e);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ERROR_TOKEN", "onError: " + anError);
                    }
                });
    }

    public static String getEncodeToken(String username, String password){
            String encode="";

        try {
             encode= Credentials.basic(username,password);
            //encode = Base64.encodeToString((username + ":" + password).getBytes("UTF-8"), Base64.DEFAULT);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return  encode;
    }

    public static void permissionCamera(Context ctx){
        if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) ctx,new String[]{
                    Manifest.permission.CAMERA
            },100);

        }
    }

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.d(TAG, "ParseException - dateFormat");
        }

        return outputDate;

    }

    public static String convertMonth(String month) {

        String bulan = "";

        switch (month) {
            case "January":
                return bulan = "Januari";
            case "February":
                return bulan = "Februari";
            case "March":
                return bulan = "Maret";
            case "May":
                return bulan = "Mei";
            case "Juny":
                return bulan = "Juni";
            case "July":
                return bulan = "July";
            case "August":
                return bulan = "Agustus";
            case "October":
                return bulan = "Oktober";
            case "December":
                return bulan = "Desember";
        }

        return bulan;
    }

}
