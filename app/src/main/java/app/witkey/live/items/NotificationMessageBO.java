package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationMessageBO implements Parcelable {

    @SerializedName("name")
    @Expose
    String name;
    @SerializedName("notification_text")
    @Expose
    String notification_text;
    @SerializedName("id")
    @Expose
    int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotification_text() {
        return notification_text;
    }

    public void setNotification_text(String notification_text) {
        this.notification_text = notification_text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.notification_text);
        dest.writeInt(this.id);
    }

    public NotificationMessageBO() {
    }

    protected NotificationMessageBO(Parcel in) {
        this.name = in.readString();
        this.notification_text = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<NotificationMessageBO> CREATOR = new Parcelable.Creator<NotificationMessageBO>() {
        @Override
        public NotificationMessageBO createFromParcel(Parcel source) {
            return new NotificationMessageBO(source);
        }

        @Override
        public NotificationMessageBO[] newArray(int size) {
            return new NotificationMessageBO[size];
        }
    };
}
