package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import app.witkey.live.utils.EnumUtils;

/**
 * created by developer on 10/03/2017.
 */

public class UserBO implements Parcelable {

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
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("profile_picture_url")
    @Expose
    public String profilePictureUrl;
    @SerializedName("dob")
    @Expose
    public String dateOfBirth;
    @SerializedName("token")
    @Expose
    public String token;
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
    @SerializedName("is_artist")
    @Expose
    public Integer isArtist = 0;
    @SerializedName("chips")
    @Expose
    public double chips = 0;
    @SerializedName("is_follow")
    @Expose
    public String is_follow = EnumUtils.FOLLOW_STATUSES.NOT_FOLLOW;
    @SerializedName("witkey_doller")
    @Expose
    public Double witkeyDollar = 0.0;

    @SerializedName("total_fans")
    @Expose
    public Integer totalFans = 0;

    @SerializedName("gift_send")
    @Expose
    public Integer totalGiftsSent = 0;
    @SerializedName("signup_type")
    @Expose
    public Integer signup_type = 0;
    @SerializedName("allow_notification")
    @Expose
    public Integer allow_notification = 0;
    @SerializedName("promotion_avail")
    @Expose
    public Integer promotion_avail = 0;
    @SerializedName("user_complete_id")
    @Expose
    public String user_complete_id;
    @SerializedName("message_notification_status")
    @Expose
    public Integer message_notification_status = 2; //{1: allow, 2: not allow}
    @SerializedName("live_notification_status")
    @Expose
    public Integer live_notification_status = 2;// {1: allow, 2: not allow}
    @SerializedName("user_progress_detail")
    @Expose
    public UserProgressDetailBO userProgressDetailBO;
    @SerializedName("welcome_message") /*IF DATA THEN KEY OTHER WISE NO KEY NULL*/
    @Expose
    public List<NotificationMessageBO> notificationMessageBO;

    @SerializedName("is_block")
    @Expose
    public Integer is_block = 0;

    public Integer getIs_block() {
        return is_block;
    }

    public void setIs_block(Integer is_block) {
        this.is_block = is_block;
    }

    //sample id BC/5001-222-12
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

    public List<NotificationMessageBO> getNotificationMessageBO() {
        return notificationMessageBO;
    }

    public void setNotificationMessageBO(List<NotificationMessageBO> notificationMessageBO) {
        this.notificationMessageBO = notificationMessageBO;
    }

    public Integer getPromotion_avail() {
        return promotion_avail;
    }

    public void setPromotion_avail(Integer promotion_avail) {
        this.promotion_avail = promotion_avail;
    }

    public Integer getMessage_notification_status() {
        return message_notification_status;
    }

    public void setMessage_notification_status(Integer message_notification_status) {
        this.message_notification_status = message_notification_status;
    }

    public Integer getLive_notification_status() {
        return live_notification_status;
    }

    public void setLive_notification_status(Integer live_notification_status) {
        this.live_notification_status = live_notification_status;
    }

    public Integer getAllow_notification() {
        return allow_notification;
    }

    public void setAllow_notification(Integer allow_notification) {
        this.allow_notification = allow_notification;
    }

    public Integer getSignup_type() {
        return signup_type;
    }

    public void setSignup_type(Integer signup_type) {
        this.signup_type = signup_type;
    }

    public UserProgressDetailBO getUserProgressDetailBO() {
        return userProgressDetailBO;
    }

    public void setUserProgressDetailBO(UserProgressDetailBO userProgressDetailBO) {
        this.userProgressDetailBO = userProgressDetailBO;
    }

    public Integer getTotalFans() {
        return totalFans;
    }

    public void setTotalFans(Integer totalFans) {
        this.totalFans = totalFans;
    }

    public Integer getTotalGiftsSent() {
        return totalGiftsSent;
    }

    public void setTotalGiftsSent(Integer totalGiftsSent) {
        this.totalGiftsSent = totalGiftsSent;
    }

    public Double getWitkeyDollar() {
        return Math.ceil(witkeyDollar);
    }

    public void setWitkeyDollar(Double witkeyDollar) {
        this.witkeyDollar = witkeyDollar;
    }

    public String getCountry() {
        if (!TextUtils.isEmpty(country))
            return country;
        else
            return "N/A";
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDateOfBirth() {
        if (!TextUtils.isEmpty(dateOfBirth))
            return dateOfBirth;
        else
            return "N/A";
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getTotalStreams() {
        return totalStreams;
    }

    /* public Integer getChips() {
         return chips;
     }

     public void setChips(Integer chips) {
         this.chips = chips;
     }*/
    public double getChips() {
        return Math.ceil(chips);
    }

    public void setChips(double chips) {
        this.chips = chips;
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

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public Integer getTotalFriends() {
        return totalFriends;
    }

    public void setTotalFriends(Integer totalFriends) {
        this.totalFriends = totalFriends;
    }

    public String getId() {
        return id==null?"2":id;
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
        return fullName;
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

    public Integer getIsArtist() {
        return isArtist;
    }

    public void setIsArtist(Integer isArtist) {
        this.isArtist = isArtist;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserBO() {
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
        dest.writeString(this.country);
        dest.writeString(this.profilePictureUrl);
        dest.writeString(this.dateOfBirth);
        dest.writeString(this.token);
        dest.writeValue(this.totalStreams);
        dest.writeValue(this.totalFollowers);
        dest.writeValue(this.totalFollowings);
        dest.writeValue(this.totalFriends);
        dest.writeValue(this.isArtist);
        dest.writeDouble(this.chips);
        dest.writeString(this.is_follow);
        dest.writeValue(this.witkeyDollar);
        dest.writeValue(this.totalFans);
        dest.writeValue(this.totalGiftsSent);
        dest.writeValue(this.signup_type);
        dest.writeValue(this.allow_notification);
        dest.writeValue(this.promotion_avail);
        dest.writeString(this.user_complete_id);
        dest.writeValue(this.message_notification_status);
        dest.writeValue(this.live_notification_status);
        dest.writeParcelable(this.userProgressDetailBO, flags);
        dest.writeTypedList(this.notificationMessageBO);
        dest.writeValue(this.is_block);
    }

    protected UserBO(Parcel in) {
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
        this.country = in.readString();
        this.profilePictureUrl = in.readString();
        this.dateOfBirth = in.readString();
        this.token = in.readString();
        this.totalStreams = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalFollowers = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalFollowings = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalFriends = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isArtist = (Integer) in.readValue(Integer.class.getClassLoader());
        this.chips = in.readDouble();
        this.is_follow = in.readString();
        this.witkeyDollar = (Double) in.readValue(Double.class.getClassLoader());
        this.totalFans = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalGiftsSent = (Integer) in.readValue(Integer.class.getClassLoader());
        this.signup_type = (Integer) in.readValue(Integer.class.getClassLoader());
        this.allow_notification = (Integer) in.readValue(Integer.class.getClassLoader());
        this.promotion_avail = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_complete_id = in.readString();
        this.message_notification_status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.live_notification_status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.userProgressDetailBO = in.readParcelable(UserProgressDetailBO.class.getClassLoader());
        this.notificationMessageBO = in.createTypedArrayList(NotificationMessageBO.CREATOR);
        this.is_block = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Creator<UserBO> CREATOR = new Creator<UserBO>() {
        @Override
        public UserBO createFromParcel(Parcel source) {
            return new UserBO(source);
        }

        @Override
        public UserBO[] newArray(int size) {
            return new UserBO[size];
        }
    };
}
