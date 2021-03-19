package com.chattriggers.ctjs.engine.loader

data class ModuleDescriptionOnline(
    val description: String,
    val downloads: Int,
    val id: Int,
    val image: String,
    val name: String,
    val owner: Owner,
    val releases: List<Release>,
    val tags: List<String>
)