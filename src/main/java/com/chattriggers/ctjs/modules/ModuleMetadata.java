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

    public ModuleMetadata(String name, String creator, String description, String version, ArrayList<String> tags, String pictureLink, String displayName) {
        this.name = name;
        this.creator = creator;
        this.description = description;
        this.version = version;
        this.tags = tags;
        this.pictureLink = pictureLink;
    }

    public ModuleMetadata() {

    }
}
