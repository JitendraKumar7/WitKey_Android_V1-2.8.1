package app.witkey.live.items;

public class UserLevelBO {

    String levelTitle;
    int levelLocalImage;
    int levelFrom;
    int levelTo;

    public UserLevelBO(String levelTitle, int levelLocalImage) {
        this.levelTitle = levelTitle;
        this.levelLocalImage = levelLocalImage;
    }

    public UserLevelBO(String levelTitle, int levelLocalImage, int levelFrom, int levelTo) {
        this.levelTitle = levelTitle;
        this.levelLocalImage = levelLocalImage;
        this.levelFrom = levelFrom;
        this.levelTo = levelTo;
    }

    public int getLevelFrom() {
        return levelFrom;
    }

    public void setLevelFrom(int levelFrom) {
        this.levelFrom = levelFrom;
    }

    public int getLevelTo() {
        return levelTo;
    }

    public void setLevelTo(int levelTo) {
        this.levelTo = levelTo;
    }

    public String getLevelTitle() {
        return levelTitle;
    }

    public void setLevelTitle(String levelTitle) {
        this.levelTitle = levelTitle;
    }

    public int getLevelLocalImage() {
        return levelLocalImage;
    }

    public void setLevelLocalImage(int levelLocalImage) {
        this.levelLocalImage = levelLocalImage;
    }
}
