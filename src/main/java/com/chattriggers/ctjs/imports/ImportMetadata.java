package com.chattriggers.ctjs.imports;

import lombok.Getter;

import java.util.ArrayList;

public class ImportMetadata {
    @Getter
    private String name;
    @Getter
    private String creator;
    @Getter
    private int version;
    @Getter
    private ArrayList<String> tags;
    @Getter
    private String pictureLink;

    public ImportMetadata(String name, String creator, int version, ArrayList<String> tags, String pictureLink) {
        this.name = name;
        this.creator = creator;
        this.version = version;
        this.tags = tags;
        this.pictureLink = pictureLink;
    }
}
