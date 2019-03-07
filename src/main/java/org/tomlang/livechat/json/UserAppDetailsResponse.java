package org.tomlang.livechat.json;

public class UserAppDetailsResponse {
    private String name;
    private int id;
    private String image;
    private String description;
    private int ownedBy;
    private SocialLinks socialLinks;
    private String timeZone;
    private String hash;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getOwnedBy() {
        return ownedBy;
    }
    public void setOwnedBy(int ownedBy) {
        this.ownedBy = ownedBy;
    }
    public SocialLinks getSocialLinks() {
        return socialLinks;
    }
    public void setSocialLinks(SocialLinks socialLinks) {
        this.socialLinks = socialLinks;
    }
    public String getTimeZone() {
        return timeZone;
    }
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
    public String getHash() {
        return hash;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    
    
    
}
