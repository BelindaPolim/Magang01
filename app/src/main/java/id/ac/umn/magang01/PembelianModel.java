package id.ac.umn.magang01;

import com.google.gson.annotations.SerializedName;

public class PembelianModel {
    @SerializedName("id")
    private String mID;
    @SerializedName("name")
    private String mName;
    @SerializedName("nilaiPembelian")
    private String mNilai;

    public PembelianModel(String id, String name, String nilaiPenjualan){
        mID = id;
        mName = name;
        mNilai = nilaiPenjualan;
    }

    public String getID(){
        return mID;
    }
    public String getName(){
        return mName;
    }
    public String getNilai(){
        return mNilai;
    }
}
