package com.chattriggers.ctjs.browser

import com.chattriggers.ctjs.browser.pages.BrowserModuleProvider
import com.chattriggers.ctjs.browser.pages.BrowserReleaseProvider
import com.google.gson.annotations.SerializedName

data class WebsiteResponse(
    val meta: WebsiteMeta,
    val modules: List<WebsiteModule>,
)

data class WebsiteMeta(
    val limit: Int,
    val offset: Int,
    val total: Int,
)

data class WebsiteModule(
    val id: Int,
    val owner: WebsiteOwner,
    override val name: String,
    override val description: String,
    val image: String,
    val downloads: Int,
    override val tags: List<String>,
    override val releases: List<WebsiteRelease>,
    val flagged: Boolean,
) : BrowserModuleProvider {
    override val creator: String get() = owner.name
}

data class WebsiteOwner(
    val id: Int,
    val name: String,
    val rank: Rank,
) {
    enum class Rank {
        @SerializedName("admin")
        Admin,
        @SerializedName("trusted")
        Trusted,
        @SerializedName("default")
        Default,
    }
}

data class WebsiteRelease(
    val id: String,
    override val releaseVersion: String,
    override val modVersion: String,
    override val changelog: String,
    val downloads: Int,
    val verified: Boolean,
) : BrowserReleaseProvider {

}
