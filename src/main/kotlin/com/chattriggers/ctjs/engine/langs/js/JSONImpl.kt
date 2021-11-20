package com.chattriggers.ctjs.engine.langs.js

import kotlin.reflect.KVisibility
import kotlin.reflect.full.memberProperties

interface JSONImpl {
    fun toJSON(key: String): String {
        val sb = StringBuilder()
        sb.append("{")

        sb.append(javaClass.kotlin.memberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .joinToString(separator = ",\n") { """"${it.name}": "${it.get(this)}"""" })

        if (sb.length > 1) sb.append(",\n")
        sb.append(""""toString": "$this"""")

        sb.append("}")

        return sb.toString()
    }
}
