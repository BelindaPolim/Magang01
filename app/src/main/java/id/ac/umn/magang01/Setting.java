package id.ac.umn.magang01;

public class Setting {

    // YSL
    public static final String IP = "http://119.235.208.235:8092/";

    public static final String URL_Privacy_Policy=IP+"privacy_policy";

    public static final String API = IP+"api/";

    public static final String API_Login = API + "auth/login";
    public static final String API_Email = API + "email";

    public static final String API_Profile_Forgot = API + "profile-forgot";
    public static final String API_Piutang_Dagang = API + "piutang_dagang_per_customer";
    public static final String API_Hutang_Dagang = API + "hutang_dagang_per_supplier";
    public static final String API_Pembelian_Dagang = API + "pembelian_per_supplier?FromTahunBulan=202006&ToTahunBulan=202008&PerBulan=1";
    public static final String API_Penjualan_Dagang = API + "penjualan_per_customer?FromTahunBulan=202006&ToTahunBulan=202008&PerBulan=1";

    //shared preference
    public static final String SP_TOKEN = "user_token";
    public static final String SP_USER = "user_username";
    public static final String SP_EMAIL = "user_email";
    public static final String SP_NO_HP = "user_email";

}