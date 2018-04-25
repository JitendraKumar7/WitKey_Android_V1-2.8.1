package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LikedUsersBO implements Parcelable {

    @SerializedName("user_id")
    @Expose
    public Integer user_id = 0;
    @SerializedName("updated_at")
    @Expose
    public String updated_at;
    @SerializedName("created_at")
    @Expose
    public String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @SerializedName("user_details")
    @Expose
    public UserDetailBO user_details;

    public LikedUsersBO(int user_id, String updated_at, UserDetailBO userDetailBO) {
        this.user_id = user_id;
        this.updated_at = updated_at;
        this.user_details = userDetailBO;
    }


    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
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

    public LikedUsersBO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.user_id);
        dest.writeString(this.updated_at);
        dest.writeString(this.created_at);
        dest.writeParcelable(this.user_details, flags);
    }

    protected LikedUsersBO(Parcel in) {
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.updated_at = in.readString();
        this.created_at = in.readString();
        this.user_details = in.readParcelable(UserDetailBO.class.getClassLoader());
    }

    public static final Creator<LikedUsersBO> CREATOR = new Creator<LikedUsersBO>() {
        @Override
        public LikedUsersBO createFromParcel(Parcel source) {
            return new LikedUsersBO(source);
        }

        @Override
        public LikedUsersBO[] newArray(int size) {
            return new LikedUsersBO[size];
        }
    };
}
