package com.chattriggers.ctjs.utils

data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
) {
    operator fun compareTo(other: Version): Int {
        major.compareTo(other.major).let {
            if (it != 0)
                return it
        }

        minor.compareTo(other.minor).let {
            if (it != 0)
                return it
        }

        return patch.compareTo(other.patch)
    }
}
