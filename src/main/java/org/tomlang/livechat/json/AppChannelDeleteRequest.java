package org.tomlang.livechat.json;

public class AppChannelDeleteRequest {

    private Integer id; 
    
    private DeleteOptions deleteOptions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DeleteOptions getDeleteOptions() {
        return deleteOptions;
    }

    public void setDeleteOptions(DeleteOptions deleteOptions) {
        this.deleteOptions = deleteOptions;
    }
    
    
}
