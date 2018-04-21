package com.chattriggers.ctjs.loader;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.libs.FileLib;
import com.chattriggers.ctjs.modules.Module;
import com.chattriggers.ctjs.modules.ModuleMetadata;
import com.chattriggers.ctjs.triggers.TriggerRegister;
import com.chattriggers.ctjs.utils.console.Console;
import com.google.gson.Gson;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.io.FileUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JSScriptLoader extends ScriptLoader {
    private ScriptEngine scriptEngine;
    private ArrayList<Module> cachedModules;

    @Override
    public void preLoad() {
        super.preLoad();

        try {
            ArrayList<URL> files = new ArrayList<>();

            for (File dir : getFoldersInDirectory(modulesDir)) {
                for (File file : dir.listFiles()) {
                    if (file.getName().endsWith(".jar")) {
                        File jar = new File(
                                CTJS.getInstance().getConfig().getModulesFolder()
                                        + dir.getName()
                                        + "/" + file.getName()
                        );

                        files.add(jar.toURI().toURL());
                    }
                }
            }

            URLClassLoader ucl = new URLClassLoader((URL[]) files.toArray(), Minecraft.class.getClassLoader());

            NashornScriptEngineFactory nsef = new NashornScriptEngineFactory();
            this.scriptEngine = nsef.getScriptEngine(ucl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            saveResource("/providedLibs.js", new File(modulesDir.getParentFile(), "chattriggers-provided-libs.js"), true);
            scriptEngine.eval(getProvidedLibsScript());
        } catch (ScriptException e) {
            Console.getConsole().printStackTrace(e);
        }
    }

    @Override
    public ArrayList<Module> loadModules(Boolean updateCheck) {
        if (cachedModules != null && !cachedModules.isEmpty()) {
            return cachedModules;
        }

        ArrayList<Module> modules = new ArrayList<>();

        for (File dir : getFoldersInDirectory(modulesDir)) {
            Module mod = loadModule(dir, updateCheck);

            if (mod == null) continue;

            modules.add(mod);
        }

        cachedModules = modules;
        return modules;
    }

    public Module loadModule(File dir, boolean updateCheck) {
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
            if (metadata != null && updateCheck) {
                try {
                    File newMetadataFile = new File(modulesDir, "currMetadata.json");
                    FileUtils.copyURLToFile(new URL("https://chattriggers.com/downloads/metadata/" + metadata.getName()),
                            newMetadataFile);

                    float currVersion = Float.parseFloat(metadata.getVersion());

                    ModuleMetadata newMetadata = new Gson().fromJson(new FileReader(newMetadataFile), ModuleMetadata.class);
                    float newVersion = Float.parseFloat(newMetadata.getVersion());

                    if (newVersion > currVersion) {
                        downloadModule(metadata.getName(), false);

                        ChatLib.chat("&6Updated " + metadata.getName());
                    }

                    newMetadataFile.delete();
                } catch (IOException e) {
                    Console.getConsole().out.println("Can't update module " + metadata.getName());
                }
            }

            Module module = new Module(
                    dir.getName(),
                    compileScripts(dir.listFiles()),
                    getAllFiles(dir.listFiles()),
                    metadata
            );

            getRequiredModules(metadata);

            TriggerRegister.currentModule = module;

            getScriptEngine().eval(module.getCompiledScript());

            TriggerRegister.currentModule = null;
            return module;
        } catch (IOException | ScriptException e) {
            Console.getConsole().printStackTrace(e);
        }

        return null;
    }

    private void getRequiredModules(ModuleMetadata metadata) {
        if (metadata == null || metadata.getRequires() == null) return;

        for (String require : metadata.getRequires()) {
            if (new File(modulesDir, require).exists()) continue;
            CTJS.getInstance().getModuleManager().importModule(require);
        }
    }

    public boolean downloadModule(String name, boolean existCheck) {
        if (existCheck) {
            File currentMetadata = new File(modulesDir, "currMetadata.json");
            try {
                FileUtils.copyURLToFile(new URL("http://167.99.3.229/downloads/metadata/" + name),
                        currentMetadata);
            } catch (IOException exception) {
                Console.getConsole().printStackTrace(exception);
                ChatLib.chat("&cModule not found!");
                currentMetadata.delete();
                return false;
            }

            currentMetadata.delete();
        }

        try {
            File downloadZip = new File(modulesDir, "currDownload.zip");

            FileUtils.copyURLToFile(
                    new URL("http://167.99.3.229/downloads/scripts/" + name),
                    downloadZip
            );

            FileLib.unzip(downloadZip.getAbsolutePath(), modulesDir.getAbsolutePath());

            downloadZip.delete();
        } catch (IOException exception) {
            Console.getConsole().printStackTrace(exception);
            return false;
        }

        return true;
    }

    @Override
    public void postLoad() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ArrayList<String> getIllegalLines() {
        return new ArrayList<>(Arrays.asList(
                "module.export", " load(\"http"
        ));
    }

    @Override
    public ScriptEngine getScriptEngine() {
        return scriptEngine;
    }

    @Override
    protected Invocable getInvocableEngine() {
        return (Invocable) scriptEngine;
    }

    /**
     * Gets the script that provides modules basic libraries.
     *
     * @return a string of the compiled script
     */
    public String getProvidedLibsScript() {
        try {
            return compileScripts(new File(this.modulesDir.getParentFile(), "chattriggers-provided-libs.js"));
        } catch (IOException e) {
            Console.getConsole().printStackTrace(e);
            return null;
        }
    }

    /**
     * Compiles all text from multiple files
     * into a singular string for loading.
     *
     * @param files a list of files to be compiled
     * @return the string after compilation
     * @throws IOException thrown if a file doesn't exist
     */
    public String compileScripts(File... files) throws IOException {
        StringBuilder compiledScript = new StringBuilder();

        for (File file : files) {
            if (!file.isFile() || !file.exists() || !file.getName().endsWith(".js")) continue;

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));
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


    private HashMap<String, List<String>> getAllFiles(File... files) {
        HashMap<String, List<String>> allFiles = new HashMap<>();

        for (File file : files) {
            if (!file.getName().endsWith(".js")) continue;

            try {
                allFiles.put(file.getName(), FileUtils.readLines(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return allFiles;
    }
}
