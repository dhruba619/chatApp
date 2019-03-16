package org.tomlang.livechat.json;

public class ActivateDeactivateUninviteTeamRequest {
    private Integer id;
    private ActivateDeactivateOptions deactiveOptions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ActivateDeactivateOptions getDeactiveOptions() {
        return deactiveOptions;
    }

    public void setDeactiveOptions(ActivateDeactivateOptions deactiveOptions) {
        this.deactiveOptions = deactiveOptions;
    }

}
