package com.polidea.cockpit

import android.app.Application
import com.polidea.cockpit.utils.FileUtils


class CockpitApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FileUtils(this).readCockpitFromFile()
    }
}