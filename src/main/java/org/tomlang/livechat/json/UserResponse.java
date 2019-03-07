package org.tomlang.livechat.json;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.tomlang.livechat.enums.SupportedLanguages;

public class UserResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    private String email;
    private String fullName;
    private String image;
    private String biography;
    private SocialLinks socialLinks;
    private Date lastLoggedIn;
    private Date joinedDate;
    private NotificationSettings notificationSettings;
    private SupportedLanguages language;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
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
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
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
    public Date getLastLoggedIn() {
        return lastLoggedIn;
    }
    public void setLastLoggedIn(Date lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
    }
    public Date getJoinedDate() {
        return joinedDate;
    }
    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }
    public NotificationSettings getNotificationSettings() {
        return notificationSettings;
    }
    public void setNotificationSettings(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
    }
    public SupportedLanguages getLanguage() {
        return language;
    }
    public void setLanguage(SupportedLanguages language) {
        this.language = language;
    }
    
    
    
}
