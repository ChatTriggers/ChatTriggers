package com.chattriggers.ctjs.engine.module.import

data class ForeignModule(
    val id: Int,
    val userId: Int,
    val description: String,
    val image: String,
    val downloads: Int,
    val hidden: Boolean,
    val createdAt: String,
    val updatedAt: String
) {
    // TODO
}