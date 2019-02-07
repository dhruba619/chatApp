package org.tomlang.livechat.json;

import org.tomlang.livechat.enums.Role;
import org.tomlang.livechat.enums.UserStatus;

public class UserPrivateInformation {

    private Integer id;

    private String fullName;

    private String image;

    private SocialLinks socialLinks;

    private String lastLoggedIn;

    private NotificationSettings notificationSettings;

    private UserStatus status;
    
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public SocialLinks getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(SocialLinks socialLinks) {
        this.socialLinks = socialLinks;
    }

    public String getLastLoggedIn() {
        return lastLoggedIn;
    }

    public void setLastLoggedIn(String lastLoggedIn) {
        this.lastLoggedIn = lastLoggedIn;
    }

    public NotificationSettings getNotificationSettings() {
        return notificationSettings;
    }

    public void setNotificationSettings(NotificationSettings notificationSettings) {
        this.notificationSettings = notificationSettings;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserPrivateInformation() {
        super();
        // TODO Auto-generated constructor stub
    }

    public UserPrivateInformation(Integer id, String fullName, String image, SocialLinks socialLinks, String lastLoggedIn, NotificationSettings notificationSettings, UserStatus status) {
        super();
        this.id = id;
        this.fullName = fullName;
        this.image = image;
        this.socialLinks = socialLinks;
        this.lastLoggedIn = lastLoggedIn;
        this.notificationSettings = notificationSettings;
        this.status = status;
    }

    

}
