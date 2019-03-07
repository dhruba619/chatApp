package org.tomlang.livechat.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.tomlang.livechat.enums.Role;
import org.tomlang.livechat.enums.UserStatus;

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
    
    
    @Column(name="chat_title")
    private String chatTitle;
    
    @Column(name="user_id", nullable=true)
    private Integer userId;
    
    
    @Column(name="app_details_id")
    private Integer appDetailsId;
    
    @Column(name="is_away")
    private boolean isAway;
    
    @Enumerated
    @Column(name="user_status",columnDefinition = "int")
    private UserStatus userStatus;
    
    
    @Enumerated
    @Column(name="role",columnDefinition = "int")
    private Role role;
    
    @Column(name="last_pinged")
    private Long lastPinged;
    
    
    @Column(name="joined")
    @CreationTimestamp
    private Date joined;
    
    @Column(name="invited_details_id")
    private Integer invitedDetailsId;


    public Date getJoined() {
        return joined;
    }


    public void setJoined(Date joined) {
        this.joined = joined;
    }


    public UserAppDetails() {
        super();
    }


    public UserAppDetails(Integer id, String chatLabel, Integer userId, Integer appDetailsId, boolean isAway, Role role, Long lastPinged) {
        super();
        this.id = id;
        this.chatTitle = chatLabel;
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


    public String getChatTitle() {
        return chatTitle;
    }


    public void setChatTitle(String chatLabel) {
        this.chatTitle = chatLabel;
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


    public UserStatus getUserStatus() {
        return userStatus;
    }


    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }


    public Integer getInvitedDetailsId() {
        return invitedDetailsId;
    }


    public void setInvitedDetailsId(Integer invitedDetailsId) {
        this.invitedDetailsId = invitedDetailsId;
    }


    @Override
    public String toString() {
        return "UserAppDetails [id=" + id + ", chatTitle=" + chatTitle + ", userId=" + userId + ", appDetailsId=" + appDetailsId + "]";
    }
    
    
    
    
    
}
