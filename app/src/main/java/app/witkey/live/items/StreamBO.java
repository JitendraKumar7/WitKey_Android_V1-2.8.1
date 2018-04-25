package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import app.witkey.live.Constants;
import app.witkey.live.utils.DateTimeOp;

/**
 * created by developer on 10/03/2017.
 */

public class StreamBO implements Parcelable {

    @SerializedName("stream_name")
    @Expose
    public String  stream_name;
    @SerializedName("stream_playback_url")
    @Expose
    public String  stream_playback_url;
    @SerializedName("stream_exp")
    @Expose
    public String  stream_exp;
    @SerializedName("stream_picture")
    @Expose
    public String  stream_picture;
    @SerializedName("deleted_at")
    @Expose
    public String  deleted_at;

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("uuid")
    @Expose
    public String uuid;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("stream_ip")
    @Expose
    public String streamIp;
    @SerializedName("stream_port")
    @Expose
    public String streamPort;
    @SerializedName("stream_app")
    @Expose
    public String streamApp;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("privacy_setting")
    @Expose
    public String privacySetting;
    @SerializedName("quality")
    @Expose
    public String quality;
    @SerializedName("is_public")
    @Expose
    public String isPublic;
    @SerializedName("allow_comments")
    @Expose
    public String allowComments;
    @SerializedName("allow_tag_requests")
    @Expose
    public String allowTagRequests;
    @SerializedName("available_later")
    @Expose
    public String availableLater;
    @SerializedName("lng")
    @Expose
    public String lng;
    @SerializedName("lat")
    @Expose
    public String lat;
    @SerializedName("stream_id")
    @Expose
    public String streamID;
    @SerializedName("total_likes")
    @Expose
    public String totalLikes;
    @SerializedName("total_dislikes")
    @Expose
    public String totalDislikes;
    @SerializedName("total_shares")
    @Expose
    public String totalShares;
    @SerializedName("total_viewers")
    @Expose
    public String totalViewers;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("status_text")
    @Expose
    public String statusText;
    @SerializedName("quality_text")
    @Expose
    public String qualityText;
    @SerializedName("user_details")
    @Expose
    public UserDetailBO user_details;
    @SerializedName("start_time")
    @Expose
    public String start_time;

    @SerializedName("streamUsername")
    @Expose
    public String stream_username;

    @SerializedName("streamPassword")
    @Expose
    public String stream_password;

    public boolean isStreaming;
    public boolean isFeatured;



    public String getStart_time() {
        if (!TextUtils.isEmpty(start_time)) {
            String time = DateTimeOp.oneFormatToAnother(start_time, Constants.dateFormat12, Constants.dateFormat30);
            if (!time.isEmpty())
                return time;
            else
                return "00:00";
        } else
            return "00:00";
    }

    public String getStream_name() {
        return stream_name;
    }

    public void setStream_name(String stream_name) {
        this.stream_name = stream_name;
    }

    public String getStream_playback_url() {
        return stream_playback_url;
    }

    public void setStream_playback_url(String stream_playback_url) {
        this.stream_playback_url = stream_playback_url;
    }

    public String getStream_exp() {
        return stream_exp;
    }

    public void setStream_exp(String stream_exp) {
        this.stream_exp = stream_exp;
    }

    public String getStream_picture() {
        return stream_picture;
    }

