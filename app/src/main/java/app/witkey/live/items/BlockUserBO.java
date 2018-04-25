package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BlockUserBO implements Parcelable {

    @SerializedName("id")
    @Expose
    public Integer id = 0;
    @SerializedName("user_id")
    @Expose
    public Integer user_id = 0;
    @SerializedName("user_details")
    @Expose
    public UserDetailBO user_details;

    public BlockUserBO(int id, int user_id, UserDetailBO userDetailBO) {
        this.id = id;
        this.user_id = user_id;
        this.user_details = userDetailBO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BlockUserBO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.user_id);
        dest.writeParcelable(this.user_details, flags);
    }

    protected BlockUserBO(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_details = in.readParcelable(UserDetailBO.class.getClassLoader());
    }

    public static final Creator<BlockUserBO> CREATOR = new Creator<BlockUserBO>() {
        @Override
        public BlockUserBO createFromParcel(Parcel source) {
            return new BlockUserBO(source);
        }

        @Override
        public BlockUserBO[] newArray(int size) {
            return new BlockUserBO[size];
        }
    };
}
