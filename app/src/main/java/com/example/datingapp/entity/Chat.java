package com.example.datingapp.entity;

import com.example.datingapp.enumurator.TypeMessage;

import java.io.Serializable;
import java.util.Date;

public class Chat implements Serializable {
    private String message;
    private String sender;
    private String receiver;
    private TypeMessage type;
    private String createAt;
    private String url;

    public Chat() {
    }

    public Chat( String message, String sender, String receiver, TypeMessage type, String createAt, String url) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.createAt = createAt;
        this.url = url;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public TypeMessage getType() {
        return type;
    }

    public void setType(TypeMessage type) {
        this.type = type;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
