/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chattriggers.ctjs.utils

import com.google.gson.*
import com.google.gson.internal.Streams
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class RuntimeTypeAdapterFactory<T>(
    private val baseType: Class<T>,
    private val typeFieldName: String = "type",
    private val maintainType: Boolean = false
) : TypeAdapterFactory {
    private val labelToSubtype = LinkedHashMap<String, Class<*>>()
    private val subtypeToLabel = LinkedHashMap<Class<*>, String>()

    fun <U : T> registerSubtype(type: Class<U>, label: String = type.simpleName) = apply {
        if (type in subtypeToLabel || label in labelToSubtype)
            throw IllegalArgumentException("types and labels must be unique")

        labelToSubtype[label] = type
        subtypeToLabel[type] = label
    }

    override fun <U : Any> create(gson: Gson, type: TypeToken<U>): TypeAdapter<U>? {
        if (type.rawType != baseType)
            return null

        val labelToDelegate = LinkedHashMap<String, TypeAdapter<*>>()
        val subtypeToDelegate = LinkedHashMap<Class<*>, TypeAdapter<*>>()

        for ((key, value) in labelToSubtype) {
            val delegate = gson.getDelegateAdapter(this, TypeToken.get(value))
            labelToDelegate[key] = delegate
            subtypeToDelegate[value] = delegate
        }

        return object : TypeAdapter<U>() {
            override fun read(reader: JsonReader): U {
                val jsonElement = Streams.parse(reader)
                val labelJsonElement = if (maintainType) {
                    jsonElement.asJsonObject.get(typeFieldName)
                } else {
                    jsonElement.asJsonObject.remove(typeFieldName)
                }

                if (labelJsonElement == null)
                    throw JsonParseException("cannot deserialize $baseType because it does not define a field named $typeFieldName")

                val label = labelJsonElement.asString
                val delegate = labelToDelegate[label] as? TypeAdapter<U>
                    ?: throw JsonParseException("cannot deserialize $baseType subtype named $label")

                return delegate.fromJsonTree(jsonElement)
            }

            override fun write(writer: JsonWriter, value: U) {
                val srcType = value::class.java
                val label = subtypeToLabel[srcType]
                val delegate = subtypeToDelegate[srcType] as? TypeAdapter<U>
                    ?: throw JsonParseException("cannot serialize ${srcType.name}")

                val jsonObject = delegate.toJsonTree(value).asJsonObject
                if (maintainType) {
                    Streams.write(jsonObject, writer)
                    return
                }

                val clone = JsonObject()
                if (jsonObject.has(typeFieldName))
                    throw JsonParseException("cannot serialize ${srcType.name} because it already defines a field named $typeFieldName")

                clone.add(typeFieldName, JsonPrimitive(label))

                for ((key, value2) in jsonObject.entrySet())
                    clone.add(key, value2)

                Streams.write(clone, writer)
            }
        }.nullSafe()
    }
}
