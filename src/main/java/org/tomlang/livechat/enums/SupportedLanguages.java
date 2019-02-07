package org.tomlang.livechat.enums;

public enum SupportedLanguages {
    GERMAN(0), ENGLISH(1);

    private Integer value;

    SupportedLanguages(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

}
