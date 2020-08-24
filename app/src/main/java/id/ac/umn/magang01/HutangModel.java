package id.ac.umn.magang01;

import com.google.gson.annotations.SerializedName;

public class HutangModel {
    @SerializedName("id")
    private String mID;
    @SerializedName("name")
    private String mName;
    @SerializedName("sisaHutang")
    private String mHutang;

    public HutangModel(String id, String name, String sisaHutang){
        mID = id;
        mName = name;
        mHutang = sisaHutang;
    }

    public String getID(){
        return mID;
    }
    public String getName(){
        return mName;
    }
    public String getSisa(){
        return mHutang;
    }
}
