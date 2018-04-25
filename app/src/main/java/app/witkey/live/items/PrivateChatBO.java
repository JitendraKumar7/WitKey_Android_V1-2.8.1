package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrivateChatBO implements Parcelable {

    @SerializedName("friend_user_id")
    @Expose
    public Integer friend_user_id = 0;
    @SerializedName("chat_id")
    @Expose
    public String chat_id;
    @SerializedName("user_details")
    @Expose
    public UserDetailBO user_details;

    public boolean showDesc = false;
    public String adminTitle;
    public String adminDesc;
    public String adminURL;

    public PrivateChatBO(boolean showDesc_, String adminTitle_, String adminDesc_, String adminURL_, String chat_id_) {

        this.showDesc = showDesc_;
        this.adminTitle = adminTitle_;
        this.adminDesc = adminDesc_;
        this.adminURL = adminURL_;
        this.chat_id = chat_id_;
    }

    public String getAdminTitle() {
        return adminTitle;
    }

    public void setAdminTitle(String adminTitle) {
        this.adminTitle = adminTitle;
    }

    public String getAdminDesc() {
        return adminDesc;
    }

    public void setAdminDesc(String adminDesc) {
        this.adminDesc = adminDesc;
    }

    public String getAdminURL() {
        return adminURL;
    }

    public void setAdminURL(String adminURL) {
        this.adminURL = adminURL;
    }

    public boolean isShowDesc() {
        return showDesc;
    }

    public void setShowDesc(boolean showDesc) {
        this.showDesc = showDesc;
    }

    public Integer getFriend_user_id() {
        return friend_user_id;
    }

    public void setFriend_user_id(Integer friend_user_id) {
        this.friend_user_id = friend_user_id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public UserDetailBO getUser_details() {
        return user_details;
    }

    public void setUser_details(UserDetailBO user_details) {
        this.user_details = user_details;
    }

    public PrivateChatBO() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.friend_user_id);
        dest.writeString(this.chat_id);
        dest.writeParcelable(this.user_details, flags);
        dest.writeByte(this.showDesc ? (byte) 1 : (byte) 0);
        dest.writeString(this.adminTitle);
        dest.writeString(this.adminDesc);
        dest.writeString(this.adminURL);
    }

    protected PrivateChatBO(Parcel in) {
        this.friend_user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.chat_id = in.readString();
        this.user_details = in.readParcelable(UserDetailBO.class.getClassLoader());
        this.showDesc = in.readByte() != 0;
        this.adminTitle = in.readString();
        this.adminDesc = in.readString();
        this.adminURL = in.readString();
    }

    public static final Creator<PrivateChatBO> CREATOR = new Creator<PrivateChatBO>() {
        @Override
        public PrivateChatBO createFromParcel(Parcel source) {
            return new PrivateChatBO(source);
        }

        @Override
        public PrivateChatBO[] newArray(int size) {
            return new PrivateChatBO[size];
        }
    };
}
