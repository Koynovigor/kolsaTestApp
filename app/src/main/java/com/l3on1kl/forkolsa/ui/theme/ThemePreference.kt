package com.l3on1kl.forkolsa.ui.theme

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit

object ThemePreference {
    private const val PREF_NAME = "theme_pref"
    private const val KEY_NIGHT_MODE = "night_mode"

    fun saveNightMode(context: Context, mode: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit {
                putInt(KEY_NIGHT_MODE, mode)
            }
    }

    fun loadNightMode(context: Context): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}
