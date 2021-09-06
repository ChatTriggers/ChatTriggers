package com.chattriggers.ctjs.browser

import com.chattriggers.ctjs.CTJS
import com.chattriggers.ctjs.utils.kotlin.fromJson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object WebsiteAPI {
    private val idRegex = """"id": ?(\d+)""".toRegex()

    // We only care about the ID
    data class LoginResponse(val id: Int)

    fun login(username: String, password: String): LoginResponse? {
        val (code, res) = sendFormUrlEncodedRequest("${CTJS.WEBSITE_ROOT}/api/account/login") {
            put("username", username)
            put("password", password)
        }

        if (code != 200)
            return null

        // Kind scuffed
        val match = idRegex.find(res) ?: return null
        return LoginResponse(match.groupValues[1].toInt())
    }

    fun createAccount(username: String, email: String, password: String): LoginResponse? {
        val (code, _) = sendFormUrlEncodedRequest("${CTJS.WEBSITE_ROOT}/api/account/new") {
            put("username", username)
            put("email", email)
            put("password", password)
        }

        return if (code == 200) login(username, password) else null
    }

    fun getUserModules(id: Int): List<WebsiteModule> {
        val url = "${CTJS.WEBSITE_ROOT}/api/modules?owner=$id"

        return try {
            val response = URL(url).openConnection().apply {
                setRequestProperty("User-Agent", "Mozilla/5.0")
            }.getInputStream().bufferedReader().readText()

            CTJS.gson.fromJson(response)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
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