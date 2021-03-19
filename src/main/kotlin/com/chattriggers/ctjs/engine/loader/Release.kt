package com.chattriggers.ctjs.engine.loader

data class Release(
    val changelog: String,
    val downloads: Int,
    val id: String,
    val modVersion: String,
    val releaseVersion: String
)