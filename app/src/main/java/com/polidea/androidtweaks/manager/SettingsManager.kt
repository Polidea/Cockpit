package com.polidea.androidtweaks.manager

import android.content.Context
import com.polidea.androidtweaks.model.GenericParam


class SettingsManager(context: Context) {
    val params: List<GenericParam>

    companion object {
        @Volatile
        private var INSTANCE: SettingsManager? = null

        fun getInstance(context: Context) : SettingsManager {
            if (INSTANCE == null) {
                INSTANCE = SettingsManager(context)
            }

            return INSTANCE as SettingsManager
        }
    }

    init {
        params = YamlParser().parseYaml("settings.yml", context)
    }
}