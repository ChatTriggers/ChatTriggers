package com.chattriggers.jsct.imports;

import lombok.Getter;

/**
 * Copyright (c) FalseHonesty 2017
 */
public class Import {
    @Getter
    private String name;
    @Getter
    private String script;

    public Import(String name, String... scripts) {
        this.name = name;
        this.script = String.join("", scripts);
        this.script = this.script.replace("function init", "function init" + name);
    }


}