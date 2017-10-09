package com.chattriggers.ctjs.imports;

import lombok.Getter;

import java.util.ArrayList;

public class Import {
    @Getter
    private String name;
    @Getter
    private String compiledScript;
    @Getter
    private ArrayList<String> lines;

    public Import(String name, String compiledScript, ArrayList<String> lines) {
        this.name = name;
        this.compiledScript = compiledScript;
        this.lines = lines;
    }


}