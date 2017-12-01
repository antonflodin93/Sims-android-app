package se.miun.android_app.Model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Message implements Serializable {

    @SerializedName("messageId")
    private String messageId;

    @SerializedName("messageText")
    private String messageText;

    @SerializedName("messageLabel")
    private String messageLabel;

    @SerializedName("messageType")
    private String messageType;

    @SerializedName("time")
    private String time;

    @SerializedName("date")
    private String date;


    public Message(String messageLabel, String messageText) {
        this.messageLabel = messageLabel;
        this.messageText = messageText;
        this.messageType = "REGULAR";
    }

    public Message(String messageLabel, String messageText, String messageType) {
        this.messageLabel = messageLabel;
        this.messageText = messageText;
        this.messageType = messageType;
    }

    public Message(String messageLabel, String messageText, String messageType, String time, String date) {
        this.messageText = messageText;
        this.messageLabel = messageLabel;
        this.messageType = messageType;
        this.time = time;
        this.date = date;
    }

    public Message(){

    }

    public String getMessageLabel() {
        return messageLabel;
    }

    public void setMessageLabel(String messageLabel) {
        this.messageLabel = messageLabel;
    }



    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}