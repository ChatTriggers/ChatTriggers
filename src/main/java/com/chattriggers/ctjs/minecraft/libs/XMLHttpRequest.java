package com.chattriggers.ctjs.minecraft.libs;

import com.chattriggers.ctjs.CTJS;
import com.chattriggers.ctjs.utils.console.Console;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class XMLHttpRequest {
    private final static String USER_AGENT = "Mozilla/5.0";

    private HttpURLConnection conn;
    private boolean async;
    private String methodCallback;

    public int status;
    public String statusText;
    public String responseText;
    public HashMap<String, Object> extras = new HashMap<>();

    public void open(String method, String urlStr, boolean async) {
        try {
            this.async = async;
            URL url = new URL(urlStr);

            this.status = -1;
            this.statusText = null;
            this.responseText = null;

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
        } catch (Exception e) {
            Console.getConsole().printStackTrace(e);
        }
    }

    public void addRequestHeader(String key, String value) {
        if (conn == null) throw new IllegalStateException("Connection must be opened first!");

        conn.addRequestProperty(key, value);
    }

    /**
     * Sets the callback method, passes in the XMLHttpRequest object
     *
     * @param methodName the method to be called back on completion of the request
     */
    public void setCallbackMethod(String methodName) {
        this.methodCallback = methodName;
    }

    /**
     * Send a post request to the currently opened connection
     *
     * @param parameters any number of post data, in the form of <code>"key", "value", "key", "value"</code>
     */
    public void send(String... parameters) {
        addRequestHeader("User-Agent", USER_AGENT);

        try {
            if (async) {
                new Thread(() -> sendPost(parameters)).start();
            } else {
                sendPost(parameters);
            }
        } catch (Exception e) {
            Console.getConsole().printStackTrace(e);
        }
    }

    private void sendPost(String... parameters) {
        try {
            List<String> paramList = Arrays.asList(parameters);

            StringBuilder data = new StringBuilder();

            for (int i = 0; i < paramList.size(); i += 2) {
                String key = URLEncoder.encode(paramList.get(i), "UTF-8");
                String value = URLEncoder.encode(paramList.get(i + 1), "UTF-8");

                if (i != 0) {
                    data.append("&");
                }

                data.append(key).append("=").append(value);
            }

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data.toString());
            wr.flush();

            sendGet();
        } catch (Exception e) {
            Console.getConsole().printStackTrace(e);
        }
    }

    /**
     * Send a GET request to the currently opened connection
     */
    public void send() {
        addRequestHeader("User-Agent", USER_AGENT);

        try {
            if (async) {
                new Thread(this::sendGet).start();
            } else {
                sendGet();
            }
        } catch (Exception e) {
            Console.getConsole().printStackTrace(e);
        }
    }

    private void sendGet() {
        try {
            this.status = conn.getResponseCode();
            this.statusText = conn.getResponseMessage();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

            this.responseText = response.toString();

            CTJS.getInstance().getModuleManager().invokeFunction(this.methodCallback, this);
        } catch (Exception e) {
            Console.getConsole().printStackTrace(e);
        }
    }

    /**
     * Get the value of the response header with the specified key
     *
     * @param headerName the key for the header
     * @return the value of the response header
     */
    public String getResponseHeader(String headerName) {
        return conn.getHeaderField(headerName);
    }
}
