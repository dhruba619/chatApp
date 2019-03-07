package org.tomlang.livechat.enums;

public enum SupportedLanguages {
    ger(0), eng(1);

    private Integer value;

    SupportedLanguages(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

}
