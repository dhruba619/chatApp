package org.tomlang.livechat.json;

public class CreateTeamRequest {
    
    private String name;
    private String email;
    private String chatTitle;
    private Integer role;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getChatTitle() {
        return chatTitle;
    }
    public void setChatTitle(String chatTitle) {
        this.chatTitle = chatTitle;
    }
    public Integer getRole() {
        return role;
    }
    public void setRole(Integer role) {
        this.role = role;
    }
    
    

}
