package org.masaha.rejalalhadith.utils

import android.support.v7.app.AppCompatDelegate

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM;

    fun toNightMode(): Int {
        return when (this) {
            LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            DARK -> AppCompatDelegate.MODE_NIGHT_YES
            SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }

    companion object {
        fun fromOrdinal(ordinal: Int): ThemeMode {
            return values().getOrElse(ordinal) { SYSTEM }
        }
    }
}
