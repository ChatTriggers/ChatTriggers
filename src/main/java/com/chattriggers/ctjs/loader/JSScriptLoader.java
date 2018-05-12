package com.chattriggers.ctjs.loader;

import com.chattriggers.ctjs.minecraft.libs.ChatLib;
import com.chattriggers.ctjs.minecraft.libs.FileLib;
import com.chattriggers.ctjs.modules.Module;
import com.chattriggers.ctjs.modules.ModuleMetadata;
import com.chattriggers.ctjs.triggers.TriggerRegister;
import com.chattriggers.ctjs.utils.config.Config;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class JSScriptLoader extends ScriptLoader {
    public static ClassLoader newLoader;

    private ScriptEngine scriptEngine;
    private ArrayList<Module> cachedModules;

    @Override
    public void preLoad() {
        super.preLoad();

        cachedModules = new ArrayList<>();

        try {
            ArrayList<URL> files = new ArrayList<>();

            for (File dir : getFoldersInDirectory(modulesDir)) {
                for (File file : dir.listFiles()) {
                    if (file.getName().endsWith(".jar")) {
                        File jar = new File(
                                Config.getInstance().getModulesFolder().value
                                        + dir.getName()
                                        + "/" + file.getName()
                        );

                        files.add(jar.toURI().toURL());
                    }
                }
            }

            URLClassLoader ucl = new URLClassLoader(files.toArray(new URL[0]), Minecraft.class.getClassLoader());

            NashornScriptEngineFactory nsef = new NashornScriptEngineFactory();
            this.scriptEngine = nsef.getScriptEngine(ucl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            saveResource("/providedLibs.js", new File(modulesDir.getParentFile(), "chattriggers-provided-libs.js"), true);
            scriptEngine.eval(getProvidedLibsScript());
        } catch (ScriptException e) {
            Console.getInstance().printStackTrace(e);
        }
    }

    @Override
    public ArrayList<Module> loadModules(Boolean updateCheck) {
        cachedModules.clear();

        for (File dir : getFoldersInDirectory(modulesDir)) {
            loadModule(dir, updateCheck);
        }

        return cachedModules;
    }

    private boolean isLoaded(File dir) {
        for (Module module : cachedModules) {
            if (module.getName().equals(dir.getName())) return true;
        }
        return false;
    }

    public void loadModule(File dir, boolean updateCheck) {
        if (isLoaded(dir)) return;

        File metadataFile = new File(dir, "metadata.json");
        ModuleMetadata metadata = null;

        if (metadataFile.exists()) {
            try {
                metadata = new Gson().fromJson(new FileReader(metadataFile), ModuleMetadata.class);
                metadata.setFileName(dir.getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            if (metadata != null && updateCheck) {
                try {
                    Console.getInstance().out.println("checking for update in " + metadata.getFileName());

                    File newMetadataFile = new File(modulesDir, "currMetadata.json");
                    FileUtils.copyURLToFile(new URL("http://167.99.3.229/downloads/metadata/" + metadata.getFileName()),
                            newMetadataFile);

                    String currVersion = metadata.getVersion();

                    ModuleMetadata newMetadata = new Gson().fromJson(new FileReader(newMetadataFile), ModuleMetadata.class);
                    String newVersion = newMetadata.getVersion();

                    if (!newVersion.equals(currVersion)) {
                        downloadModule(metadata.getFileName(), false);

                        ChatLib.chat("&6Updated " + metadata.getName());
                    }

                    newMetadataFile.delete();
                } catch (IOException e) {
                    Console.getInstance().out.println("Can't update module " + metadata.getName());
                }
            }

            Module module = new Module(
                    dir.getName(),
                    compileScripts(dir),
                    getAllFiles(dir),
                    metadata
            );

            getRequiredModules(metadata, updateCheck);

            TriggerRegister.currentModule = module;

            getScriptEngine().eval(module.getCompiledScript());

            TriggerRegister.currentModule = null;

            cachedModules.add(module);
        } catch (IOException | ScriptException exception) {
            Console.getInstance().printStackTrace(exception);
        }
    }

    private void getRequiredModules(ModuleMetadata metadata, boolean updateCheck) {
        if (metadata == null || metadata.getRequires() == null) return;

        for (String require : metadata.getRequires()) {
            if (new File(modulesDir, require).exists()) {
                loadModule(new File(modulesDir, require), updateCheck);
                continue;
            }
            ModuleManager.getInstance().importModule(require);
        }
    }

    public boolean downloadModule(String name, boolean existCheck) {
        if (existCheck) {
            File currentMetadata = new File(modulesDir, "currMetadata.json");
            try {
                FileUtils.copyURLToFile(new URL("http://167.99.3.229/downloads/metadata/" + name),
                        currentMetadata);
            } catch (IOException exception) {
                Console.getInstance().printStackTrace(exception);
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
            Console.getInstance().printStackTrace(exception);
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
            Console.getInstance().printStackTrace(e);
            return null;
        }
    }

    /**
     * Compiles all text from multiple files
     * into a singular string for loading.
     *
     * @param dir the directory where the scripts to be compiled are
     * @return the string after compilation
     * @throws IOException thrown if a file doesn't exist
     */
    public String compileScripts(File dir) throws IOException {
        List<File> files = Files.find(
                dir.toPath(),
                5,
                (path, basicFileAttributes) -> path.toString().toLowerCase().endsWith(".js")
        ).map(Path::toFile).collect(Collectors.toList());

        StringBuilder compiledScript = new StringBuilder();

        for (File file : files) {
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


    private HashMap<String, List<String>> getAllFiles(File dir) throws IOException {
        List<File> files = Files.find(
                dir.toPath(),
                5,
                (path, basicFileAttributes) -> path.toString().toLowerCase().endsWith(".js")
        ).map(Path::toFile).collect(Collectors.toList());

        HashMap<String, List<String>> allFiles = new HashMap<>();

        for (File file : files) {
            try {
                allFiles.put(file.getName(), FileUtils.readLines(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return allFiles;
    }
}
