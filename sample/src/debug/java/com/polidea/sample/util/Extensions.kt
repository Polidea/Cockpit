package com.polidea.sample.util

import com.polidea.androidtweaks.tweaks.Tweaks
import com.polidea.sample.MainActivity

internal fun MainActivity.onEditValues() {
    Tweaks.showTweaks(this)
}