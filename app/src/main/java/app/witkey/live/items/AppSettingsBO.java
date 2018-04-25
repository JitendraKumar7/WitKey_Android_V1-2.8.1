package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppSettingsBO implements Parcelable {

    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("is_live")
    @Expose
    String is_live;
    @SerializedName("go_live_date")
    @Expose
    String go_live_date;
    @SerializedName("allow_signup")
    @Expose
    String allow_signup;
    @SerializedName("allow_stream")
    @Expose
    String allow_stream;
    @SerializedName("allow_share")
    @Expose
    String allow_share;
    @SerializedName("created_at")
    @Expose
    String created_at;
    @SerializedName("updated_at")
    @Expose
    String updated_at;
    @SerializedName("payment_api_key")
    @Expose
    String payment_api_key;
    @SerializedName("payment_email")
    @Expose
    String payment_email;
    @SerializedName("wowza_android_app_license_key")
    @Expose
    String wowza_android_app_license_key;
    @SerializedName("pubnub_publish_key")
    @Expose
    String pubnub_publish_key;

    @SerializedName("promo_code")
    @Expose
    String promo_code;
    @SerializedName("pubnub_subs_key")
    @Expose
    String pubnub_subs_key;
    @SerializedName("app_allow")
    @Expose
    String app_allow;
    @SerializedName("chat_signature")
    @Expose
    String chat_signature;


    @SerializedName("gift_version")
    @Expose
    String gift_version;
    @SerializedName("payment_currency")
    @Expose
    String payment_currency;
    @SerializedName("payment_language")
    @Expose
    String payment_language;
    @SerializedName("payment_url")
    @Expose
    String payment_url;
    @SerializedName("payment_ureturn")
    @Expose
    String payment_ureturn;
    @SerializedName("payment_ucancel")
    @Expose
    String payment_ucancel;
    @SerializedName("payment_post_back_url")
    @Expose
    String payment_post_back_url;
    @SerializedName("payment_unotify")
    @Expose
    String payment_unotify;
    @SerializedName("strea_group_chat")
    @Expose
    String strea_group_chat;
    @SerializedName("tok_api_key")
    @Expose
    String tok_api_key;
    @SerializedName("android_version")
    @Expose
    String android_version;
    @SerializedName("is_force_update_android")//("is_force_update")
    @Expose
    Integer is_force_update = 0;

    @SerializedName("allow_initial_promotion")
    @Expose
    Integer allow_initial_promotion;

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public Integer getIs_force_update() {
        return is_force_update;
    }

    public void setIs_force_update(Integer is_force_update) {
        this.is_force_update = is_force_update;
    }

    public String getTok_api_key() {
        return tok_api_key;
    }

    public void setTok_api_key(String tok_api_key) {
        this.tok_api_key = tok_api_key;
    }

    public String getGift_version() {
        return gift_version;
    }

    public void setGift_version(String gift_version) {
        this.gift_version = gift_version;
    }

    public String getPayment_currency() {
        return payment_currency;
    }

    public void setPayment_currency(String payment_currency) {
        this.payment_currency = payment_currency;
    }

    public String getPayment_language() {
        return payment_language;
    }

    public void setPayment_language(String payment_language) {
        this.payment_language = payment_language;
    }

    public String getPayment_url() {
        return payment_url;
    }

    public void setPayment_url(String payment_url) {
        this.payment_url = payment_url;
    }

    public String getPayment_ureturn() {
        return payment_ureturn;
    }

    public void setPayment_ureturn(String payment_ureturn) {
        this.payment_ureturn = payment_ureturn;
    }

    public String getPayment_ucancel() {
        return payment_ucancel;
    }

    public void setPayment_ucancel(String payment_ucancel) {
        this.payment_ucancel = payment_ucancel;
    }

    public String getPayment_post_back_url() {
        return payment_post_back_url;
    }

    public void setPayment_post_back_url(String payment_post_back_url) {
        this.payment_post_back_url = payment_post_back_url;
    }

    public String getPayment_unotify() {
        return payment_unotify;
    }

    public void setPayment_unotify(String payment_unotify) {
        this.payment_unotify = payment_unotify;
    }

    public String getStrea_group_chat() {
        return strea_group_chat;
    }

    public void setStrea_group_chat(String strea_group_chat) {
        this.strea_group_chat = strea_group_chat;
    }

    public String getChat_signature() {
        return chat_signature;
    }

    public void setChat_signature(String chat_signature) {
        this.chat_signature = chat_signature;
    }


    public String getMiliseconds() {
        return miliseconds;
    }

    public void setMiliseconds(String miliseconds) {
        this.miliseconds = miliseconds;
    }

    @SerializedName("miliseconds")
    @Expose
    String miliseconds;
    //String wowza_ios_app_license_key;

    public String getPromo_code() {
        if (!TextUtils.isEmpty(promo_code))
            return promo_code;
        else
            return "";
    }

    public void setPromo_code(String promo_code) {
        this.promo_code = promo_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIs_live() {
        return is_live;
    }

    public void setIs_live(String is_live) {
        this.is_live = is_live;
    }

    public String getGo_live_date() {
        return go_live_date;
    }

    public void setGo_live_date(String go_live_date) {
        this.go_live_date = go_live_date;
    }

    public String getAllow_signup() {
        return allow_signup;
    }

    public void setAllow_signup(String allow_signup) {
        this.allow_signup = allow_signup;
    }

    public String getAllow_stream() {
        return allow_stream;
    }

    public void setAllow_stream(String allow_stream) {
        this.allow_stream = allow_stream;
    }

    public String getAllow_share() {
        return allow_share;
    }

    public void setAllow_share(String allow_share) {
        this.allow_share = allow_share;
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

    public String getPayment_api_key() {
        return payment_api_key;
    }

    public void setPayment_api_key(String payment_api_key) {
        this.payment_api_key = payment_api_key;
    }

    public String getPayment_email() {
        return payment_email;
    }

    public void setPayment_email(String payment_email) {
        this.payment_email = payment_email;
    }

    public String getWowza_android_app_license_key() {
        return wowza_android_app_license_key;
    }

    public void setWowza_android_app_license_key(String wowza_android_app_license_key) {
        this.wowza_android_app_license_key = wowza_android_app_license_key;
    }

    public String getPubnub_publish_key() {
        return pubnub_publish_key;
    }

    public void setPubnub_publish_key(String pubnub_publish_key) {
        this.pubnub_publish_key = pubnub_publish_key;
    }

    public String getPubnub_subs_key() {
        return pubnub_subs_key;
    }

    public void setPubnub_subs_key(String pubnub_subs_key) {
        this.pubnub_subs_key = pubnub_subs_key;
    }

    public String getApp_allow() {
        return app_allow;
    }

    public Integer getAllow_initial_promotion() {
        return allow_initial_promotion;
    }

    public void setAllow_initial_promotion(Integer allow_initial_promotion) {
        this.allow_initial_promotion = allow_initial_promotion;
    }

    public void setApp_allow(String app_allow) {
        this.app_allow = app_allow;
    }

    public AppSettingsBO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.is_live);
        dest.writeString(this.go_live_date);
        dest.writeString(this.allow_signup);
        dest.writeString(this.allow_stream);
        dest.writeString(this.allow_share);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.payment_api_key);
        dest.writeString(this.payment_email);
        dest.writeString(this.wowza_android_app_license_key);
        dest.writeString(this.pubnub_publish_key);
        dest.writeString(this.promo_code);
        dest.writeString(this.pubnub_subs_key);
        dest.writeString(this.app_allow);
        dest.writeString(this.chat_signature);
        dest.writeString(this.gift_version);
        dest.writeString(this.payment_currency);
        dest.writeString(this.payment_language);
        dest.writeString(this.payment_url);
        dest.writeString(this.payment_ureturn);
        dest.writeString(this.payment_ucancel);
        dest.writeString(this.payment_post_back_url);
        dest.writeString(this.payment_unotify);
        dest.writeString(this.strea_group_chat);
        dest.writeString(this.tok_api_key);
        dest.writeString(this.android_version);
        dest.writeValue(this.is_force_update);
        dest.writeValue(this.allow_initial_promotion);
        dest.writeString(this.miliseconds);
    }

    protected AppSettingsBO(Parcel in) {
        this.id = in.readString();
        this.is_live = in.readString();
        this.go_live_date = in.readString();
        this.allow_signup = in.readString();
        this.allow_stream = in.readString();
        this.allow_share = in.readString();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.payment_api_key = in.readString();
        this.payment_email = in.readString();
        this.wowza_android_app_license_key = in.readString();
        this.pubnub_publish_key = in.readString();
        this.promo_code = in.readString();
        this.pubnub_subs_key = in.readString();
        this.app_allow = in.readString();
        this.chat_signature = in.readString();
        this.gift_version = in.readString();
        this.payment_currency = in.readString();
        this.payment_language = in.readString();
        this.payment_url = in.readString();
        this.payment_ureturn = in.readString();
        this.payment_ucancel = in.readString();
        this.payment_post_back_url = in.readString();
        this.payment_unotify = in.readString();
        this.strea_group_chat = in.readString();
        this.tok_api_key = in.readString();
        this.android_version = in.readString();
        this.is_force_update = (Integer) in.readValue(Integer.class.getClassLoader());
        this.allow_initial_promotion = (Integer) in.readValue(Integer.class.getClassLoader());
        this.miliseconds = in.readString();
    }

    public static final Creator<AppSettingsBO> CREATOR = new Creator<AppSettingsBO>() {
        @Override
        public AppSettingsBO createFromParcel(Parcel source) {
            return new AppSettingsBO(source);
        }

        @Override
        public AppSettingsBO[] newArray(int size) {
            return new AppSettingsBO[size];
        }
    };
}
