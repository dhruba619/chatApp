package org.tomlang.livechat.exceptions;

import org.springframework.http.HttpStatus;

public class LiveChatException extends Exception {
    
    private String errorDesciption;
    private HttpStatus httpCode;
    
    public LiveChatException(String errorDesciption, HttpStatus httpCode) {
        super();
        this.errorDesciption = errorDesciption;
        this.httpCode = httpCode;
    }

    public String getErrorDesciption() {
        return errorDesciption;
    }

    public void setErrorDesciption(String errorDesciption) {
        this.errorDesciption = errorDesciption;
    }

    public HttpStatus getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(HttpStatus httpCode) {
        this.httpCode = httpCode;
    }
    
    
    

}
