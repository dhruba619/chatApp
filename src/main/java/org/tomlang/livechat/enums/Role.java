package org.tomlang.livechat.enums;

public enum Role {

    AGENT(0), ADMIN(1), OWNER(2);

    private Integer value;

    Role(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

}
