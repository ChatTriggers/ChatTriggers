package com.chattriggers.ctjs.libs;

import com.chattriggers.ctjs.utils.console.Console;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class FileLib {
    /**
     * Writes a file to folder in Imports.
     * @param importName name of the import
     * @param fileName name of the file
     * @param toWrite string to write in file
     */
    public static void write(String importName, String fileName, String toWrite) {
        try {
            FileUtils.write(new File("./mods/ChatTriggers/Imports/" + importName + "/" + fileName), toWrite);
        } catch (IOException exception) {
            Console.getConsole().printStackTrace(exception);
        }
    }

    /**
     * Reads a file from folder in Imports.
     * @param importName name of the import
     * @param fileName name of the file
     * @return the string in the file
     */
    public static String read(String importName, String fileName) {
        try {
            File file = new File("./mods/ChatTriggers/Imports/" + importName + "/" + fileName);
            BufferedReader br = new BufferedReader(new FileReader(file));

            if (!file.exists() || br.readLine() == null) {
                br.close();
                return null;
            }

            br.close();
            return FileUtils.readFileToString(file);
        } catch (IOException exception) {
            Console.getConsole().printStackTrace(exception);
        }

        return null;
    }

    /**
     * Gets the contents of a url as a string
     * @param theUrl the url to get the data from
     * @return the string stored in the url content
     */
    public static String getUrlContent(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (Exception exception) {
            Console.getConsole().printStackTrace(exception);
        }

        return content.toString();
    }
}
