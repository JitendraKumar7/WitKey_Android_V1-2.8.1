package app.witkey.live.items;

public class GiftItemBO {

    private int itemID;
    private int itemCount;
    private int itemPrice;

    public GiftItemBO(int itemID_, int itemCount_, int itemPrice_) {
        this.itemID = itemID_;
        this.itemCount = itemCount_;
        this.itemPrice = itemPrice_;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

}
