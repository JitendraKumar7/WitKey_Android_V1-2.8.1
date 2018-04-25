package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MomentBO implements Parcelable {


    @SerializedName("id")
    @Expose
    public Integer moment_id = 0;
    @SerializedName("user_id")
    @Expose
    public Integer user_id = 0;
    @SerializedName("status_text")
    @Expose
    public String user_status_text;
    @SerializedName("image_array")
    @Expose
    public String[] image_arrsy;
    @SerializedName("total_comments")
    @Expose
    public Integer total_comments = 0;

    @SerializedName("is_like")
    @Expose
    public Integer is_like = 0;
    @SerializedName("total_likes")
    @Expose
    public Integer total_likes = 0;
    @SerializedName("created_at")
    @Expose
    public String created_at;
    @SerializedName("user_detail")
    @Expose
    public UserDetailBO user_details;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Integer getTotal_comments() {
        return total_comments;
    }

    public void setTotal_comments(Integer total_comments) {
        this.total_comments = total_comments;
    }

    public Integer getTotal_likes() {
        return total_likes;
    }

    public void setTotal_likes(Integer total_likes) {
        this.total_likes = total_likes;
    }

    public UserDetailBO getUser_details() {
        return user_details;
    }

    public void setUser_details(UserDetailBO user_details) {
        this.user_details = user_details;
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

    public String getUser_status_text() {
        return user_status_text;
    }

    public void setUser_status_text(String user_status_text) {
        this.user_status_text = user_status_text;
    }

    public String[] getImage_arrsy() {
        return image_arrsy;
    }

    public void setImage_arrsy(String[] image_arrsy) {
        this.image_arrsy = image_arrsy;
    }

    public Integer getIs_like() {
        return is_like;
    }

    public void setIs_like(Integer is_like) {
        this.is_like = is_like;
    }

    public MomentBO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.moment_id);
        dest.writeValue(this.user_id);
        dest.writeString(this.user_status_text);
        dest.writeStringArray(this.image_arrsy);
        dest.writeValue(this.total_comments);
        dest.writeValue(this.is_like);
        dest.writeValue(this.total_likes);
        dest.writeString(this.created_at);
        dest.writeParcelable(this.user_details, flags);
    }

    protected MomentBO(Parcel in) {
        this.moment_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_status_text = in.readString();
        this.image_arrsy = in.createStringArray();
        this.total_comments = (Integer) in.readValue(Integer.class.getClassLoader());
        this.is_like = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total_likes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.created_at = in.readString();
        this.user_details = in.readParcelable(UserDetailBO.class.getClassLoader());
    }

    public static final Creator<MomentBO> CREATOR = new Creator<MomentBO>() {
        @Override
        public MomentBO createFromParcel(Parcel source) {
            return new MomentBO(source);
        }

        @Override
        public MomentBO[] newArray(int size) {
            return new MomentBO[size];
        }
    };
}
