package app.witkey.live.items;

/**
 * created by developer on 6/1/2017.
 * this BO is used to send social data on pubnub..
 */

public class SocialDataBO {

    //    "likes":self.likeCount,"disLikes":self.disLikeCount,"shares":"","viewers":""
    private int likes = 0;
    private int disLikes = 0;
    private int gifts = 0;
    private String userID;


    public SocialDataBO(int likes, int disLikes, int gifts, String userID) {
        this.likes = likes;
        this.disLikes = disLikes;
        this.gifts = gifts;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDisLikes() {
        return disLikes;
    }

    public void setDisLikes(int disLikes) {
        this.disLikes = disLikes;
    }

    public int getGifts() {
        return gifts;
    }

    public void setGifts(int gifts) {
        this.gifts = gifts;
    }
}
