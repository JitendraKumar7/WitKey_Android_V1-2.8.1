package app.witkey.live.items;

import android.net.Uri;

public class MomentImagesBO {

    private Uri momentImageUri;
    private String filePath;

    private boolean selected = false;

    public MomentImagesBO(Uri momentImageUri, String filePath) {
        this.momentImageUri = momentImageUri;
        this.filePath = filePath;
    }
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public Uri getMomentImageUri() {
        return momentImageUri;
    }

    public void setMomentImageUri(Uri momentImageUri) {
        this.momentImageUri = momentImageUri;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
