package com.customer.orderproupdated.bean;

/**
 * Created by Administrator on 7/7/2016.
 */
public class RecentChat {
    String Name;
    String ChatCount;
    String FriendJID;
    String LastMessage;
    String Time;



    public RecentChat() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getChatCount() {
        return ChatCount;
    }

    public void setChatCount(String chatCount) {
        ChatCount = chatCount;
    }

    public String getFriendJID() {
        return FriendJID;
    }

    public void setFriendJID(String friendJID) {
        FriendJID = friendJID;
    }


    public String getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
