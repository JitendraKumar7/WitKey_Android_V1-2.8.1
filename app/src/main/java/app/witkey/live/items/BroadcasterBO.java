package app.witkey.live.items;

import java.util.ArrayList;

/**
 * created by developer on 9/25/2017.
 */

public class BroadcasterBO {

    String userImageURL;
    String userName;
    String userPosition;
    boolean indexTop;
    ArrayList<BroadcasterBO> BroadcasterTopBO;

    public BroadcasterBO(String userImageURL_, String userName_, String userPosition_,
                         boolean indexTop_, ArrayList<BroadcasterBO> broadcasterTopBO_) {
        this.userImageURL = userImageURL_;
        this.userName = userName_;
        this.userPosition = userPosition_;
        this.indexTop = indexTop_;
        this.BroadcasterTopBO = broadcasterTopBO_;
    }

    public String getUserImageURL() {
        return userImageURL;
    }

    public void setUserImageURL(String userImageURL) {
        this.userImageURL = userImageURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(String userPosition) {
        this.userPosition = userPosition;
    }

    public boolean isIndexTop() {
        return indexTop;
    }

    public void setIndexTop(boolean indexTop) {
        this.indexTop = indexTop;
    }


    public ArrayList<BroadcasterBO> getBroadcasterTopBO() {
        return BroadcasterTopBO;
    }

    public void setBroadcasterTopBO(ArrayList<BroadcasterBO> broadcasterTopBO) {
        BroadcasterTopBO = broadcasterTopBO;
    }

}
