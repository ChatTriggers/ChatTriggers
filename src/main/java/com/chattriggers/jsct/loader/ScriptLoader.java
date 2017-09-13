package com.chattriggers.jsct.loader;

import com.chattriggers.jsct.JSCT;
import com.chattriggers.jsct.imports.Import;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.script.ScriptException;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Copyright (c) FalseHonesty 2017
 */
public class ScriptLoader {
    private List<Import> loadedImports;
    private NashornScriptEngine scriptEngine;

    public ScriptLoader() {
        this.scriptEngine = JSCT.getInstance().getScriptEngine();
        loadImports();

        try {
            scriptEngine.eval(getProvidedLibsScript());
            scriptEngine.eval(getCustomLibsScript());

            for (Import customImport : this.loadedImports) {
                scriptEngine.eval(customImport.getScript());
                scriptEngine.invokeFunction("init" + customImport.getName());
            }
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void loadImports() {
        this.loadedImports = getCompiledImports();
    }

    @SubscribeEvent
    public void onClientTick() {
        try {
            scriptEngine.invokeFunction("updateProvidedLibs");
            scriptEngine.invokeFunction("updateCustomLibs");
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method to get the script that provides imports
     * basic libraries.
     * @return a string of the compiled script
     */
    public String getProvidedLibsScript() {
        try {
            return compileScripts(new File("./mods/ChatTriggers/libs/providedLibs.js"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A method to get the script that provides imports
     * custom libraries.
     * @return a string of the compiled script
     */
    public String getCustomLibsScript() {
        try {
            return compileScripts(new File("./mods/ChatTriggers/libs/customLibs.js"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Helper method to compile all text multiple files
     * into a singular string for loading.
     * @param files a list of files to be compiled
     * @return the string after compilation
     * @throws IOException thrown if a file doesn't exist
     */
    public String compileScripts(File... files) throws IOException {
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
    public List<File> getFoldersInDirectory(File directory) {
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
    public List<Import> getCompiledImports() {
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
