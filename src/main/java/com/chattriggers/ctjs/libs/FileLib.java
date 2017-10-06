package com.chattriggers.ctjs.libs;

import com.chattriggers.ctjs.utils.console.Console;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileLib {
    public static void write(String importName, String fileName, String toWrite) {
        try {
            FileUtils.write(new File("./mods/ChatTriggers/Imports/" + importName + "/" + fileName), toWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(String importName, String fileName) {
        try {
            return FileUtils.readFileToString(new File("./mods/ChatTriggers/Imports/" + importName + "/" + fileName));
        } catch (IOException e) {
            Console.getConsole().printStackTrace(e);
        }

        return null;
    }
}
