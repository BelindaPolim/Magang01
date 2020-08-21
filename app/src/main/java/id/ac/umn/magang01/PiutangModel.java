package id.ac.umn.magang01;

import com.google.gson.annotations.SerializedName;

public class PiutangModel {
    @SerializedName("id")
    private String mID;
    @SerializedName("name")
    private String mName;
    @SerializedName("sisaPiutang")
    private String mPiutang;

    public PiutangModel(String id, String name, String sisaPiutang){
        mID = id;
        mName = name;
        mPiutang = sisaPiutang;
    }

    public String getID(){
        return mID;
    }
    public String getName(){
        return mName;
    }
    public String getSisa(){
        return mPiutang;
    }
}
