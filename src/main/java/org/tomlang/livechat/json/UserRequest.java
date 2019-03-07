package org.tomlang.livechat.json;

import org.tomlang.livechat.enums.SupportedLanguages;

public class UserRequest {

    private String email;
    private String fullName;
    private String password;
    private SupportedLanguages language;
    private String appName;
    

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SupportedLanguages getLanguage() {
        return language;
    }

    public void setLanguage(SupportedLanguages language) {
        this.language = language;
    }

    public UserRequest(String email, String fullName, String password, SupportedLanguages language, String appName) {
        super();
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.language = language;
        this.appName = appName;

    }

    public UserRequest() {
        super();
    }

    @Override
    public String toString() {
        return "UserRequest [email=" + email + ", fullName=" + fullName + "]";

    }
}
