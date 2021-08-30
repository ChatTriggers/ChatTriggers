package com.chattriggers.ctjs.browser

import com.chattriggers.ctjs.CTJS
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object WebsiteAPI {
    fun login(username: String, password: String): Boolean {
        val (code, _) = sendFormUrlEncodedRequest("${CTJS.WEBSITE_ROOT}/api/account/login") {
            put("username", username)
            put("password", password)
        }

        return code == 200
    }

    fun createAccount(username: String, email: String, password: String): Boolean {
        val (code, _) = sendFormUrlEncodedRequest("${CTJS.WEBSITE_ROOT}/api/account/new") {
            put("username", username)
            put("email", email)
            put("password", password)
        }

        if (code != 200)
            return false

        return login(username, password)
    }

    private fun sendFormUrlEncodedRequest(url: String, builder: PostRequestBuilder.() -> Unit): Response {
        val prb = PostRequestBuilder()
        prb.builder()
        val urlData = prb.urlString

        val connection: HttpURLConnection = (URL(url).openConnection() as HttpURLConnection).apply {
            setRequestProperty("User-Agent", "Mozilla/5.0")
            requestMethod = "POST"
            doOutput = true
        }

        try {
            connection.connect()

            OutputStreamWriter(connection.outputStream).use {
                it.write(urlData)
                it.flush()
            }

            connection.responseCode

            val text = BufferedReader(InputStreamReader(connection.inputStream)).use {
                it.readText()
            }

            return Response(connection.responseCode, text)
        } finally {
            connection.disconnect()
        }
    }

    private data class Response(val code: Int, val text: String)

    class PostRequestBuilder {
        var urlString = ""

        fun put(key: String, value: String) {
            val encodedKey = URLEncoder.encode(key, "UTF-8")
            val encodedValue = URLEncoder.encode(value, "UTF-8")

            if (urlString == "") {
                urlString = "$encodedKey=$encodedValue"
            } else {
                urlString += "&$encodedKey=$encodedValue"
            }
        }
    }
}