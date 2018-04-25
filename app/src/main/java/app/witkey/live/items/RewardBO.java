package app.witkey.live.items;

public class RewardBO {

    private String imageSource;
    private int imageSourceLocal;

    public RewardBO() {
    }

    public RewardBO(String imageSource, int imageSourceLocal) {
        this.imageSource = imageSource;
        this.imageSourceLocal = imageSourceLocal;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public int getImageSourceLocal() {
        return imageSourceLocal;
    }

    public void setImageSourceLocal(int imageSourceLocal) {
        this.imageSourceLocal = imageSourceLocal;
    }
}
