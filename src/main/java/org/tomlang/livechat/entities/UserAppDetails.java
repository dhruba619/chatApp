package org.tomlang.livechat.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.tomlang.livechat.enums.Role;

/**
 * UserAppDetails Entity class
 * @author dhrubajyotibhattacharjee
 *
 */
@Entity
@Table(name="user_app_details")
public class UserAppDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    
    
    @Column(name="chat_label")
    private String chatLabel;
    
    @Column(name="user_id")
    private Integer userId;
    
    
    @Column(name="app_details_id")
    private Integer appDetailsId;
    
    @Column(name="is_away")
    private boolean isAway;
    
    
    @Enumerated
    @Column(name="role",columnDefinition = "int")
    private Role role;
    
    @Column(name="last_pinged")
    private Long lastPinged;


    public UserAppDetails() {
        super();
    }


    public UserAppDetails(Integer id, String chatLabel, Integer userId, Integer appDetailsId, boolean isAway, Role role, Long lastPinged) {
        super();
        this.id = id;
        this.chatLabel = chatLabel;
        this.userId = userId;
        this.appDetailsId = appDetailsId;
        this.isAway = isAway;
        this.role = role;
        this.lastPinged = lastPinged;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public String getChatLabel() {
        return chatLabel;
    }


    public void setChatLabel(String chatLabel) {
        this.chatLabel = chatLabel;
    }


    public Integer getUserId() {
        return userId;
    }


    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public Integer getAppDetailsId() {
        return appDetailsId;
    }


    public void setAppDetailsId(Integer appDetailsId) {
        this.appDetailsId = appDetailsId;
    }


    public boolean isAway() {
        return isAway;
    }


    public void setAway(boolean isAway) {
        this.isAway = isAway;
    }


    public Role getRole() {
        return role;
    }


    public void setRole(Role role) {
        this.role = role;
    }


    public Long getLastPinged() {
        return lastPinged;
    }


    public void setLastPinged(Long lastPinged) {
        this.lastPinged = lastPinged;
    }


    @Override
    public String toString() {
        return "UserAppDetails [id=" + id + ", chatLabel=" + chatLabel + ", userId=" + userId + ", appDetailsId=" + appDetailsId + "]";
    }
    
    
    
    
    
}
