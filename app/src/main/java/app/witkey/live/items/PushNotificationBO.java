package app.witkey.live.items;

public class PushNotificationBO {

    //{"type":11,"body":"Some one foll0wing u","title":"You have new Fan!"}

    private int type;
    private String body;
    private String title;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
