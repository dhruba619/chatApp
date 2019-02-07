package org.tomlang.livechat.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * App Entity class
 * @author dhrubajyotibhattacharjee
 *
 */
@Entity
@Table(name="app")
public class App {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;
    
    
    @Column(name="created_by")
    private Integer createdBy;
    
    @Column(name="app_hashcode")
    private String appHashcode;
    
    @Column(name="app_details_id")
    
    private Integer appDetailsId;

    /**
     * No Argument constructor
     */
    public App() {
        super();
        
    }

    /**
     * Contructor with fields
     * @param id
     * @param createdBy
     * @param appHashcode
     * @param appDetailsId
     */
    public App(Integer id, Integer createdBy, String appHashcode, Integer appDetailsId) {
        super();
        this.id = id;
        this.createdBy = createdBy;
        this.appHashcode = appHashcode;
        this.appDetailsId = appDetailsId;
    }
    
    /*
     * Getter setters...
     */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public String getAppHashcode() {
        return appHashcode;
    }

    public void setAppHashcode(String appHashcode) {
        this.appHashcode = appHashcode;
    }

    public Integer getAppDetailsId() {
        return appDetailsId;
    }

    public void setAppDetailsId(Integer appDetailsId) {
        this.appDetailsId = appDetailsId;
    }

    @Override
    public String toString() {
        return "App [id=" + id + ", createdBy=" + createdBy + ", appHashcode=" + appHashcode + ", appDetailsId=" + appDetailsId + "]";
    }
    
    
    
    
    
}
