package app.witkey.live.items;

public class WalletBO {

    String chips;
    String freeChips;
    double chipsPrice;

    public WalletBO(String chips, String freeChips, double chipsPrice) {
        this.chips = chips;
        this.freeChips = freeChips;
        this.chipsPrice = chipsPrice;
    }

    public String getChips() {
        return chips;
    }

    public void setChips(String chips) {
        this.chips = chips;
    }

    public String getFreeChips() {
        return freeChips;
    }

    public void setFreeChips(String freeChips) {
        this.freeChips = freeChips;
    }

    public double getChipsPrice() {
        return chipsPrice;
    }

    public void setChipsPrice(double chipsPrice) {
        this.chipsPrice = chipsPrice;
    }
}
