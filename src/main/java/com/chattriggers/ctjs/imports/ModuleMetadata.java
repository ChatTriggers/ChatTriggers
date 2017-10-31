package com.chattriggers.ctjs.imports;

import lombok.Getter;

import java.util.ArrayList;

public class ModuleMetadata {
    @Getter
    private String name;
    @Getter
    private int version;
    @Getter
    private ArrayList<String> tags;
    @Getter
    private String pictureLink;
    @Getter
    private String creator;

    public ModuleMetadata(String name, String creator, int version, ArrayList<String> tags, String pictureLink) {
        this.name = name;
        this.creator = creator;
        this.version = version;
        this.tags = tags;
        this.pictureLink = pictureLink;
    }
}
