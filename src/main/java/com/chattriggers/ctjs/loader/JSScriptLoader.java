package com.chattriggers.ctjs.loader;

import com.chattriggers.ctjs.libs.ChatLib;
import com.chattriggers.ctjs.modules.Module;
import com.chattriggers.ctjs.modules.ModuleMetadata;
import com.chattriggers.ctjs.utils.console.Console;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.io.FileUtils;

import javax.script.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JSScriptLoader extends ScriptLoader {
    private ScriptEngine scriptEngine;
    private ArrayList<Module> cachedModules;

    @Override
    public void preLoad() {
        super.preLoad();

        try {
            ArrayList<URL> files = new ArrayList<>();
            files.add(new File(System.getProperty("java.home"), "lib/ext/nashorn.jar").toURI().toURL());

            for (File dir : getFoldersInDirectory(modulesDir)) {
                for (File file : dir.listFiles()) {
                    if (file.getName().endsWith(".jar")) {
                        File jar = new File("mods/ChatTriggers/modules/" + dir.getName()
                                + "/" + file.getName());

                        files.add(jar.toURI().toURL());
                    }
                }
            }

            URLClassLoader ucl = new URLClassLoader(files.toArray(new URL[files.size()]), Minecraft.class.getClassLoader());

            Class<?> factoryClass = ucl.loadClass("jdk.nashorn.api.scripting.NashornScriptEngineFactory");
            ScriptEngineFactory factory = (ScriptEngineFactory) factoryClass.getConstructor().newInstance();
            Method getScriptEngine = factory.getClass().getMethod("getScriptEngine", ClassLoader.class);

            this.scriptEngine = (ScriptEngine) getScriptEngine.invoke(factory, ucl);
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
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
    public ArrayList<Module> loadModules() {
        if (cachedModules != null && !cachedModules.isEmpty()) {
            return cachedModules;
        }

        ArrayList<Module> modules = new ArrayList<>();

        for (File dir : getFoldersInDirectory(modulesDir)) {
            Module mod = loadModule(dir, true);

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

            getScriptEngine().eval(module.getCompiledScript());
            return module;
        } catch (IOException | ScriptException e) {
            Console.getConsole().printStackTrace(e);
        }

        return null;
    }

    public boolean downloadModule(String name, boolean existCheck) {
        if (existCheck) {
            try {
                FileUtils.copyURLToFile(new URL("https://chattriggers.com/downloads/metadata/" + name),
                        new File(modulesDir, "currMetadata.json"));
            } catch (IOException e) {
                ChatLib.chat("&cModule not found!");
                return false;
            }

            new File(modulesDir,  "currMetadata.json").delete();
        }

        try {
            File downloadZip = new File(modulesDir, "currDownload.zip");
            File outputDir = new File(modulesDir, name);
            outputDir.delete();
            outputDir.mkdir();
            FileUtils.copyURLToFile(
                    new URL("https://chattriggers.com/downloads/scripts/" + name),
                    downloadZip
            );

            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(downloadZip));
            ZipEntry zipEntry = zis.getNextEntry();

            while(zipEntry != null){
                String fileName = zipEntry.getName();
                File newFile = new File(outputDir, fileName.replaceAll("(?i)" + name + "/", ""));

                if (zipEntry.isDirectory()) {
                    newFile.mkdir();
                    zipEntry = zis.getNextEntry();
                    continue;
                }

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                zipEntry = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
            downloadZip.delete();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
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
     * Gets the script that provides modules basic libraries.
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
