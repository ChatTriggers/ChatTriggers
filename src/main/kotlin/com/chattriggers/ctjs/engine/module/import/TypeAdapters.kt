package com.chattriggers.ctjs.engine.module.import

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.reflect.Type

class BooleanTypeAdapter : JsonDeserializer<Boolean> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Boolean {
        return json.asInt != 0
    }
}

class DocumentTypeAdapter : JsonDeserializer<Document> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Document {
        return Jsoup.parse(json.asString)
    }
}