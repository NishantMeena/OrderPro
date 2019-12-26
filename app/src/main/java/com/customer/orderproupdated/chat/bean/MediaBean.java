package com.customer.orderproupdated.chat.bean;


import okhttp3.MediaType;

/**
 * Created by Rajesh Kushwah on 19-01-2017.
 */
public class MediaBean {

    private String messageId;
    private String date;
    private String filePath;
    private String fileStatus;
    private MediaType mType;

    public MediaType getmType() {
        return mType;
    }

    public void setmType(MediaType mType) {
        this.mType = mType;
    }

    public String getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
