package org.tomlang.livechat.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="app_design")
public class AppDesign {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    
    @Column(name="app_id")
    private Integer appId;
    
    
    @Column(name="theme_color",nullable=false)
    @Pattern(regexp="^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    private String themeColor;
    
    @Column(name="wallpaper")
    private Integer wallpaper;
    
    @Column(name="custom_css")
    private String customCss;
    
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

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(String themeColor) {
        this.themeColor = themeColor;
    }

    public Integer getWallpaper() {
        return wallpaper;
    }

    public void setWallpaper(Integer wallpaper) {
        this.wallpaper = wallpaper;
    }

    public String getCustomCss() {
        return customCss;
    }

    public void setCustomCss(String customCss) {
        this.customCss = customCss;
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
    
    
    
}
