package se.miun.android_app.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Message implements Serializable {

    @SerializedName("messageId")
    private String messageId;

    @SerializedName("messageText")
    private String messageText;

    public Message(){

    }


    public Message(String messageText) {
        this.messageText = messageText;
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
}