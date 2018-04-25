package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StreamEndSummaryBO implements Parcelable {

    @SerializedName("total_viewrs")
    @Expose
    public Integer total_viewrs = 0;
    @SerializedName("total_viewers_last_24")
    @Expose
    public Integer total_viewers_last_24 = 0;
    @SerializedName("witkey_dollar")
    @Expose
    public Integer witkey_dollar = 0;
    @SerializedName("witkey_dollar_last_24")
    @Expose
    public Integer witkey_dollar_last_24 = 0;
    @SerializedName("new_fan")
    @Expose
    public Integer new_fan = 0;
    @SerializedName("total_fan_last_24")
    @Expose
    public Integer total_fan_last_24 = 0;
    @SerializedName("popularity")
    @Expose
    public Integer popularity = 0;
    @SerializedName("popularity_last_24")
    @Expose
    public Integer popularity_last_24 = 0;
    public String endStreamSummary;

    public String getEndStreamSummary() {
        return endStreamSummary;
    }

    public void setEndStreamSummary(String endStreamSummary) {
        this.endStreamSummary = endStreamSummary;
    }

    public Integer getTotal_viewrs() {
        return total_viewrs;
    }

    public void setTotal_viewrs(Integer total_viewrs) {
        this.total_viewrs = total_viewrs;
    }

    public Integer getTotal_viewers_last_24() {
        return total_viewers_last_24;
    }

    public void setTotal_viewers_last_24(Integer total_viewers_last_24) {
        this.total_viewers_last_24 = total_viewers_last_24;
    }

    public Integer getWitkey_dollar() {
        return witkey_dollar;
    }

    public void setWitkey_dollar(Integer witkey_dollar) {
        this.witkey_dollar = witkey_dollar;
    }

    public Integer getWitkey_dollar_last_24() {
        return witkey_dollar_last_24;
    }

    public void setWitkey_dollar_last_24(Integer witkey_dollar_last_24) {
        this.witkey_dollar_last_24 = witkey_dollar_last_24;
    }

    public Integer getNew_fan() {
        return new_fan;
    }

    public void setNew_fan(Integer new_fan) {
        this.new_fan = new_fan;
    }

    public Integer getTotal_fan_last_24() {
        return total_fan_last_24;
    }

    public void setTotal_fan_last_24(Integer total_fan_last_24) {
        this.total_fan_last_24 = total_fan_last_24;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public Integer getPopularity_last_24() {
        return popularity_last_24;
    }

    public void setPopularity_last_24(Integer popularity_last_24) {
        this.popularity_last_24 = popularity_last_24;
    }

    public StreamEndSummaryBO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.total_viewrs);
        dest.writeValue(this.total_viewers_last_24);
        dest.writeValue(this.witkey_dollar);
        dest.writeValue(this.witkey_dollar_last_24);
        dest.writeValue(this.new_fan);
        dest.writeValue(this.total_fan_last_24);
        dest.writeValue(this.popularity);
        dest.writeValue(this.popularity_last_24);
        dest.writeString(this.endStreamSummary);
    }

    protected StreamEndSummaryBO(Parcel in) {
        this.total_viewrs = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total_viewers_last_24 = (Integer) in.readValue(Integer.class.getClassLoader());
        this.witkey_dollar = (Integer) in.readValue(Integer.class.getClassLoader());
        this.witkey_dollar_last_24 = (Integer) in.readValue(Integer.class.getClassLoader());
        this.new_fan = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total_fan_last_24 = (Integer) in.readValue(Integer.class.getClassLoader());
        this.popularity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.popularity_last_24 = (Integer) in.readValue(Integer.class.getClassLoader());
        this.endStreamSummary = in.readString();
    }

    public static final Creator<StreamEndSummaryBO> CREATOR = new Creator<StreamEndSummaryBO>() {
        @Override
        public StreamEndSummaryBO createFromParcel(Parcel source) {
            return new StreamEndSummaryBO(source);
        }

        @Override
        public StreamEndSummaryBO[] newArray(int size) {
            return new StreamEndSummaryBO[size];
        }
    };
}
