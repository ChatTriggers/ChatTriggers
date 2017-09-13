package com.chattriggers.jsct.imports;

import lombok.Getter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        this.script = String.join("", scripts).replace("function init", "function init" + name);
    }

    /**
     * Helper method to compile all text multiple files
     * into a singular string for loading.
     * @param files a list of files to be compiled
     * @return the string after compilation
     * @throws IOException thrown if a file doesn't exist
     */
    public static String compileScripts(File... files) throws IOException {
        StringBuilder compiledScript = new StringBuilder();

        for (File file : files) {
            if (!file.isFile() || !file.exists()) continue;

            compiledScript.append(new String(Files.readAllBytes(Paths.get(file.toURI()))));
        }

        return compiledScript.toString();
    }

    /**
     * Helper method to get all the folders in a directory,
     * used to get import folders.
     * @param directory directory to search through
     * @return a list of files in directory, or null if not a directory
     */
    public static List<File> getFoldersInDirectory(File directory) {
        if (!directory.isDirectory()) return null;

        List<File> filesToReturn = new ArrayList<>();
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) filesToReturn.add(file);
        }

        return filesToReturn;
    }

    /**
     * Helper method which gets all the imports
     * compiled into strings.
     * @return a list of imports, all compiled
     */
    public static List<Import> getCompiledImports() {
        List<Import> compiledImports = new ArrayList<>();

        File importsDir = new File("./mods/ChatTriggers/Imports/");

        for (File importDir : getFoldersInDirectory(importsDir)) {
            try {
                Import newImport = new Import(importDir.getName(), compileScripts(importDir.listFiles()));
                compiledImports.add(newImport);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return compiledImports;
    }
}