package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GiftPanelItemBO implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("gift_key")
    @Expose
    private Integer gift_key;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("status")
    @Expose
    private Integer is_active;

    @SerializedName("type")
    @Expose
    private Integer type;

    @SerializedName("price")
    @Expose
    private Double price;

    @SerializedName("gift_url")
    @Expose
    private String gif_url;

    @SerializedName("icon_url")
    @Expose
    private String icon_url;

    @SerializedName("gift_version")
    @Expose
    private Integer gift_version;

    private boolean selected = false;

    public GiftPanelItemBO(int id, int gift_key_, String name_, int is_active_, int type_, double price, String gif_url_, String icon_url_, int gift_version_) {
        this.id = id;
        this.gift_key = gift_key_;

        this.name = name_;
        this.is_active = is_active_;
        this.type = type_;
        this.price = price;
        this.gif_url = gif_url_;
        this.icon_url = icon_url_;

        this.gift_version = gift_version_;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGift_key() {
        return gift_key;
    }

    public void setGift_key(Integer gift_key) {
        this.gift_key = gift_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIs_active() {
        return is_active;
    }

    public void setIs_active(Integer is_active) {
        this.is_active = is_active;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getGif_url() {
        return gif_url;
    }

    public void setGif_url(String gif_url) {
        this.gif_url = gif_url;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public Integer getGift_version() {
        return gift_version;
    }

    public void setGift_version(Integer gift_version) {
        this.gift_version = gift_version;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public GiftPanelItemBO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeValue(this.gift_key);
        dest.writeString(this.name);
        dest.writeValue(this.is_active);
        dest.writeValue(this.type);
        dest.writeValue(this.price);
        dest.writeString(this.gif_url);
        dest.writeString(this.icon_url);
        dest.writeValue(this.gift_version);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected GiftPanelItemBO(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.gift_key = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.is_active = (Integer) in.readValue(Integer.class.getClassLoader());
        this.type = (Integer) in.readValue(Integer.class.getClassLoader());
        this.price = (Double) in.readValue(Double.class.getClassLoader());
        this.gif_url = in.readString();
        this.icon_url = in.readString();
        this.gift_version = (Integer) in.readValue(Integer.class.getClassLoader());
        this.selected = in.readByte() != 0;
    }

    public static final Creator<GiftPanelItemBO> CREATOR = new Creator<GiftPanelItemBO>() {
        @Override
        public GiftPanelItemBO createFromParcel(Parcel source) {
            return new GiftPanelItemBO(source);
        }

        @Override
        public GiftPanelItemBO[] newArray(int size) {
            return new GiftPanelItemBO[size];
        }
    };
}
