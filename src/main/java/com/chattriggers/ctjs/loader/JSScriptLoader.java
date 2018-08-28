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
    public ArrayList<Module> loadModules(boolean updateCheck, boolean reload) {
        if (reload) {
            cachedModules.clear();

            File toDownload = new File(modulesDir, ".to_download.txt");

            if (toDownload.exists()) {
                String content = FileLib.read(toDownload);

                for (String module : content.split(",")) {
                    if ("\n".equals(module) || "".equals(module)) continue;

                    ModuleManager.getInstance().importModule(module, true);
                }

                toDownload.delete();
            }

            for (File dir : getFoldersInDirectory(modulesDir)) {
                loadModule(dir, updateCheck);
            }
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
        ModuleMetadata metadata = new ModuleMetadata();

        if (metadataFile.exists()) {
            try {
                metadata = new Gson().fromJson(FileLib.read(metadataFile), ModuleMetadata.class);
                metadata.setFileName(dir.getName());
            } catch (Exception exception) {
                Console.getInstance().printStackTrace(exception);
            }
        }

        try {
            if (!metadata.isDefault() && updateCheck) {
                try {
                    Console.getInstance().out.println("checking for update in " + metadata.getFileName());

                    File newMetadataFile = new File(modulesDir, "currMetadata.json");
                    FileUtils.copyURLToFile(new URL("http://chattriggers.com/downloads/metadata/" + metadata.getFileName()),
                            newMetadataFile);

                    String currVersion = metadata.getVersion();

                    try {
                        ModuleMetadata newMetadata = new Gson().fromJson(new FileReader(newMetadataFile), ModuleMetadata.class);
                        String newVersion = newMetadata.getVersion();

                        if (!newVersion.equals(currVersion)) {
                            downloadModule(metadata.getFileName(), false);

                            ChatLib.chat("&6Updated " + metadata.getName());
                        }
                    } catch (Exception exception) {
                        Console.getInstance().printStackTrace(exception);
                    }

                    newMetadataFile.delete();
                } catch (IOException e) {
                    Console.getInstance().out.println("Can't update module " + metadata.getName());
                }
            }

            String compiledScript = compileScripts(
                    dir,
                    metadata.getIgnored()
            );

            Module module = new Module(
                    dir.getName(),
                    getAllFiles(dir, metadata.getIgnored()),
                    metadata
            );

            getRequiredModules(metadata, updateCheck);

            TriggerRegister.currentModule = module;

            getScriptEngine().eval(compiledScript);

            TriggerRegister.currentModule = null;

            cachedModules.add(module);
        } catch (IOException | ScriptException exception) {
            Console.getInstance().out.println("Error loading module from " + dir);
            Console.getInstance().printStackTrace(exception);
        }
    }

    private void getRequiredModules(ModuleMetadata metadata, boolean updateCheck) {
        if (metadata.isDefault() || metadata.getRequires() == null) return;

        for (String require : metadata.getRequires()) {
            if (new File(modulesDir, require).exists()) {
                loadModule(new File(modulesDir, require), updateCheck);
                continue;
            }
            ModuleManager.getInstance().importModule(require, true);
        }
    }

    public boolean downloadModule(String name, boolean existCheck) {
        if (existCheck) {
            File currentMetadata = new File(modulesDir, "currMetadata.json");
            try {
                FileLib.deleteDirectory(new File(modulesDir, name));
                FileUtils.copyURLToFile(new URL("http://chattriggers.com/downloads/metadata/" + name),
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
                    new URL("http://chattriggers.com/downloads/scripts/" + name),
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
            return compileScripts(new File(this.modulesDir.getParentFile(), "chattriggers-provided-libs.js"), null);
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
    public String compileScripts(File dir, ArrayList<String> ignored) throws IOException {
        List<File> files = getScriptFiles(dir, ignored);

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

    private List<File> getScriptFiles(File dir, ArrayList<String> ignored) throws IOException {
        return Files.find(
                dir.toPath(),
                5,
                (path, basicFileAttributes) -> path.toString().toLowerCase().endsWith(".js")
        ).map(Path::toFile).filter(file -> {
            if (ignored == null) return true;

            for (String ignore : ignored) {
                if (file.getPath().contains(ignore)) {
                    return false;
                }
            }

            return true;
        }).collect(Collectors.toList());
    }


    private HashMap<String, List<String>> getAllFiles(File dir, ArrayList<String> ignored) throws IOException {
        HashMap<String, List<String>> allFiles = new HashMap<>();

        for (File file : getScriptFiles(dir, ignored)) {
            try {
                //#if MC<=10809
                allFiles.put(file.getName(), FileUtils.readLines(file));
                //#else
                //$$ allFiles.put(file.getName(), FileUtils.readLines(file, java.nio.charset.Charset.defaultCharset()));
                //#endif
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return allFiles;
    }
}
