package com.chattriggers.ctjs.minecraft.libs;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.utils.console.Console;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileLib {
    /**
     * Writes a file to folder in Imports.
     *
     * @param importName name of the import
     * @param fileName   name of the file
     * @param toWrite    string to write in file
     */
    public static void write(String importName, String fileName, String toWrite) {
        write(CTJS.getInstance().getConfig().getModulesFolder().value + "/" + importName + "/" + fileName, toWrite);
    }

    /**
     * Writes a file to anywhere on the system.<br>
     * Use "./" for the ".minecraft" folder.<br>
     *
     * @param fileLocation the location and file name
     * @param toWrite      string to write in file
     */
    public static void write(String fileLocation, String toWrite) {
        try {
            FileUtils.write(new File(fileLocation), toWrite);
        } catch (IOException exception) {
            Console.getConsole().printStackTrace(exception);
        }
    }

    /**
     * Reads a file from folder in Imports.<br>
     * Returns an empty string if file is not found.
     *
     * @param importName name of the import
     * @param fileName   name of the file
     * @return the string in the file
     */
    public static String read(String importName, String fileName) {
        return read(CTJS.getInstance().getConfig().getModulesFolder().value + "/" + importName + "/" + fileName);
    }

    /**
     * Reads a file from anywhere on the system.<br>
     * Use "./" for the ".minecraft" folder.<br>
     * Returns an empty string if file is not found.
     *
     * @param fileLocation the location and file name
     * @return the string in the file
     */
    public static String read(String fileLocation) {
        try {
            File file = new File(fileLocation);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"));

            if (!file.exists() || br.readLine() == null) {
                br.close();
                return "";
            }

            br.close();
            return FileUtils.readFileToString(file);
        } catch (IOException exception) {
            Console.getConsole().printStackTrace(exception);
        }

        return "";
    }

    /**
     * Gets the contents of a url as a string
     *
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

    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath the zip file path
     * @param destDirectory the destination directory
     * @throws IOException IOException
     */
    public static void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    // helper method for unzipping
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        File toWrite = new File(filePath);
        toWrite.getParentFile().mkdirs();
        toWrite.createNewFile();

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
