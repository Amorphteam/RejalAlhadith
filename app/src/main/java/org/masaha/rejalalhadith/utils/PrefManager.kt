package org.masaha.rejalalhadith.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import org.masaha.rejalalhadith.database.DataRepositoryImpl

class PrefManager(ctx: Context) {
    private var sharedPreferences: SharedPreferences

    private val fontKey = "pref_font"

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
}