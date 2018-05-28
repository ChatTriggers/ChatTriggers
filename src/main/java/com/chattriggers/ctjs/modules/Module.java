package com.chattriggers.ctjs.modules;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

public class Module {
    @Getter
    private String name;
    @Getter
    private HashMap<String,List<String>> files;
    @Getter
    private ModuleMetadata metadata;

    public Module(String name, HashMap<String, List<String>> files, ModuleMetadata metadata) {
        this.name = name;
        this.files = files;
        this.metadata = metadata == null ? new ModuleMetadata() : metadata;
    }

    @Override
    public String toString() {
        return "Module{"
                + this.name + ","
                + this.files.keySet().toString() + ","
                + this.metadata.getVersion()
                + "}";
    }
}