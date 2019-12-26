package com.customer.orderproupdated.chat.bean;

/**
 * Created by Advantal on 10-11-2016.
 */
public class XEP {

    String messageId;
    String status;
    String friendJID;
    String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFriendJID() {
        return friendJID;
    }

    public void setFriendJID(String friendJID) {
        this.friendJID = friendJID;
    }
}
