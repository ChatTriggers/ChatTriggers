package com.chattriggers.ctjs.imports;

import lombok.Getter;

import java.util.ArrayList;

public class Module {
    @Getter
    private String name;
    @Getter
    private String compiledScript;
    @Getter
    private ArrayList<String> lines;
    @Getter
    private ModuleMetadata metadata;

    public Module(String name, String compiledScript, ArrayList<String> lines, ModuleMetadata metadata) {
        this.name = name;
        this.compiledScript = compiledScript;
        this.lines = lines;
        this.metadata = metadata == null ? new ModuleMetadata() : metadata;
    }
}