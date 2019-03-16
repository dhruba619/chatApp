package org.tomlang.livechat.enums;

public enum TagTarget {
    CONATCT(0), CONVERSATION(1), BOTH(2);

    private Integer value;

    TagTarget(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
