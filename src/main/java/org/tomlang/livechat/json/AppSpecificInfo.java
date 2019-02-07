package org.tomlang.livechat.json;

public class AppSpecificInfo {

    private String image;
    private String timeZone;
    private String description;
    private String name;
    private String socialLinks;
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSocialLinks() {
        return socialLinks;
    }
    public void setSocialLinks(String socialLinks) {
        this.socialLinks = socialLinks;
    }
    public AppSpecificInfo() {
        super();
        // TODO Auto-generated constructor stub
    }
    public AppSpecificInfo(String image, String timeZone, String description, String name, String socialLinks) {
        super();
        this.image = image;
        this.timeZone = timeZone;
        this.description = description;
        this.name = name;
        this.socialLinks = socialLinks;
    }

    
}
