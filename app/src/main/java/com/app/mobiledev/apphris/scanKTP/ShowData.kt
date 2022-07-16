package com.app.mobiledev.apphris.scanKTP;

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.mobiledev.apphris.R
import com.app.mobiledev.apphris.scanKTP.productsearch.Product

import org.json.JSONObject

class ShowData : AppCompatActivity() {

    private val tag = "ShowData"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_data)

        val getData = intent.getParcelableExtra<Product>("IDData");
        val nama = getData?.title;
        val data = getData?.subtitle;

        val jsonconversion = JSONObject(data)

        Log.d(tag, "NIK is " + jsonconversion.getString("documentNumber"))
        Log.d(tag, "nama is " + nama )
        Log.d(tag, "Tempat tanggal lahir is " + jsonconversion.getString("placeOfBirth") + " " +  jsonconversion.getString("dateOfBirth"))
        Log.d(tag, "Alamat?");
    }
}