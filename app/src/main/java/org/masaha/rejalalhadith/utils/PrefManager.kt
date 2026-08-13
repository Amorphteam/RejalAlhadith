package org.masaha.rejalalhadith.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PrefManager(ctx: Context) {
    private var sharedPreferences: SharedPreferences

    private val fontKey = "pref_font"
    private val searchModeKey = "pref_search_mode"
    private val themeModeKey = "pref_theme_mode"

    companion object {
        val initialFontSize = 20 // px
    }

    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    fun saveFontSize(font: Int) {
        sharedPreferences.edit().putInt(fontKey, font).apply()
    }

    fun getFontSize(): Int {
        return sharedPreferences.getInt(fontKey, initialFontSize)
    }

    fun saveSearchMode(mode: SearchMode) {
        sharedPreferences.edit().putInt(searchModeKey, mode.ordinal).apply()
    }

    fun getSearchMode(): SearchMode {
        return SearchMode.fromOrdinal(
                sharedPreferences.getInt(searchModeKey, SearchMode.NAME_CONTAINS.ordinal)
        )
    }

    fun saveThemeMode(mode: ThemeMode) {
        sharedPreferences.edit().putInt(themeModeKey, mode.ordinal).apply()
    }

    fun getThemeMode(): ThemeMode {
        return ThemeMode.fromOrdinal(
                sharedPreferences.getInt(themeModeKey, ThemeMode.SYSTEM.ordinal)
        )
    }
}
