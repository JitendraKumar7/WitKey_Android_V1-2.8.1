package app.witkey.live.items;

public class NotificationListBO {

    String ImageURL;
    String name;
    boolean isNotificationOn;

    public NotificationListBO(String imageURL, String name, boolean isNotificationOn) {
        ImageURL = imageURL;
        this.name = name;
        this.isNotificationOn = isNotificationOn;
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

    public boolean isNotificationOn() {
        return isNotificationOn;
    }

    public void setNotificationOn(boolean notificationOn) {
        isNotificationOn = notificationOn;
    }
}
