package com.chattriggers.ctjs.loader;

import com.chattriggers.ctjs.Reference;
import com.chattriggers.ctjs.engine.module.ModuleManager;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Collectors;

//#if FORGE
import net.minecraftforge.fml.common.Loader;
//#else
//$$ import net.fabricmc.loader.api.FabricLoader;
//#endif

public class UriScheme {
    private static final String PROTOCOL = "chattriggers://";
    private static final int PORT = 21965;
    private static final String QUOTE = "\"";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("No URL found, aborting...");
            return;
        }

        if (!args[0].startsWith(PROTOCOL)) {
            System.out.println("URL found is not supported, aborting...");
            System.out.println(args[0]);
            return;
        }

        String url = args[0];
        System.out.println("Trying to work with URL: " + url);
        String module = url.substring(PROTOCOL.length()).replace("/", "");

        try {
            connectWithSockets(module);
        } catch (Exception e) {
            copyModuleIn(module);
        }
    }

    public static void installUriScheme() {
        try {
            regAdd(" /f /ve /d " + quote("URL:chattriggers Protocol"));

            regAdd(" /f /v " + quote("URL Protocol") + " /d " + quote(""));

            String sep = File.separator;
            String javaProgram = System.getProperty("java.home") + sep + "bin" + sep + "javaw.exe";

            // TODO(VERIFY)
            //#if FORGE
            //#if MC<=11202
            String modJar = Loader.instance().getIndexedModList().get(Reference.MODID).getSource().getAbsolutePath();
            //#else
            //$$ String modJar = FMLLoader.getLoadingModList().getModFileById(Reference.MODID).getFile().getFilePath().toAbsolutePath().toString();
            //#endif
            //#else
            //$$ String modJar = FabricLoader.getInstance()
            //$$         .getModContainer(Reference.MODID).get()
            //$$         .getRootPaths().get(0).toAbsolutePath()
            //$$         .toString();
            //#endif

            String value = ("\"" + javaProgram + "\" -cp \"" + modJar
                + "\" com.chattriggers.ctjs.loader.UriScheme " + "\"%1\"").replace("\"", "\\\"");

            regAdd("\\shell\\open\\command /f /ve /d " + "\"" + value + "\"");
        } catch (Exception e) {
            System.err.println("Unable to install chattriggers URI scheme, disregard if OS is not Windows");
        }
    }

    public static void createSocketListener() {
        new Thread(UriScheme::socketListener, "CTJSSocketListener").start();
    }

    private static String quote(String toQuote) {
        return QUOTE + toQuote + QUOTE;
    }

    private static void regAdd(String args) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec("REG ADD HKCU\\Software\\Classes\\chattriggers" + args);
        if (process.waitFor() != 0) {
            throw new IOException("Error editing registry!");
        }
    }

    private static void socketListener() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (!Thread.interrupted()) {
                try (Socket clientSocket = serverSocket.accept()) {
                    InputStream inputStream = clientSocket.getInputStream();
                    String module = new BufferedReader(new InputStreamReader(inputStream))
                        .lines().collect(Collectors.joining("\n"));
                    ModuleManager.INSTANCE.importModule(module);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void connectWithSockets(String module) throws Exception {
        Socket socket = new Socket(InetAddress.getLocalHost(), PORT);
        socket.getOutputStream().write(module.getBytes());
        socket.close();
    }

    private static void copyModuleIn(String module) {
        System.out.println("Adding module named " + module + " to the to download list!");

        String dataFolder = System.getenv("APPDATA");
        File modulesDir = new File(dataFolder + "\\.minecraft\\config\\ChatTriggers\\modules");

        File toDownload = new File(modulesDir, ".to_download.txt");

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(toDownload, true));
            pw.append(module).append(",");
            pw.close();
        } catch (Exception e) {
            System.out.println("Error writing to file.");
        }
    }
}
