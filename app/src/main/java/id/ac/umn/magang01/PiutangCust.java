package id.ac.umn.magang01;

public class PiutangCust {
    private String cID;
    private String cName;
    private String cSisa;

    public PiutangCust(String id, String name, String sisaPiutang){
        cID = id;
        cName = name;
        cSisa = sisaPiutang;
    }

    public String getcID(){
        return cID;
    }
    public String getcName(){
        return cName;
    }
    public String getcSisa(){
        return cSisa;
    }
}