    public void setStream_picture(String stream_picture) {
        this.stream_picture = stream_picture;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public boolean isStreaming() {
        return isStreaming;
    }

    public void setStreaming(boolean streaming) {
        isStreaming = streaming;
    }

    public String getStream_username() {
        return stream_username;
    }

    public void setStream_username(String stream_username) {
        this.stream_username = stream_username;
    }

    public String getStream_password() {
            return stream_password;
    }

    public void setStream_password(String stream_password) {
        this.stream_password = stream_password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStreamID() {
        return streamID;
    }

    public void setStreamID(String streamID) {
        this.streamID = streamID;
    }

    public String getName() {
        if (!TextUtils.isEmpty(name))
            return name;
        else
            return "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreamIp() {
        return streamIp;
    }

    public void setStreamIp(String streamIp) {
        this.streamIp = streamIp;
    }

    public String getStreamPort() {
        return streamPort;
    }

    public void setStreamPort(String streamPort) {
        this.streamPort = streamPort;
    }

    public String getStreamApp() {
        return streamApp;
    }

    public void setStreamApp(String streamApp) {
        this.streamApp = streamApp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrivacySetting() {
        return privacySetting;
    }

    public void setPrivacySetting(String privacySetting) {
        this.privacySetting = privacySetting;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getAllowComments() {
        return allowComments;
    }

    public void setAllowComments(String allowComments) {
        this.allowComments = allowComments;
    }

    public String getAllowTagRequests() {
        return allowTagRequests;
    }

    public void setAllowTagRequests(String allowTagRequests) {
        this.allowTagRequests = allowTagRequests;
    }

    public String getAvailableLater() {
        return availableLater;
    }

    public void setAvailableLater(String availableLater) {
        this.availableLater = availableLater;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
    }

    public String getTotalDislikes() {
        return totalDislikes;
    }

    public void setTotalDislikes(String totalDislikes) {
        this.totalDislikes = totalDislikes;
    }

    public String getTotalShares() {
        return totalShares;
    }

    public void setTotalShares(String totalShares) {
        this.totalShares = totalShares;
    }

    public String getTotalViewers() {
        if (!TextUtils.isEmpty(totalViewers))
            return totalViewers;
        else
            return "0";
    }

    public void setTotalViewers(String totalViewers) {
        this.totalViewers = totalViewers;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getQualityText() {
        return qualityText;
    }

    public void setQualityText(String qualityText) {
        this.qualityText = qualityText;
    }

    public UserDetailBO getUser_details() {
        return user_details;
    }

    public void setUser_details(UserDetailBO user_details) {
        this.user_details = user_details;
    }

    public StreamBO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stream_name);
        dest.writeString(this.stream_playback_url);
        dest.writeString(this.stream_exp);
        dest.writeString(this.stream_picture);
        dest.writeString(this.deleted_at);
        dest.writeString(this.id);
        dest.writeString(this.userId);
        dest.writeString(this.uuid);
        dest.writeString(this.name);
        dest.writeString(this.streamIp);
        dest.writeString(this.streamPort);
        dest.writeString(this.streamApp);
        dest.writeString(this.status);
        dest.writeString(this.privacySetting);
        dest.writeString(this.quality);
        dest.writeString(this.isPublic);
        dest.writeString(this.allowComments);
        dest.writeString(this.allowTagRequests);
        dest.writeString(this.availableLater);
        dest.writeString(this.lng);
        dest.writeString(this.lat);
        dest.writeString(this.streamID);
        dest.writeString(this.totalLikes);
        dest.writeString(this.totalDislikes);
        dest.writeString(this.totalShares);
        dest.writeString(this.totalViewers);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.statusText);
        dest.writeString(this.qualityText);
        dest.writeParcelable(this.user_details, flags);
        dest.writeString(this.start_time);
        dest.writeString(this.stream_username);
        dest.writeString(this.stream_password);
        dest.writeByte(this.isStreaming ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFeatured ? (byte) 1 : (byte) 0);
    }

    protected StreamBO(Parcel in) {
        this.stream_name = in.readString();
        this.stream_playback_url = in.readString();
        this.stream_exp = in.readString();
        this.stream_picture = in.readString();
        this.deleted_at = in.readString();
        this.id = in.readString();
        this.userId = in.readString();
        this.uuid = in.readString();
        this.name = in.readString();
        this.streamIp = in.readString();
        this.streamPort = in.readString();
        this.streamApp = in.readString();
        this.status = in.readString();
        this.privacySetting = in.readString();
        this.quality = in.readString();
        this.isPublic = in.readString();
        this.allowComments = in.readString();
        this.allowTagRequests = in.readString();
        this.availableLater = in.readString();
        this.lng = in.readString();
        this.lat = in.readString();
        this.streamID = in.readString();
        this.totalLikes = in.readString();
        this.totalDislikes = in.readString();
        this.totalShares = in.readString();
        this.totalViewers = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.statusText = in.readString();
        this.qualityText = in.readString();
        this.user_details = in.readParcelable(UserDetailBO.class.getClassLoader());
        this.start_time = in.readString();
        this.stream_username = in.readString();
        this.stream_password = in.readString();
        this.isStreaming = in.readByte() != 0;
        this.isFeatured = in.readByte() != 0;
    }

    public static final Creator<StreamBO> CREATOR = new Creator<StreamBO>() {
        @Override
        public StreamBO createFromParcel(Parcel source) {
            return new StreamBO(source);
        }

        @Override
        public StreamBO[] newArray(int size) {
            return new StreamBO[size];
        }
    };
}
