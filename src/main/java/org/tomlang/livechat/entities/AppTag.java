package org.tomlang.livechat.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.tomlang.livechat.enums.TagTarget;

@Entity
@Table(name="app_tag")
public class AppTag {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    
    @Column(name="app_id")
    private Integer appId;
    
    @Column(name="name")
    private String name;
    
    @Column(name="color",nullable=false)
    @Pattern(regexp="^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")
    private String color;
    
    @Enumerated
    @Column(name="target",columnDefinition = "int", nullable=false)
    private TagTarget target;
    
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TagTarget getTarget() {
        return target;
    }

    public void setTarget(TagTarget target) {
        this.target = target;
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
