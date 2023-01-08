package com.example.contactapplication.model;

import java.time.LocalDateTime;

public class SendMessageModel {
    private String sentOTP;
    private String contactName;
    private LocalDateTime localDateTime;

    public SendMessageModel(String contactName, String sentOTP, LocalDateTime localDateTime) {
        this.sentOTP = sentOTP;
        this.contactName = contactName;
        this.localDateTime = localDateTime;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getSentOTP() {
        return sentOTP;
    }

    public void setSentOTP(String sentOTP) {
        this.sentOTP = sentOTP;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
