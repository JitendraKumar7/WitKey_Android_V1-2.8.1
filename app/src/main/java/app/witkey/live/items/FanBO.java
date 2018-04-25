package app.witkey.live.items;

public class FanBO {

    String ImageURL;
    String name;
    boolean isFollowing;

    public FanBO(String imageURL, String name, boolean isFollowing) {
        ImageURL = imageURL;
        this.name = name;
        this.isFollowing = isFollowing;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}
