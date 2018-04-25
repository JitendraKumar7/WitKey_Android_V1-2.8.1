package app.witkey.live.items;

/**
 * created by developer on 9/23/2017.
 */

public class FeaturedBO {

    String userImageURL;
    String userName;
    String lastSeenTime;
    String viewsNo;
    String cardText;
    String cardImage;

    public FeaturedBO(String userImageURL_, String userName_, String lastSeenTime_, String viewsNo_, String cardText_, String cardImage_) {
        this.userImageURL = userImageURL_;
        this.userName = userName_;
        this.lastSeenTime = lastSeenTime_;
        this.viewsNo = viewsNo_;
        this.cardImage = cardImage_;
        this.cardText = cardText_;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public String getLastSeenTime() {
        return lastSeenTime;
    }

    public void setLastSeenTime(String lastSeenTime) {
        this.lastSeenTime = lastSeenTime;
    }

    public String getViewsNo() {
        return viewsNo;
    }

    public void setViewsNo(String viewsNo) {
        this.viewsNo = viewsNo;
    }

    public String getCardText() {
        return cardText;
    }

    public void setCardText(String cardText) {
        this.cardText = cardText;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String cardImage) {
        this.cardImage = cardImage;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
