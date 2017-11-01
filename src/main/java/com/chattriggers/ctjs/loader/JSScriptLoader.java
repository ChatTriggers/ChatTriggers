package com.chattriggers.ctjs.loader;

import com.chattriggers.ctjs.imports.Module;
import com.chattriggers.ctjs.imports.ModuleMetadata;
import com.chattriggers.ctjs.utils.console.Console;
import com.google.gson.Gson;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.FileUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class JSScriptLoader extends ScriptLoader {
    private ScriptEngine scriptEngine;
    private ArrayList<Module> cachedModules;

    @Override
    public void preLoad() {
        super.preLoad();

        this.scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

        try {
            saveResource("/providedLibs.js", new File(modulesDir, "chattriggers-provided-libs.js"), true);
            scriptEngine.eval(getProvidedLibsScript());
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Module> loadModules() {
        if (cachedModules != null && !cachedModules.isEmpty()) {
            return cachedModules;
        }

        ArrayList<Module> modules = new ArrayList<>();

        for (File dir : getFoldersInDirectory(modulesDir)) {
            File metadataFile = new File(dir, "metadata.json");
            ModuleMetadata metadata = null;

            if (metadataFile.exists()) {
                try {
                    metadata = new Gson().fromJson(new FileReader(metadataFile), ModuleMetadata.class);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            try {
                Module module = new Module(
                    dir.getName(),
                    compileScripts(dir.listFiles()),
                    getAllLines(dir.listFiles()),
                    metadata
                );

                modules.add(module);
                getScriptEngine().eval(module.getCompiledScript());
            } catch (IOException | ScriptException e) {
                e.printStackTrace();
            }
        }

        cachedModules = modules;
        return modules;
    }

    @Override
    public void postLoad() {
        super.postLoad();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ArrayList<String> getIllegalLines() {
        return new ArrayList<>(Arrays.asList(
                "module.export", "load(\"http"
        ));
    }

    @Override
    protected ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    @Override
    protected Invocable getInvocableEngine() {
        return (Invocable) scriptEngine;
    }

    /**
     * Gets the script that provides imports basic libraries.
     * @return a string of the compiled script
     */
    public String getProvidedLibsScript() {
        try {
            return compileScripts(new File(this.modulesDir, "chattriggers-provided-libs.js"));
        } catch (IOException e) {
            Console.getConsole().printStackTrace(e);
            return null;
        }
    }

    /**
     * Compiles all text from multiple files
     * into a singular string for loading.
     * @param files a list of files to be compiled
     * @return the string after compilation
     * @throws IOException thrown if a file doesn't exist
     */
    public String compileScripts(File... files) throws IOException {
        StringBuilder compiledScript = new StringBuilder();

        for (File file : files) {
            if (!file.isFile() || !file.exists() || !file.getName().endsWith(".js")) continue;

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            parseScript:
            while ((line = br.readLine()) != null) {
                for (String illegalLine : getIllegalLines()) {
                    if (line.contains(illegalLine)) continue parseScript;
                }

                compiledScript.append(line).append("\n");
            }
        }

        return compiledScript.toString();
    }



    private ArrayList<String> getAllLines(File... files) {
        ArrayList<String> stringList = new ArrayList<>();

        for (File file : files) {
            if (!file.getName().endsWith(".js")) continue;

            try {
                stringList.addAll(FileUtils.readLines(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return stringList;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        try {
            getInvocableEngine().invokeFunction("updateProvidedLibsTick");
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onWorldLoad(WorldEvent.Load event) {
        try {
            getInvocableEngine().invokeFunction("updateProvidedLibsWorld");
        } catch (ScriptException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
