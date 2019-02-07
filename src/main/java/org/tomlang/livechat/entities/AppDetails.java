package org.tomlang.livechat.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * AppDetails Entity class
 * @author dhrubajyotibhattacharjee
 *
 */
@Entity
@Table(name="app_details")
public class AppDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    
    @Column(name="image")
    private String image;
    
    @Column(name="name")
    private String name;
    
    @Column(name="time_zone")
    private String timeZone;
    
    @Column(name="description")
    private String description;
    
    @Column(name="social_links")
    private String socialLinks;
    
   

    public AppDetails() {
        super();
        
    }
    

    public AppDetails(Integer id, String image, String name, String timeZone, String description, String socialLinks) {
        super();
        this.id = id;
        this.image = image;
        this.name = name;
        this.timeZone = timeZone;
        this.description = description;
        this.socialLinks = socialLinks;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(String socialLinks) {
        this.socialLinks = socialLinks;
    }


    @Override
    public String toString() {
        return "AppDetails [id=" + id + ", image=" + image + ", name=" + name + ", timeZone=" + timeZone + ", description=" + description + ", socialLinks=" + socialLinks + "]";
    }
    
    
    
    
}
