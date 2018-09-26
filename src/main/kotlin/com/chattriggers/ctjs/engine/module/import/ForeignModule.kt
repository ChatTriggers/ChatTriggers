package com.chattriggers.ctjs.engine.module.import

import org.jsoup.nodes.Document

data class ForeignModule(
        val id: Int,
        val userId: Int,
        val description: Document,
        val image: String,
        val downloads: Int,
        val hidden: Boolean,
        val createdAt: String,
        val updatedAt: String
)