package org.tomlang.livechat.json;

import org.tomlang.livechat.enums.SupportedLanguages;

public class UserUpdateProfileRequest {
    private String fullName;
    private SupportedLanguages language;
    private String biography;
    private SocialLinks socialLinks;
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public SupportedLanguages getLanguage() {
        return language;
    }
    public void setLanguage(SupportedLanguages language) {
        this.language = language;
    }
    public String getBiography() {
        return biography;
    }
    public void setBiography(String biography) {
        this.biography = biography;
    }
    public SocialLinks getSocialLinks() {
        return socialLinks;
    }
    public void setSocialLinks(SocialLinks socialLinks) {
        this.socialLinks = socialLinks;
    }
    
    

}
