package id.ac.umn.magang01;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    public static final String API_Pembelian_Dagang = API + "pembelian_per_supplier";
    public static final String API_Penjualan_Dagang = API + "penjualan_per_customer";

    //shared preference
    public static final String SP_TOKEN = "user_token";
    public static final String SP_USER = "user_username";
    public static final String SP_EMAIL = "user_email";
    public static final String SP_NO_HP = "user_email";


    //Date settings
    public static String DISPLAY_FROM_DATE = "";
    public static String DISPLAY_TO_DATE = "";

    //Parameter

        //Default date
        public static String thisYear = new SimpleDateFormat("yyyy").format(new Date());
        public static String thisMonth = new SimpleDateFormat("MM").format(new Date());

    public static String FROM_DATE = thisYear + "01";
    public static String TO_DATE = thisYear + thisMonth;
    public static int PER_BULAN;
}