package com.chattriggers.ctjs.engine.module

import com.chattriggers.ctjs.browser.pages.BrowserModuleProvider
import com.chattriggers.ctjs.browser.pages.BrowserReleaseProvider
import java.io.File

class Module(
    override val name: String,
    var metadata: ModuleMetadata,
    val folder: File,
) : BrowserModuleProvider {
    override val creator: String? get() = metadata.creator
    override val description: String? get() = metadata.description
    override val tags: List<String> get() = metadata.tags ?: emptyList()
    override val releases: List<BrowserReleaseProvider> get() = emptyList()

    override fun toString() = "Module{name=$name,version=${metadata.version}}"
}
