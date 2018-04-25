package app.witkey.live.items;

public class GiftReceivedBO {

    private int localImage;
    private String timeLeft;

    public GiftReceivedBO(int localImage, String timeLeft) {
        this.localImage = localImage;
        this.timeLeft = timeLeft;
    }

    public int getLocalImage() {
        return localImage;
    }

    public void setLocalImage(int localImage) {
        this.localImage = localImage;
    }

    public String getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        this.timeLeft = timeLeft;
    }
}
