package id.ac.umn.magang01;

import com.google.gson.annotations.SerializedName;

public class InfoModel {
    @SerializedName("name")
    private String name;
    @SerializedName("info")
    private String info;

    public InfoModel(String name, String info){
        this.name = name;
        this.info = info;
    }

    public String getName(){
        return name;
    }

    public String getInfo(){
        return info;
    }
}
