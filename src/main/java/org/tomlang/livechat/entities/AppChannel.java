package org.tomlang.livechat.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="app_channel")
public class AppChannel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    
    @Column(name="app_id")
    private Integer appId;

    @Column(name="name")
    private String name;
    
    @Column(name="online_forward")
    private String onlineForward;
    
    @Column(name="offline_forward")
    private String offlineForward;
    
    @Column(name="is_default_channel")
    private boolean isDefaultChannel;
    
    @Column(name="description")
    private String description;
    
    @Column(name="members")
    private String members;
    
    @Column(name="created_at")
    @CreationTimestamp
    private Date createdAt;
    
    @Column(name="modified_at")
    @UpdateTimestamp
    private Date modifiedAt;
    
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnlineForward() {
        return onlineForward;
    }

    public void setOnlineForward(String onlineForward) {
        this.onlineForward = onlineForward;
    }

    public String getOfflineForward() {
        return offlineForward;
    }

    public void setOfflineForward(String offlineForward) {
        this.offlineForward = offlineForward;
    }

    public boolean isDefaultChannel() {
        return isDefaultChannel;
    }

    public void setDefaultChannel(boolean isDefaultChannel) {
        this.isDefaultChannel = isDefaultChannel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }
    
    
}
