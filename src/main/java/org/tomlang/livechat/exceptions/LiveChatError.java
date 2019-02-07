package org.tomlang.livechat.exceptions;

import java.util.Date;

public class LiveChatError {
    private String message;
    private Date date;
    public LiveChatError() {
        super();
        // TODO Auto-generated constructor stub
    }
    public LiveChatError(String message, Date date) {
        super();
        this.message = message;
        this.date = date;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    
    
}
