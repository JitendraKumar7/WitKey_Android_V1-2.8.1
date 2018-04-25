package app.witkey.live;

import java.io.File;

import app.witkey.live.utils.EnumUtils;

public class Constants {

    /*DEVELOPMENT CONFIG*/
    //APP URL FOR CLIENT WITH (EnumUtils.AppDomain.LIVE), APP URL FOR DEVELOPMENT WITH (EnumUtils.AppDomain.DEV)
    public static final EnumUtils.AppDomain appDomain = EnumUtils.AppDomain.LIVE;
    // FOR BUILD TYPE (TRUE FOR QA, FALSE FOR CLIENT)
    public static boolean BUILD_TYPE_QA = false;
    // FOR BUILD TYPE (TRUE FOR NO SPLASH, FALSE TO SHOW) NOTE: SPLASH SCREEN MUST PLAY ONCE TO SAVE APP-SETTINGS
    public static boolean HIDE_SPLASH = false;
    // FOR BUILD TYPE (TRUE FOR ENABLE STREAM VOICE AND FALSE FOR DISABLE STREAM VOICE)
    public static boolean ENABLE_STREAM_VOICE = true;
    // ENABLE LIVE STREAM : TRUE, STREAM RECORDING : FALSE
    public static boolean ENABLE_LIVE_STREAMING = true;
    public static boolean APP_IS_LIVE = false;

    public static int GO_LIVE_WITH_TOK = 100, GO_LIVE_WITH_KSY = 200, GO_LIVE_WITH_WOWZA = 300;
    public static int CURRENT_GO_LIVE_WITH = GO_LIVE_WITH_KSY;
    // tok URL RTC

    /*DEVELOPMENT CONFIG*/

    //Constants for image frames animation
    public static final String SPLASH_FILE_DIR = "splashanimationframs";//"splashframes";
    public static final String SPLASH_FILE_IDENTIFIER = "Widkey_";//"Witkey-";
    public static final int SPLASH_FRAME_SIZE = 175;//150;
    public static final int SPLASH_FRAME_DELAY = 0;

    public static final String DEFAULT_FONT_NAME = "Roboto-Regular.ttf";
    public static final String DEFAULT_FONT_NAME_FOR_ET = "Roboto-Regular.ttf"; // font majorly used in edittext
    public static final String dateFormat0 = "MM/dd/yyyy";
    public static final String dateFormat1 = "EEE, MMM dd";
    public static final String dateFormat2 = "EEE, MMM dd - hh:mm a";
    public static final String dateFormat3 = "MMM dd, yyyy";
    public static final String dateFormat4 = "hh:mm a";
    public static final String dateFormat5 = "M-d-yyyy";
    public static final String dateFormat6 = "dd/MM/yyyy";
    public static final String dateFormat7 = "MM/dd/yyyy hh:mm:ss a";
    public static final String dateFormat8 = "yyyyMMdd-HHmmssSSS";
    public static final String dateFormat9 = "h:mm a";
    public static final String dateFormat10 = "HH:mm:ss";
    public static final String dateFormat11 = "ddMMyyyyHHmmss";
    public static final String dateFormat12 = "yyyy-MM-dd HH:mm:ss";
    public static final String dateFormat13 = "MM/dd/yyyy hh:mm a";
    public static final String dateFormat14 = "yyyy";
    public static final String dateFormat15 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String dateFormat16 = "yyyy-MM-dd";
    public static final String dateFormat17 = "EE, MMM dd";
    public static final String dateFormat18 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String dateFormat19 = "EEEE, MMMM dd, yyyy";
    public static final String dateFormat20 = "MM/dd";
    public static final String dateFormat21 = "MMMM dd, yyyy";
    public static final String dateFormat22 = "MM/yy";
    public static final String dateFormat23 = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String dateFormat24 = "MMMM dd";
    public static final String dateFormat25 = "yyyy:MM:dd HH:mm:ss";
    public static final String ServerTimeZone = "EST";
    public static final String dateFormat26 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String dateFormat27 = "dd MMM yyyy";
    public static final String dateFormat28 = "dd MMM";
    public static final String dateFormat30 = "mm:ss";
    public static final String dateFormat29 = "hh:mm:ss a";

