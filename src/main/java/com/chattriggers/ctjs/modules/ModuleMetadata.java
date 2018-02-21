package com.chattriggers.ctjs.modules;

import lombok.Getter;

import java.util.ArrayList;

public class ModuleMetadata {
    @Getter
    private String name = null;
    @Getter
    private String displayName = null;
    @Getter
    private String version = null;
    @Getter
    private ArrayList<String> tags = null;
    @Getter
    private String pictureLink = null;
    @Getter
    private String creator = null;
    @Getter
    private String description = null;
    @Getter
    private ArrayList<String> requires = null;

    ModuleMetadata() {}

    @Override
    public String toString() {
        return "{name=" + name + ",displayName=" + displayName + ",version=" + version
                + ",tags=" + (tags == null ? "null" : tags.toString()) + ",pictureLink=" + pictureLink
                + ",creator=" + creator + ",requires=" + requires + "}";
    }
}
