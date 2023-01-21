package com.example.contactapplication.model;

import java.time.LocalDateTime;

public class SendMessageModel {
    private String message;
    private String contactName;
    private LocalDateTime localDateTime;

    public SendMessageModel(String contactName, String message, LocalDateTime localDateTime) {
        this.message = message;
        this.contactName = contactName;
        this.localDateTime = localDateTime;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
