package org.tomlang.livechat.json;

public class AppResponse {
    private String name;
    private Integer id;
    private String image;
    private String description;
    private Integer ownedBy;
    private SocialLinks socialLinks;
    private String timeZone;
    private String hash;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getOwnedBy() {
        return ownedBy;
    }
    public void setOwnedBy(Integer ownedBy) {
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
