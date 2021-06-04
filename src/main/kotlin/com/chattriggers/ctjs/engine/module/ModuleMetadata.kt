package com.chattriggers.ctjs.engine.module

import com.chattriggers.ctjs.engine.loader.CTRepositoryHandler
import com.chattriggers.ctjs.engine.loader.GitHubRepositoryHandler
import com.chattriggers.ctjs.engine.loader.RepositoryHandler
import java.util.*

data class ModuleMetadata(
    val name: String? = null,
    val version: String? = null,
    var entry: String? = null,
    val asmEntry: String? = null,
    val asmExposedFunctions: Map<String, String>? = null,
    val tags: ArrayList<String>? = null,
    val pictureLink: String? = null,
    val creator: String? = null,
    val description: String? = null,
    val requires: ArrayList<String>? = null,
    val helpMessage: String? = null,
    val changelog: String? = null,
    val ignored: ArrayList<String>? = null,
    var isRequired: Boolean = false,
    val repository: RepositoryInfo? = null
)

sealed class RepositoryInfo(val handler: RepositoryHandler)

class CTRepositoryInfo(val identifier: String, val version: String) : RepositoryInfo(CTRepositoryHandler)

class GitHubRepositoryInfo(
    val username: String,
    val repoName: String,
    val lastModification: Long
) : RepositoryInfo(GitHubRepositoryHandler)
