package com.chattriggers.ctjs.imports;

import lombok.Getter;

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