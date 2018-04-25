package app.witkey.live.items;

public class GiftZipItemBO {

    String id;
    String Zip_url;
    int count;
    int gift_level;
    double version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZip_url() {
        return Zip_url;
    }

    public void setZip_url(String zip_url) {
        Zip_url = zip_url;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getGift_level() {
        return gift_level;
    }

    public void setGift_level(int gift_level) {
        this.gift_level = gift_level;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }
}
