package com.chattriggers.ctjs.engine.module.import

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.NodeVisitor
import java.lang.StringBuilder

data class ForeignModule(
        val id: Int,
        val userId: Int,
        val description: Document,
        val image: String,
        val downloads: Int,
        val hidden: Boolean,
        val createdAt: String,
        val updatedAt: String
) {
    private var cachedDescription: String? = null

    val descriptionString: String
        get() = cachedDescription ?: parseDescriptionDocument()

    private fun parseDescriptionDocument(): String {
        val visitor = MinecraftStringGenerator()
        description.traverse(visitor)
        cachedDescription = visitor.getResult()
        return cachedDescription!!
    }
}

class MinecraftStringGenerator : NodeVisitor {
    private val builder = StringBuilder()

    override fun head(node: Node, depth: Int) {
        when (node) {
            is TextNode -> doTextHead(node, depth)
            is Element -> doElementHead(node, depth)
        }
    }

    override fun tail(node: Node, depth: Int) {
        when (node) {
            is Element -> doElementTail(node, depth)
        }
    }

    private fun doElementHead(node: Element, depth: Int) {
        when (node.tagName()) {
            "li" -> builder.append("• ")
            "b", "strong" -> builder.append("&l")
            "i", "em" -> builder.append("&o")
            "u" -> builder.append("&n")
            "ul", "ol" -> builder.append("\n")
        }
    }

    private fun doElementTail(node: Element, depth: Int) {
        if (node.isBlock && node.tagName() != "div") {
            builder.append("\n")
        }

        when (node.tagName()) {
            "b", "strong", "i", "em", "u" -> builder.append("&r")
            "br" -> builder.append("\n")
        }
    }

     private fun doTextHead(node: TextNode, depth: Int) {
         builder.append(node.text())
     }

    fun getResult(): String {
        return builder.toString().trim()
    }
}