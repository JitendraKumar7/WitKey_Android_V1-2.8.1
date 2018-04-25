package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * created by developer on 5/8/2017.
 */

public class ConversationBO implements Parcelable {
    public static final int ME = 0;
    public static final int OTHER = 1;
//    public static final int FIRST_MESSAGE = 2;

    @SerializedName("dpUrl")
    @Expose
    String dpUrl;
    @SerializedName("date")
    @Expose
    String date;
    @SerializedName("senderName")
    @Expose
    String senderName;
    @SerializedName("senderId")
    @Expose
    String senderId;
    @SerializedName("text")
    @Expose
    String text;
    @SerializedName("username")
    @Expose
    String username;
    @SerializedName("socialGiftID")
    @Expose
    int socialGiftID;
    @SerializedName("socialGiftCount")
    @Expose
    int socialGiftCount;
    @SerializedName("conversationTypeFlag")
    @Expose
    int conversationTypeFlag;
    @SerializedName("giftPrice")
    @Expose
    double giftPrice;
    @SerializedName("giftURL")
    @Expose
    String giftURL;
    @SerializedName("type")
    @Expose
    int type;
    @SerializedName("count")
    @Expose
    String count;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getGiftURL() {
        return giftURL;
    }

    public void setGiftURL(String giftURL) {
        this.giftURL = giftURL;
    }

    public UserBO getUser_details() {
        return user_details;
    }

    public void setUser_details(UserBO user_details) {
        this.user_details = user_details;
    }

    @SerializedName("user_details")
    @Expose
    UserBO user_details;

    int mRowType = OTHER;

    public ConversationBO(String senderName, String mMessage, int mType, double giftPrice_) {
        this.senderName = senderName;
        this.text = mMessage;
        this.mRowType = mType;
        this.giftPrice = giftPrice_;
    }

    /*public ConversationBO(String dpUrl, String date, String senderName, String senderId,
                          String text, String username, int mRowType) {
        this.dpUrl = dpUrl;
        this.date = date;
        this.senderName = senderName;
        this.senderId = senderId;
        this.text = text;
        this.username = username;
        this.mRowType = mRowType;
    }*/
    /*FOR SOCIAL CHAT*/
    public ConversationBO(String dpUrl, String date, String senderName, String senderId,
                          String text, String username, int mRowType, int socialGiftID, double giftPrice_, int socialGiftCount_, String gifURL_, int type_) {
        this.dpUrl = dpUrl;
        this.date = date;
        this.senderName = senderName;
        this.senderId = senderId;
        this.text = text;
        this.username = username;
        this.conversationTypeFlag = mRowType;
        this.socialGiftID = socialGiftID;
        this.giftPrice = giftPrice_;
        this.count = socialGiftCount_ + "";
        this.giftURL = gifURL_;
        this.type = type_;

    }

    /*FOR LIVE STATUS TEXT WITH USER OBJECT TO SHOW USER (VIEWER, LIVE, ETC) DATA ON OTHER SIDE*/
    public ConversationBO(String dpUrl, String date, String senderName, String senderId,
                          String text, String username, int mRowType, int socialGiftID, double giftPrice_, UserBO user_details_, int socialGiftCount_) {
        this.dpUrl = dpUrl;
        this.date = date;
        this.senderName = senderName;
        this.senderId = senderId;
        this.text = text;
        this.username = username;
        this.conversationTypeFlag = mRowType;
        this.socialGiftID = socialGiftID;
        this.giftPrice = giftPrice_;
        this.user_details = user_details_;
        this.count = socialGiftCount_ + "";
    }

    public int getSocialGiftCount() {
        return socialGiftCount;
    }

    public void setSocialGiftCount(int socialGiftCount) {
        this.socialGiftCount = socialGiftCount;
    }

    public String getDpUrl() {
        return dpUrl;
    }

    public double getGiftPrice() {
        return giftPrice;
    }

    public void setGiftPrice(double giftPrice) {
        this.giftPrice = giftPrice;
    }

    public void setDpUrl(String dpUrl) {
        this.dpUrl = dpUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSenderName() {
        return senderName;
    }

    public int getSocialGiftID() {
        return socialGiftID;
    }

    public void setSocialGiftID(int socialGiftID) {
        this.socialGiftID = socialGiftID;
    }

    public int getConversationTypeFlag() {
        return conversationTypeFlag;
    }

    public void setConversationTypeFlag(int conversationTypeFlag) {
        this.conversationTypeFlag = conversationTypeFlag;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getmRowType() {
        return mRowType;
    }

    public void setmRowType(int mRowType) {
        this.mRowType = mRowType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dpUrl);
        dest.writeString(this.date);
        dest.writeString(this.senderName);
        dest.writeString(this.senderId);
        dest.writeString(this.text);
        dest.writeString(this.username);
        dest.writeInt(this.socialGiftID);
        dest.writeInt(this.socialGiftCount);
        dest.writeInt(this.conversationTypeFlag);
        dest.writeDouble(this.giftPrice);
        dest.writeString(this.giftURL);
        dest.writeInt(this.type);
        dest.writeString(this.count);
        dest.writeParcelable(this.user_details, flags);
        dest.writeInt(this.mRowType);
    }

    protected ConversationBO(Parcel in) {
        this.dpUrl = in.readString();
        this.date = in.readString();
        this.senderName = in.readString();
        this.senderId = in.readString();
        this.text = in.readString();
        this.username = in.readString();
        this.socialGiftID = in.readInt();
        this.socialGiftCount = in.readInt();
        this.conversationTypeFlag = in.readInt();
        this.giftPrice = in.readDouble();
        this.giftURL = in.readString();
        this.type = in.readInt();
        this.count = in.readString();
        this.user_details = in.readParcelable(UserBO.class.getClassLoader());
        this.mRowType = in.readInt();
    }

    public static final Creator<ConversationBO> CREATOR = new Creator<ConversationBO>() {
        @Override
        public ConversationBO createFromParcel(Parcel source) {
            return new ConversationBO(source);
        }

        @Override
        public ConversationBO[] newArray(int size) {
            return new ConversationBO[size];
        }
    };
}
