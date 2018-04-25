package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import app.witkey.live.utils.EnumUtils;

/**
 * created by developer on 10/03/2017.
 */

public class UserDetailBO implements Parcelable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("role_id")
    @Expose
    public String roleId;
    @SerializedName("full_name")
    @Expose
    public String fullName;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("status_text")
    @Expose
    public String statusText;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("mobile_no")
    @Expose
    public String mobileNo;
    @SerializedName("is_picture")
    @Expose
    public String isPicture;
    @SerializedName("privacy_setting")
    @Expose
    public String privacySetting;
    @SerializedName("notification_status")
    @Expose
    public String notificationStatus;
    @SerializedName("device_type")
    @Expose
    public String deviceType;
    @SerializedName("device_token")
    @Expose
    public String deviceToken;
    @SerializedName("verification_code")
    @Expose
    public String verificationCode;
    @SerializedName("is_verified")
    @Expose
    public String isVerified;
    @SerializedName("status_text_value")
    @Expose
    public String statusTextValue;
    @SerializedName("role_name")
    @Expose
    public String roleName;
    @SerializedName("profile_picture_url")
    @Expose
    public String profilePictureUrl;
    @SerializedName("is_friend")
    @Expose
    public String is_friend = EnumUtils.FRIEND_STATUSES.NON_FRIEND;
    @SerializedName("is_follow")
    @Expose
    public String is_follow = EnumUtils.FOLLOW_STATUSES.NOT_FOLLOW;
    @SerializedName("total_streams")
    @Expose
    public Integer totalStreams = 0;
    @SerializedName("total_followers")
    @Expose
    public Integer totalFollowers = 0;
    @SerializedName("total_followings")
    @Expose
    public Integer totalFollowings = 0;
    @SerializedName("total_friends")
    @Expose
    public Integer totalFriends = 0;
    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("user_progress_detail")
    @Expose
    public UserProgressDetailBO userProgressDetailBO;
    @SerializedName("witkey_doller")
    @Expose
    public Double witkeyDollar;
    @SerializedName("chips")
    @Expose
    public Double chips = 0.0;
    @SerializedName("is_block")
    @Expose
    public Integer is_block = 0;

    @SerializedName("dob")
    @Expose
    public String dateOfBirth;

    public String getDateOfBirth() {
        if (!TextUtils.isEmpty(dateOfBirth))
            return dateOfBirth;
        else
            return "N/A";
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getIs_block() {
        return is_block;
    }

    public void setIs_block(Integer is_block) {
        this.is_block = is_block;
    }

    public Integer getIsArtist() {
        return isArtist;
    }

    public void setIsArtist(Integer isArtist) {
        this.isArtist = isArtist;
    }

    @SerializedName("is_artist")
    @Expose
    public Integer isArtist = 0;

    @SerializedName("user_complete_id")
    @Expose
    public String user_complete_id;


    public String getUser_complete_id() {
        if (!TextUtils.isEmpty(user_complete_id)) {
            if (user_complete_id.contains("/")) {
                return user_complete_id.split("/")[1];
            } else {
                return user_complete_id;
            }
        } else {
            return id;
        }
    }

    public void setUser_complete_id(String user_complete_id) {
        this.user_complete_id = user_complete_id;
    }

    public Double getWitkeyDollar() {
        return Math.ceil(witkeyDollar);
    }

    public void setWitkeyDollar(Double witkeyDollar) {
        this.witkeyDollar = witkeyDollar;
    }

    public UserProgressDetailBO getUserProgressDetailBO() {
        return userProgressDetailBO;
    }

    public void setUserProgressDetailBO(UserProgressDetailBO userProgressDetailBO) {
        this.userProgressDetailBO = userProgressDetailBO;
    }

    public Double getChips() {
        return Math.ceil(chips);
    }

    public void setChips(Double chips) {
        this.chips = chips;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getTotalStreams() {
        return totalStreams;
    }

    public void setTotalStreams(Integer totalStreams) {
        this.totalStreams = totalStreams;
    }

    public Integer getTotalFollowers() {
        return totalFollowers;
    }

    public void setTotalFollowers(Integer totalFollowers) {
        this.totalFollowers = totalFollowers;
    }

    public Integer getTotalFollowings() {
        return totalFollowings;
    }

    public void setTotalFollowings(Integer totalFollowings) {
        this.totalFollowings = totalFollowings;
    }

    public Integer getTotalFriends() {
        return totalFriends;
    }

    public void setTotalFriends(Integer totalFriends) {
        this.totalFriends = totalFriends;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getFullName() {
        if (!TextUtils.isEmpty(fullName))
            return fullName;
        else
            return "";
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        if (!TextUtils.isEmpty(fullName)) {
            return fullName;
        } else if (!TextUtils.isEmpty(username))
            return username;
        else if (!TextUtils.isEmpty(username)) {
            return email;
        } else {
            return "";
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getIsPicture() {
        return isPicture;
    }

    public void setIsPicture(String isPicture) {
        this.isPicture = isPicture;
    }

    public String getPrivacySetting() {
        return privacySetting;
    }

    public void setPrivacySetting(String privacySetting) {
        this.privacySetting = privacySetting;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        this.isVerified = isVerified;
    }

    public String getStatusTextValue() {
        return statusTextValue;
    }

    public void setStatusTextValue(String statusTextValue) {
        this.statusTextValue = statusTextValue;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getIs_friend() {
        if (!TextUtils.isEmpty(is_friend))
            return is_friend;
        else
            return "0";
    }

    public void setIs_friend(String is_friend) {
        this.is_friend = is_friend;
    }

    public UserDetailBO() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.roleId);
        dest.writeString(this.fullName);
        dest.writeString(this.username);
        dest.writeString(this.email);
        dest.writeString(this.status);
        dest.writeString(this.statusText);
        dest.writeString(this.gender);
        dest.writeString(this.mobileNo);
        dest.writeString(this.isPicture);
        dest.writeString(this.privacySetting);
        dest.writeString(this.notificationStatus);
        dest.writeString(this.deviceType);
        dest.writeString(this.deviceToken);
        dest.writeString(this.verificationCode);
        dest.writeString(this.isVerified);
        dest.writeString(this.statusTextValue);
        dest.writeString(this.roleName);
        dest.writeString(this.profilePictureUrl);
        dest.writeString(this.is_friend);
        dest.writeString(this.is_follow);
        dest.writeValue(this.totalStreams);
        dest.writeValue(this.totalFollowers);
        dest.writeValue(this.totalFollowings);
        dest.writeValue(this.totalFriends);
        dest.writeString(this.token);
        dest.writeParcelable(this.userProgressDetailBO, flags);
        dest.writeValue(this.witkeyDollar);
        dest.writeValue(this.chips);
        dest.writeValue(this.is_block);
        dest.writeString(this.dateOfBirth);
        dest.writeValue(this.isArtist);
        dest.writeString(this.user_complete_id);
    }

    protected UserDetailBO(Parcel in) {
        this.id = in.readString();
        this.roleId = in.readString();
        this.fullName = in.readString();
        this.username = in.readString();
        this.email = in.readString();
        this.status = in.readString();
        this.statusText = in.readString();
        this.gender = in.readString();
        this.mobileNo = in.readString();
        this.isPicture = in.readString();
        this.privacySetting = in.readString();
        this.notificationStatus = in.readString();
        this.deviceType = in.readString();
        this.deviceToken = in.readString();
        this.verificationCode = in.readString();
        this.isVerified = in.readString();
        this.statusTextValue = in.readString();
        this.roleName = in.readString();
        this.profilePictureUrl = in.readString();
        this.is_friend = in.readString();
        this.is_follow = in.readString();
        this.totalStreams = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalFollowers = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalFollowings = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalFriends = (Integer) in.readValue(Integer.class.getClassLoader());
        this.token = in.readString();
        this.userProgressDetailBO = in.readParcelable(UserProgressDetailBO.class.getClassLoader());
        this.witkeyDollar = (Double) in.readValue(Double.class.getClassLoader());
        this.chips = (Double) in.readValue(Double.class.getClassLoader());
        this.is_block = (Integer) in.readValue(Integer.class.getClassLoader());
        this.dateOfBirth = in.readString();
        this.isArtist = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_complete_id = in.readString();
    }

    public static final Creator<UserDetailBO> CREATOR = new Creator<UserDetailBO>() {
        @Override
        public UserDetailBO createFromParcel(Parcel source) {
            return new UserDetailBO(source);
        }

        @Override
        public UserDetailBO[] newArray(int size) {
            return new UserDetailBO[size];
        }
    };
}
