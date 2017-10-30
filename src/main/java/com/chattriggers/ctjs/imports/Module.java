package com.chattriggers.ctjs.imports;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Module {
    @Getter
    private String name;
    @Getter
    private String compiledScript;
    @Getter
    private ArrayList<String> lines;

    @Getter
    private ModuleMetadata metadata = null;


    public Module(String name, String compiledScript, ArrayList<String> lines) {
        this.name = name;
        this.compiledScript = compiledScript;
        this.lines = lines;
    }

    public Module(String name, String compiledScript, ArrayList<String> lines, ModuleMetadata metadata) {
        this.name = name;
        this.compiledScript = compiledScript;
        this.lines = lines;
        this.metadata = metadata;
    }
}

class ModuleMetadata {
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