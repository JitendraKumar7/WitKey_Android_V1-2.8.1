package app.witkey.live.items;

/**
 * created by developer on 9/26/2017.
 */

public class MessageBO {

    String messageTime;
    String messageDate;
    String messageText;
    String messagetype;

    public MessageBO(String messageTime_, String messageDate_, String messageText_, String messagetype_) {
        this.messageTime = messageTime_;
        this.messageDate = messageDate_;
        this.messageText = messageText_;
        this.messagetype = messagetype_;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessagetype() {
        return messagetype;
    }

    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }
    
}
