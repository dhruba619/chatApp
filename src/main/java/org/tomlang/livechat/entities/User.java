package org.tomlang.livechat.entities;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.tomlang.livechat.enums.SupportedLanguages;

/**
 * User Entity class
 * @author dhrubajyotibhattacharjee
 *
 */
@Entity
@Table(name="user")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    
    @Column(name="email", unique=true)
    private String email;
    
    @Column(name="full_name")
    private String fullName;
    
    @Column(name="password")
    private String password;
    
    @Column(name="password_salt")
    private String passwordSalt;
    
    @Column(name="image")
    private String image;
    
    @Column(name="biography")
    private String biography;
    
    @Column(name="social_links")
    private String socialLinks;
    
    @Column(name="last_logged_in")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoggedIn;
    
    @Column(name="join_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date joinedDate;
    
    @Column(name="confirm_email_hash")
    private String  confirmEmailHash;
    
    @Column(name="has_confirmed_email")
    private boolean hasConfirmedEmail;
    
    @Column(name="notification_settings")
    private String notificationSettings;
    
    @Enumerated
    @Column(name="language",columnDefinition = "int")
    private SupportedLanguages language;
    
    

    /**
     * No Argument constructor
     */
    public User() {
        super();
    }

    /**
     * Contructor with fields
     * @param id
     * @param email
     * @param fullName
     * @param password
     * @param passwordSalt
     * @param image
     * @param biography
     * @param socialLinks
     * @param lastLoggedIn
     * @param joinedDate
     * @param confirmEmailHash
     * @param hasConfirmedEmail
     * @param notificationSettings
     */
    public User(Integer id, String email, String fullName, String password, String passwordSalt, String image, String biography, String socialLinks, Date lastLoggedIn, Date joinedDate, String confirmEmailHash, boolean hasConfirmedEmail,
        String notificationSettings, SupportedLanguages language) {
        super();
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.passwordSalt = passwordSalt;
        this.image = image;
        this.biography = biography;
        this.socialLinks = socialLinks;
        this.lastLoggedIn = lastLoggedIn;
        this.joinedDate = joinedDate;
        this.confirmEmailHash = confirmEmailHash;
        this.hasConfirmedEmail = hasConfirmedEmail;
        this.notificationSettings = notificationSettings;
        this.language=language;
    }

    /*
     * Getter and Setters Below
     * 
     */
    
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
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

    public String getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(String socialLinks) {
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

    public String getConfirmEmailHash() {
        return confirmEmailHash;
    }

    public void setConfirmEmailHash(String confirmEmailHash) {
        this.confirmEmailHash = confirmEmailHash;
    }

    public boolean isHasConfirmedEmail() {
        return hasConfirmedEmail;
    }

    public void setHasConfirmedEmail(boolean hasConfirmedEmail) {
        this.hasConfirmedEmail = hasConfirmedEmail;
    }

    public String getNotificationSettings() {
        return notificationSettings;
    }

    public void setNotificationSettings(String notificationSettings) {
        this.notificationSettings = notificationSettings;
    }

    public SupportedLanguages getLanguage() {
        return language;
    }

    public void setLanguage(SupportedLanguages language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", email=" + email + ", fullName=" + fullName + ", password=" + password + ", passwordSalt=" + passwordSalt + ", image=" + image + ", biography=" + biography + ", socialLinks=" + socialLinks + ", lastLoggedIn=" + lastLoggedIn
            + ", joinedDate=" + joinedDate + ", confirmEmailHash=" + confirmEmailHash + ", hasConfirmedEmail=" + hasConfirmedEmail + ", notificationSettings=" + notificationSettings + ", language=" + language + "]";
    }


    
       

}
