package com.app.mobiledev.apphris.izin.izinSakit;

import android.util.Log;

public class modelIzinSakit {
    private String id;
    private String name;
    private String kyano;
    private String indikasi_sakit;
    private String mulai_sakit_tanggal;
    private String selesai_sakit_tanggal;
    private String catatan;
    private String created_at;
    private String updated_at;
    private String approve_head;
    private String approve_hrd;
    private String lampiran_file;

    private String head_kyano;
    private String hrd_kyano;
    private String head_approve_date;
    private String hrd_approve_date;
    private String head_name;


    public String getName() {
        Log.d("model_getName", "getName: "+name);
        return name;
    }

    public void setName(String name) {
        Log.d("model_setName", "getName: "+name);
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKyano() {
        return kyano;
    }

    public void setKyano(String kyano) {
        this.kyano = kyano;
    }

    public String getIndikasi_sakit() {
        return indikasi_sakit;
    }

    public void setIndikasi_sakit(String indikasi_sakit) {
        this.indikasi_sakit = indikasi_sakit;
    }

    public String getMulai_sakit_tanggal() {
        return mulai_sakit_tanggal;
    }

    public void setMulai_sakit_tanggal(String mulai_sakit_tanggal) {
        this.mulai_sakit_tanggal = mulai_sakit_tanggal;
    }

    public String getSelesai_sakit_tanggal() {
        return selesai_sakit_tanggal;
    }

    public void setSelesai_sakit_tanggal(String selesai_sakit_tanggal) {
        this.selesai_sakit_tanggal = selesai_sakit_tanggal;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getApprove_head() {
        return approve_head;
    }

    public void setApprove_head(String approve_head) {
        this.approve_head = approve_head;
    }

    public String getApprove_hrd() {
        return approve_hrd;
    }

    public void setApprove_hrd(String approve_hrd) {
        this.approve_hrd = approve_hrd;
    }

    public String getLampiran_file() {
        return lampiran_file;
    }

    public void setLampiran_file(String lampiran_file) {
        this.lampiran_file = lampiran_file;
    }

    public String getHead_kyano() {
        return head_kyano;
    }

    public void setHead_kyano(String head_kyano) {
        this.head_kyano = head_kyano;
    }

    public String getHrd_kyano() {
        return hrd_kyano;
    }

    public void setHrd_kyano(String hrd_kyano) {
        this.hrd_kyano = hrd_kyano;
    }

    public String getHead_approve_date() {
        return head_approve_date;
    }

    public void setHead_approve_date(String head_approve_date) {
        this.head_approve_date = head_approve_date;
    }

    public String getHrd_approve_date() {
        return hrd_approve_date;
    }

    public void setHrd_approve_date(String hrd_approve_date) {
        this.hrd_approve_date = hrd_approve_date;
    }

    public String getHead_name() {
        return head_name;
    }

    public void setHead_name(String head_name) {
        this.head_name = head_name;
    }
}
