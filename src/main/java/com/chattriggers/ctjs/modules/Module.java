package com.chattriggers.ctjs.modules;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;

public class Module {
    @Getter
    private String name;
    @Getter
    private String compiledScript;
    @Getter
    private HashMap<String,List<String>> files;
    @Getter
    private ModuleMetadata metadata;

    public Module(String name, String compiledScript, HashMap<String, List<String>> files, ModuleMetadata metadata) {
        this.name = name;
        this.compiledScript = compiledScript;
        this.files = files;
        this.metadata = metadata == null ? new ModuleMetadata() : metadata;
    }

    @Override
    public String toString() {
        return "Module{"
                + this.name
                + "}";
    }
}