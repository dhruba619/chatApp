package org.tomlang.livechat.json;

import com.google.gson.Gson;

public class SocialLinks {
    private String facebook;
    private String twitter;
    private String linkedin;
    private String instagram;
    public String getFacebook() {
        return facebook;
    }
    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }
    public String getTwitter() {
        return twitter;
    }
    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }
    public String getLinkedin() {
        return linkedin;
    }
    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }
    public String getInstagram() {
        return instagram;
    }
    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }
    public SocialLinks(String facebook, String twitter, String linkedin, String instagram) {
        super();
        this.facebook = facebook;
        this.twitter = twitter;
        this.linkedin = linkedin;
        this.instagram = instagram;
    }
    public SocialLinks() {
        super();
        // TODO Auto-generated constructor stub
    }
    @Override
    public String toString() {
        
        Gson gson = new Gson();
        return gson.toJson(this);
       // return "SocialLinks [facebook=" + facebook + ", twitter=" + twitter + ", linkedin=" + linkedin + ", instagram=" + instagram + "]";
    }
    
     

}