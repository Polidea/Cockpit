package com.polidea.androidtweaks

import android.app.Application
import com.polidea.androidtweaks.utils.FileUtils


class TweaksApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        FileUtils(this).readTweaksFromFile()
    }
}