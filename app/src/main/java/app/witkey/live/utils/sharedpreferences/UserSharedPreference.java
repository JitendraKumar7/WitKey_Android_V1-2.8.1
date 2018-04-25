package app.witkey.live.utils.sharedpreferences;

import com.google.firebase.iid.FirebaseInstanceId;

import app.witkey.live.utils.Keys;

/**
 * created by developer on 10/03/2017.
 */

public class UserSharedPreference {


    // METHODS FOR USER LOGIN STATUS
    public static boolean readIsUserLoggedIn() {
        return (SharedPref.read(Keys.USER_LOGIN_STATUS, false));
    }

    public static void saveIsUserLoggedIn(boolean value) {
        SharedPref.save(Keys.USER_LOGIN_STATUS, value);
    }

    // METHODS FOR USER TOKEN
    public static String readUserToken() {
        return (SharedPref.read(Keys.USER_TOKEN, ""));
    }

    public static void saveUserToken(String value) {
        SharedPref.save(Keys.USER_TOKEN, value);
    }

    // METHODS FOR USER FIRST VISIT
    public static boolean readUserFirstVisit() {
        return (SharedPref.read(Keys.FIRST_VISIT, false));
    }

    public static void saveUserFirstVisit(boolean value) {
        SharedPref.save(Keys.FIRST_VISIT, value);
    }

    // METHODS FOR USER FIRST VISIT
    public static boolean readFirstVisitToWellComeNote() {
        return (SharedPref.read(Keys.FIRST_VISIT, false));
    }

    public static void saveFirstVisitToWellComeNote(boolean value) {
        SharedPref.save(Keys.FIRST_VISIT, value);
    }

    // METHODS FOR USER FIRST VISIT
    public static String readUserWowzaKey() {
        return (SharedPref.read(Keys.WOWZA_KEY, ""));
    }

    public static void saveUserWowzaKey(String value) {
        SharedPref.save(Keys.WOWZA_KEY, value);
    }

    // METHODS FOR USER FIRST VISIT
    public static String readUserPubNubKey() {
        return (SharedPref.read(Keys.PUBNUB_PUBLISH_KEY, ""));
    }

    public static void saveUserPubNubKey(String value) {
        SharedPref.save(Keys.PUBNUB_PUBLISH_KEY, value);
    }

    // METHODS FOR USER FIRST VISIT
    public static String readUserPubNubSubKey() {
        return (SharedPref.read(Keys.PUBNUB_SUB_KEY, ""));
    }

    public static void saveUserPubNubSubKey(String value) {
        SharedPref.save(Keys.PUBNUB_SUB_KEY, value);
    }

    // METHODS FOR USER FIRST VISIT
    public static String readUserPaymentKey() {
        return (SharedPref.read(Keys.PAYMENT_KEY, ""));
    }

    public static void saveUserPaymentKey(String value) {
        SharedPref.save(Keys.PAYMENT_KEY, value);
    }

    // METHODS FOR USER FIRST VISIT
    public static String readUserPrivateChatKey() {
        return (SharedPref.read(Keys.USER_PRIVATE_CHAT_KEY, ""));
    }

    public static void saveUserPrivateChatKey(String value) {
        SharedPref.save(Keys.USER_PRIVATE_CHAT_KEY, value);
    }

    // METHODS FOR USER GROUP CHAT KEY
    public static String readUserStreamChatKey() {
        return (SharedPref.read(Keys.USER_PRIVATE_CHAT_KEY, ""));
    }

    public static void saveUserStreamChatKey(String value) {
        SharedPref.save(Keys.USER_PRIVATE_CHAT_KEY, value);
    }

    // METHODS FOR USER GIFT VERSION
    public static String readUserCurrentGiftVersion() {
        return (SharedPref.read(Keys.USER_GIFTS_CURRENT_VERSION, "0"));
    }

    public static void saveUserCurrentGiftVersion(String value) {
        SharedPref.save(Keys.USER_GIFTS_CURRENT_VERSION, value);
    }

    // METHODS FOR USER GIFT LIST VERSION
    public static String readUserCurrentZipGiftList() {
        return (SharedPref.read(Keys.USER_GIFTS_CURRENT_LIST, ""));
    }

    public static void saveUserCurrentZipGiftList(String value) {
        SharedPref.save(Keys.USER_GIFTS_CURRENT_LIST, value);
    }

    // METHODS FOR USER GCM TOKEN
    public static String readRegToken() {
        String token = SharedPref.read(Keys.REGISTER_TOKEN, null);
        if (token == null) return FirebaseInstanceId.getInstance().getToken();
        else return token;
    }

    public static void saveRegToken(String value) {
        SharedPref.save(Keys.REGISTER_TOKEN, value);
    }
}
