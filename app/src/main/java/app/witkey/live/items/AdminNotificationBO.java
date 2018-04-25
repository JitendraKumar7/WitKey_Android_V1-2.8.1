package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;

public class AdminNotificationBO implements Parcelable {

    Integer id;
    String notification_text;
    String created_at;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNotification_text() {
        return notification_text;
    }

    public void setNotification_text(String notification_text) {
        this.notification_text = notification_text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.notification_text);
        dest.writeString(this.created_at);
    }

    public AdminNotificationBO() {
    }

    protected AdminNotificationBO(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.notification_text = in.readString();
        this.created_at = in.readString();
    }

    public static final Parcelable.Creator<AdminNotificationBO> CREATOR = new Parcelable.Creator<AdminNotificationBO>() {
        @Override
        public AdminNotificationBO createFromParcel(Parcel source) {
            return new AdminNotificationBO(source);
        }

        @Override
        public AdminNotificationBO[] newArray(int size) {
            return new AdminNotificationBO[size];
        }
    };
}
