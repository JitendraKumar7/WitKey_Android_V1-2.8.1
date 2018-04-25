package app.witkey.live.items;

public class ProfileItemsBO {

    String itemName;
    int itemLocalImage;
    String itemValue;

    public ProfileItemsBO(String itemName, int itemLocalImage, String itemValue) {
        this.itemName = itemName;
        this.itemLocalImage = itemLocalImage;
        this.itemValue = itemValue;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemLocalImage() {
        return itemLocalImage;
    }

    public void setItemLocalImage(int itemLocalImage) {
        this.itemLocalImage = itemLocalImage;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }
}
