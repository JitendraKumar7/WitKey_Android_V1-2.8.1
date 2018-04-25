package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentsBO implements Parcelable {

    @SerializedName("id")
    @Expose
    public Integer comment_id = 0;
    @SerializedName("moment_id")
    @Expose
    public Integer moment_id = 0;
    @SerializedName("user_id")
    @Expose
    public Integer user_id = 0;
    @SerializedName("moment_user_id")
    @Expose
    public Integer moment_user_id = 0;
    @SerializedName("comment")
    @Expose
    public String comment;
    @SerializedName("created_at")
    @Expose
    public String created_at;

    @SerializedName("is_deleted")
    @Expose
    public Integer is_deleted = 0;
    @SerializedName("user_details")
    @Expose
    public UserDetailBO user_details;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public CommentsBO(int comment_id, int moment_id, int user_id, int moment_user_id, String comment, String time, UserDetailBO userDetailBO) {
        this.comment_id = comment_id;
        this.moment_id = moment_id;
        this.user_id = user_id;
        this.moment_user_id = moment_user_id;
        this.comment = comment;
        this.user_details = userDetailBO;
        this.created_at = time;
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public Integer getMoment_id() {
        return moment_id;
    }

    public void setMoment_id(Integer moment_id) {
        this.moment_id = moment_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getMoment_user_id() {
        return moment_user_id;
    }

    public void setMoment_user_id(Integer moment_user_id) {
        this.moment_user_id = moment_user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(Integer is_deleted) {
        this.is_deleted = is_deleted;
    }

    public UserDetailBO getUser_details() {
        return user_details;
    }

    public void setUser_details(UserDetailBO user_details) {
        this.user_details = user_details;
    }

    public CommentsBO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.comment_id);
        dest.writeValue(this.moment_id);
        dest.writeValue(this.user_id);
        dest.writeValue(this.moment_user_id);
        dest.writeString(this.comment);
        dest.writeString(this.created_at);
        dest.writeValue(this.is_deleted);
        dest.writeParcelable(this.user_details, flags);
    }

    protected CommentsBO(Parcel in) {
        this.comment_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.moment_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.moment_user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.comment = in.readString();
        this.created_at = in.readString();
        this.is_deleted = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_details = in.readParcelable(UserDetailBO.class.getClassLoader());
    }

    public static final Creator<CommentsBO> CREATOR = new Creator<CommentsBO>() {
        @Override
        public CommentsBO createFromParcel(Parcel source) {
            return new CommentsBO(source);
        }

        @Override
        public CommentsBO[] newArray(int size) {
            return new CommentsBO[size];
        }
    };
}
