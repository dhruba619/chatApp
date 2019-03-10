package org.tomlang.livechat.json;

import java.util.List;

public class AppChannelRequest {

    private String name;
    private String onlineForward;
    private String offlineForward;
    private boolean isDefaultChannel; 
    private String description; 
    private List<String> members; 
    private Integer id;
    
    
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
    public List<String> getMembers() {
        return members;
    }
    public void setMembers(List<String> members) {
        this.members = members;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
}
