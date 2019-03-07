package org.tomlang.livechat.json;

public class NotificationSettings {

    private boolean desktopAssigned;
    private boolean desktopSend;
    private boolean soundAssigned;
    private boolean soundSend;
    public boolean isDesktopAssigned() {
        return desktopAssigned;
    }
    public void setDesktopAssigned(boolean desktopAssigned) {
        this.desktopAssigned = desktopAssigned;
    }
    public boolean isDesktopSend() {
        return desktopSend;
    }
    public void setDesktopSend(boolean desktopSend) {
        this.desktopSend = desktopSend;
    }
    public boolean isSoundAssigned() {
        return soundAssigned;
    }
    public void setSoundAssigned(boolean soundAssigned) {
        this.soundAssigned = soundAssigned;
    }
    public boolean isSoundSend() {
        return soundSend;
    }
    public void setSoundSend(boolean soundSend) {
        this.soundSend = soundSend;
    }

    
    
    
}
