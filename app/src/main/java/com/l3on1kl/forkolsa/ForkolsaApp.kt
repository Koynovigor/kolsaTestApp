package com.l3on1kl.forkolsa

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.l3on1kl.forkolsa.ui.theme.ThemePreference
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ForkolsaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(ThemePreference.loadNightMode(this))
    }
}
