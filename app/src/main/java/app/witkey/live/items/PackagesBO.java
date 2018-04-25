package app.witkey.live.items;

/**
 * created by developer on 9/25/2017.
 */

public class PackagesBO {

     /*"id":1,
       "amount":0.99,
       "witky_chips":150,
       "free_chips":0,
       "allow_promotion":0,
       "Promotion":0*/

    int id = 0;
    double amount = 0;
    int witky_chips = 0;
    int allow_promotion = 0;
    int Promotion = 0;
    int free_chips = 0;

    public int getFree_chips() {
        return free_chips;
    }

    public void setFree_chips(int free_chips) {
        this.free_chips = free_chips;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getWitky_chips() {
        return witky_chips;
    }

    public void setWitky_chips(int witky_chips) {
        this.witky_chips = witky_chips;
    }

    public int getAllow_promotion() {
        return allow_promotion;
    }

    public void setAllow_promotion(int allow_promotion) {
        this.allow_promotion = allow_promotion;
    }

    public int getPromotion() {
        return Promotion;
    }

    public void setPromotion(int promotion) {
        Promotion = promotion;
    }

}
