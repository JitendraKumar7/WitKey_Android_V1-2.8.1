package app.witkey.live.items;

/**
 * created by developer on 9/23/2017.
 */

public class NewGoddessMachoBO {

    String ImageURL;
    String micCount;
    String imageText;

    public NewGoddessMachoBO(String ImageURL_, String micCount_, String imageText_) {
        this.ImageURL = ImageURL_;
        this.micCount = micCount_;
        this.imageText = imageText_;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getMicCount() {
        return micCount;
    }

    public void setMicCount(String micCount) {
        this.micCount = micCount;
    }

    public String getImageText() {
        return imageText;
    }

    public void setImageText(String imageText) {
        this.imageText = imageText;
    }

}
