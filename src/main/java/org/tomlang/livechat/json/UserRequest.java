package org.tomlang.livechat.json;

import org.tomlang.livechat.enums.SupportedLanguages;

public class UserRequest {

    private String email;
    private String full_name;
    private String password;
    private SupportedLanguages language;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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

    public UserRequest(String email, String full_name, String password, SupportedLanguages language) {
        super();
        this.email = email;
        this.full_name = full_name;
        this.password = password;
        this.language = language;

    }

    public UserRequest() {
        super();
    }

    @Override
    public String toString() {
        return "UserRequest [email=" + email + ", full_name=" + full_name + "]";

    }
}
