package com.customer.orderproupdated.chat.bean;

/**
 * Created by Advantal on 29-12-2016.
 */
public class MessageLog {

    private String message;
    private String timestamp;

    public MessageLog(String message, String timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
