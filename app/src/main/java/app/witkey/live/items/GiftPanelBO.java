package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;

public class GiftPanelBO implements Parcelable {


    /* "id": 1,
        "gift_key": 0,
        "name": "Baloon",
        "is_active": 1,
        "created_at": "2017-11-23 14:17:31",
        "updated_at": "2017-11-23 14:17:31",
        "type": "1",
        "price": 400,
        "gift_url": "http://18.220.157.19/witkey_dev/public/images-gift/1000.gif",
        "icon_url": "http://18.220.157.19/witkey_dev/public/images-gift/Gift_Entry_lollipop_Pop Me!_150.png",
        "gift_version": 1*/



    private int localImage;
    private int id;

    private String name;
    private int price;

    private String url;
    private int type;
    private int status; // LOCK UNLOCK STATUS
    private String isUpdateVersion;

    private String quantity;
    private boolean selected = false;

    public GiftPanelBO(int localImage, int id, String quantity, String name, int price, String url, int type, int status, String isUpdate) {
        this.localImage = localImage;
        this.id = id;
        this.quantity = quantity;

        this.name = name;
        this.price = price;

        this.url = url;
        this.type = type;
        this.status = status;
        this.isUpdateVersion = isUpdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLocalImage() {
        return localImage;
    }

    public void setLocalImage(int localImage) {
        this.localImage = localImage;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }
    public String getIsUpdateVersion() {
        return isUpdateVersion;
    }

    public void setIsUpdateVersion(String isUpdateVersion) {
        this.isUpdateVersion = isUpdateVersion;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.localImage);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.price);
        dest.writeString(this.url);
        dest.writeInt(this.type);
        dest.writeInt(this.status);
        dest.writeString(this.isUpdateVersion);
        dest.writeString(this.quantity);
        dest.writeByte(this.selected ? (byte) 1 : (byte) 0);
    }

    protected GiftPanelBO(Parcel in) {
        this.localImage = in.readInt();
        this.id = in.readInt();
        this.name = in.readString();
        this.price = in.readInt();
        this.url = in.readString();
        this.type = in.readInt();
        this.status = in.readInt();
        this.isUpdateVersion = in.readString();
        this.quantity = in.readString();
        this.selected = in.readByte() != 0;
    }

    public static final Creator<GiftPanelBO> CREATOR = new Creator<GiftPanelBO>() {
        @Override
        public GiftPanelBO createFromParcel(Parcel source) {
            return new GiftPanelBO(source);
        }

        @Override
        public GiftPanelBO[] newArray(int size) {
            return new GiftPanelBO[size];
        }
    };
}
