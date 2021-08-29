package com.chattriggers.ctjs.browser

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
    val name: String,
    val description: String,
    val image: String,
    val downloads: Int,
    val tags: List<String>,
    val releases: List<WebsiteRelease>,
    val flagged: Boolean,
)

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
    val releaseVersion: String,
    val modVersion: String,
    val changelog: String,
    val downloads: Int,
    val verified: Boolean,
)