    //server Error codes for GenerateToken service...
    //server Error codes for GenerateToken service...
    public static final String VALID_RESPONSE_CODE = "200";
    public static final String CREATED_RESPONSE_CODE = "201";
    public static final String UPDATED_RESPONSE_CODE = "202";
    public static final String EC_INVALID_TOKEN = "500";
    public static final String EC_TOKEN_EXPIRED = "502";

    // Encryption related constants
    public static final String Encrypt = ":Encrypt";

    //key and vector for debug build..
    public static final String EncryptionKeyDebug = "";
    public static final String EncryptionVectorDebug = "";

    //key and vector for live build..
    public static final String EncryptionKeyLive = "";
    public static final String EncryptionVectorLive = "";

    // Time related constants
    public static final int WEBSERVICE_WAITTIME = 120000;
    public static final int WEBSERVICE_SOCKETTIME = 120000;

    public static String CURRENCY_SYMBOL = "US. ";
    public static String NULL = "N/A";

    public static int MAX_ITEMS_TO_LOAD = 10;

    //twitter login keys for debug...
    public final static String CONSUMER_KEY = "ToRN8xEppdP2ZeX6T5PfTZIYr";
    public final static String CONSUMER_SECRET = "IittLm4Qho1RDENb779UX08jv8wKdUpKiRxq6ICVHs14CJuPGg";

    public static String PUBNUB_PUBLISH_KEY = "pub-c-3aab1218-12f1-4ce2-bba8-26502ec5ce9a";
    public static String PUBNUB_SUBS_KEY = "sub-c-0980efc0-b0e0-11e7-85f8-821de3cbacaa";

    public static String APP_NAME = "WitKey";
    public static String FILES = "Files";
    public static String GIFTS = ".Gifs";
    public static String WITKEY_PICTURE = "Pictures";
    public static String WITKEY_SCREEN_SHOT = "Screen Shot";
    public static String WITKEY_FOLDER_PATH = android.os.Environment.getExternalStorageDirectory() + File.separator + Constants.APP_NAME + File.separator + Constants.GIFTS;
    public static String WITKEY_GIFTS_FOLDER_PATH = APP_NAME + File.separator + GIFTS;
//    public static String WITKEY_GIFTS_FOLDER_PATH = FILES + "/" + GIFTS;

    public static String DUMMY_IMAGE_URL = "https://www.w3schools.com/w3css/img_avatar3.png";
    public static String NONE = "NONE";

    public static final int SHOW_BOTTOM_TAB_SCROLL_OFFSET = 20; // todo use this in dp instead of px

    public static final String IS_LIVE_TRUE = "1";
    public static final String IS_ALLOW_APP_TRUE = "1";
    public static final String IS_ALLOW_SIGNUP_TRUE = "1";
    public static final String PRIVATE_CHAT_STRING = "-WitkeyChat-";
    public static final String GIFTS_GIFS_URL = "http://witkeyapp.com/witkey_dev/public/gifts/all_gifts.zip";

    //TOK
    public static String API_KEY = "46031402";
    public static String SESSION_ID = "1_MX40NjAzMTQwMn5-MTUxNTAwNjQ0MzM1Nn5vRFVIbFpJdmE3a041MDBEMkJyVXJqR1N-fg";
    public static String TOKEN = "T1==cGFydG5lcl9pZD00NjAzMTQwMiZzaWc9Mjc1MDBhNTM1ODQ4MWUyMmQ3MGFmZDZlZWNmYTRlNjgzMjM5ZDg4MTpzZXNzaW9uX2lkPTFfTVg0ME5qQXpNVFF3TW41LU1UVXhOVEF3TmpRME16TTFObjV2UkZWSWJGcEpkbUUzYTA0MU1EQkVNa0p5VlhKcVIxTi1mZyZjcmVhdGVfdGltZT0xNTE1MDQ4NTY5Jm5vbmNlPTAuMTU1OTg0MzEzNTc5NDAyOCZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNTE1MTM0OTY5JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
}
