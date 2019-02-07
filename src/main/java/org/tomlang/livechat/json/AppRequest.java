package org.tomlang.livechat.json;

public class AppRequest {
    private String name;
    private String timeZone;
   
    
    public String getTimeZone() {
        return timeZone;
    }
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public AppRequest() {
        super();
        // TODO Auto-generated constructor stub
    }
    public AppRequest(String name) {
        super();
        this.name = name;
    }
    @Override
    public String toString() {
        return "AppRequest [name=" + name + "]";
    }
    
    
    

}
