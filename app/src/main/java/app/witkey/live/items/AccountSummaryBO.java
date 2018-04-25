package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import app.witkey.live.Constants;
import app.witkey.live.utils.DateTimeOp;

/**
 * created by developer on 01/15/2018.
 */

public class AccountSummaryBO implements Parcelable {

    @SerializedName("coins_count")
    @Expose
    public Double coins_count = 0.0;
    @SerializedName("witkey_dollar_count")
    @Expose
    public Double witkey_dollar_count = 0.0;
    @SerializedName("user_id")
    @Expose
    public Integer user_id = 0;
    @SerializedName("user_details")
    @Expose
    public UserDetailBO user_details;

    public Double getCoins_count() {
        return coins_count;
    }

    public void setCoins_count(Double coins_count) {
        this.coins_count = coins_count;
    }

    public Double getWitkey_dollar_count() {
        return witkey_dollar_count;
    }

    public void setWitkey_dollar_count(Double witkey_dollar_count) {
        this.witkey_dollar_count = witkey_dollar_count;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public UserDetailBO getUser_details() {
        return user_details;
    }

    public void setUser_details(UserDetailBO user_details) {
        this.user_details = user_details;
    }

    public AccountSummaryBO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.coins_count);
        dest.writeValue(this.witkey_dollar_count);
        dest.writeValue(this.user_id);
        dest.writeParcelable(this.user_details, flags);
    }

    protected AccountSummaryBO(Parcel in) {
        this.coins_count = (Double) in.readValue(Double.class.getClassLoader());
        this.witkey_dollar_count = (Double) in.readValue(Double.class.getClassLoader());
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_details = in.readParcelable(UserDetailBO.class.getClassLoader());
    }

    public static final Creator<AccountSummaryBO> CREATOR = new Creator<AccountSummaryBO>() {
        @Override
        public AccountSummaryBO createFromParcel(Parcel source) {
            return new AccountSummaryBO(source);
        }

        @Override
        public AccountSummaryBO[] newArray(int size) {
            return new AccountSummaryBO[size];
        }
    };
}
