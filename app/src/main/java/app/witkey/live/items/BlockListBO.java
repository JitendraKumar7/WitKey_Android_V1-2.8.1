package app.witkey.live.items;

public class BlockListBO {

    String ImageURL;
    String name;
    boolean isBlocked;

    public BlockListBO(String imageURL, String name, boolean isBlocked) {
        ImageURL = imageURL;
        this.name = name;
        this.isBlocked = isBlocked;
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

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
