package org.tomlang.livechat.json;

import com.google.gson.Gson;

public class NotificationSettings {

    private boolean desktop_assigned;
    private boolean desktop_send;
    private boolean sound_assigned;
    private boolean sound_send;
    public boolean isDesktop_assigned() {
        return desktop_assigned;
    }
    public void setDesktop_assigned(boolean desktop_assigned) {
        this.desktop_assigned = desktop_assigned;
    }
    public boolean isDesktop_send() {
        return desktop_send;
    }
    public void setDesktop_send(boolean desktop_send) {
        this.desktop_send = desktop_send;
    }
    public boolean isSound_assigned() {
        return sound_assigned;
    }
    public void setSound_assigned(boolean sound_assigned) {
        this.sound_assigned = sound_assigned;
    }
    public boolean isSound_send() {
        return sound_send;
    }
    public void setSound_send(boolean sound_send) {
        this.sound_send = sound_send;
    }
    public NotificationSettings(boolean desktop_assigned, boolean desktop_send, boolean sound_assigned, boolean sound_send) {
        super();
        this.desktop_assigned = desktop_assigned;
        this.desktop_send = desktop_send;
        this.sound_assigned = sound_assigned;
        this.sound_send = sound_send;
    }
    public NotificationSettings() {
        super();
        // TODO Auto-generated constructor stub
    }
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
        //return "NotificationSettings [desktop_assigned=" + desktop_assigned + ", desktop_send=" + desktop_send + ", sound_assigned=" + sound_assigned + ", sound_send=" + sound_send + "]";
    }
    
    
}
