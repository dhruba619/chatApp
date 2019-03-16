package org.tomlang.livechat.json;

import org.tomlang.livechat.enums.TagTarget;

public class AppTagJson {

    private Integer id;
    private String name;
    private String color;
    private TagTarget target;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public TagTarget getTarget() {
        return target;
    }

    public void setTarget(TagTarget target) {
        this.target = target;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
