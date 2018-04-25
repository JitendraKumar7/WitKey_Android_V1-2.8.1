package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProgressDetailBO implements Parcelable {

    /*@SerializedName("daily_mission_veiw_stream")
    @Expose
    boolean daily_mission_veiw_stream;
    @SerializedName("daily_mission_share_stream")
    @Expose
    boolean daily_mission_share_stream;
    @SerializedName("daily_mission_invite_friend")
    @Expose
    boolean daily_mission_invite_friend;*/

    @SerializedName("daily_mission_progress")
    @Expose
    int daily_mission_progress = 0;
    @SerializedName("user_enegry")
    @Expose
    int user_enegry = 0;
    @SerializedName("user_total_tornados")
    @Expose
    int user_total_tornados = 0;
    @SerializedName("user_current_tornados")
    @Expose
    int user_current_tornados = 0;
    @SerializedName("artist_total_tornados")
    @Expose
    int artist_total_tornados = 0;
    @SerializedName("artist_current_tornados")
    @Expose
    int artist_current_tornados = 0;
    @SerializedName("user_level")
    @Expose
    int user_level = 0;
    @SerializedName("artist_level")
    @Expose
    int artist_level = 0;

    @SerializedName("chips_spend")
    @Expose
    public double chips_spend = 0;
    @SerializedName("chips")
    @Expose
    public double chips = 0;
    @SerializedName("gift_send")
    @Expose
    public Integer totalGiftsSent = 0;
    @SerializedName("total_followers")
    @Expose
    public Integer totalFollowers = 0;
    @SerializedName("total_followings")
    @Expose
    public Integer totalFollowings = 0;
    @SerializedName("witkey_doller")
    @Expose
    public Double witkeyDollar = 0.0;

    public double getChips_spend() {
        return chips_spend;
    }

    public void setChips_spend(double chips_spend) {
        this.chips_spend = chips_spend;
    }

    public double getChips() {
        return chips;
    }

    public void setChips(double chips) {
        this.chips = chips;
    }

    public Integer getTotalGiftsSent() {
        return totalGiftsSent;
    }

    public void setTotalGiftsSent(Integer totalGiftsSent) {
        this.totalGiftsSent = totalGiftsSent;
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

    public Double getWitkeyDollar() {
        return witkeyDollar;
    }

    public void setWitkeyDollar(Double witkeyDollar) {
        this.witkeyDollar = witkeyDollar;
    }

    public int getDaily_mission_progress() {
        return daily_mission_progress;
    }

    public void setDaily_mission_progress(int daily_mission_progress) {
        this.daily_mission_progress = daily_mission_progress;
    }

    /*public boolean isDaily_mission_veiw_stream() {
        return daily_mission_veiw_stream;
    }

    public void setDaily_mission_veiw_stream(boolean daily_mission_veiw_stream) {
        this.daily_mission_veiw_stream = daily_mission_veiw_stream;
    }

    public boolean isDaily_mission_share_stream() {
        return daily_mission_share_stream;
    }

    public void setDaily_mission_share_stream(boolean daily_mission_share_stream) {
        this.daily_mission_share_stream = daily_mission_share_stream;
    }

    public boolean isDaily_mission_invite_friend() {
        return daily_mission_invite_friend;
    }

    public void setDaily_mission_invite_friend(boolean daily_mission_invite_friend) {
        this.daily_mission_invite_friend = daily_mission_invite_friend;
    }*/

    public int getUser_enegry() {
        return user_enegry;
    }

    public void setUser_enegry(int user_enegry) {
        this.user_enegry = user_enegry;
    }

    public int getUser_total_tornados() {
        return user_total_tornados;
    }

    public void setUser_total_tornados(int user_total_tornados) {
        this.user_total_tornados = user_total_tornados;
    }

    public int getUser_current_tornados() {
        return user_current_tornados;
    }

    public void setUser_current_tornados(int user_current_tornados) {
        this.user_current_tornados = user_current_tornados;
    }

    public int getArtist_total_tornados() {
        return artist_total_tornados;
    }

    public void setArtist_total_tornados(int artist_total_tornados) {
        this.artist_total_tornados = artist_total_tornados;
    }

    public int getArtist_current_tornados() {
        return artist_current_tornados;
    }

    public void setArtist_current_tornados(int artist_current_tornados) {
        this.artist_current_tornados = artist_current_tornados;
    }

    public int getUser_level() {
        return user_level;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public int getArtist_level() {
        return artist_level;
    }

    public void setArtist_level(int artist_level) {
        this.artist_level = artist_level;
    }

    public UserProgressDetailBO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.daily_mission_progress);
        dest.writeInt(this.user_enegry);
        dest.writeInt(this.user_total_tornados);
        dest.writeInt(this.user_current_tornados);
        dest.writeInt(this.artist_total_tornados);
        dest.writeInt(this.artist_current_tornados);
        dest.writeInt(this.user_level);
        dest.writeInt(this.artist_level);
        dest.writeDouble(this.chips_spend);
        dest.writeDouble(this.chips);
        dest.writeValue(this.totalGiftsSent);
        dest.writeValue(this.totalFollowers);
        dest.writeValue(this.totalFollowings);
        dest.writeValue(this.witkeyDollar);
    }

    protected UserProgressDetailBO(Parcel in) {
        this.daily_mission_progress = in.readInt();
        this.user_enegry = in.readInt();
        this.user_total_tornados = in.readInt();
        this.user_current_tornados = in.readInt();
        this.artist_total_tornados = in.readInt();
        this.artist_current_tornados = in.readInt();
        this.user_level = in.readInt();
        this.artist_level = in.readInt();
        this.chips_spend = in.readDouble();
        this.chips = in.readDouble();
        this.totalGiftsSent = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalFollowers = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalFollowings = (Integer) in.readValue(Integer.class.getClassLoader());
        this.witkeyDollar = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Creator<UserProgressDetailBO> CREATOR = new Creator<UserProgressDetailBO>() {
        @Override
        public UserProgressDetailBO createFromParcel(Parcel source) {
            return new UserProgressDetailBO(source);
        }

        @Override
        public UserProgressDetailBO[] newArray(int size) {
            return new UserProgressDetailBO[size];
        }
    };
}
