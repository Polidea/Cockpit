package com.polidea.cockpit.sample

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StyleRes

enum class Style(val value: String,
                 @ColorRes val statusBarColorResId: Int,
                 @DrawableRes val backgroundDrawableResId: Int,
                 @StyleRes val alertDialogStyleResId: Int) {
    RED("red",
            R.color.redThemeGradientEndColor,
            R.drawable.red_theme_background,
            R.style.AppTheme_Dialog_Red),
    BLUE("blue",
            R.color.blueThemeGradientEndColor,
            R.drawable.blue_theme_background,
            R.style.AppTheme_Dialog_Blue);

    companion object {
        fun forValue(value: String): Style {
            return values().find { it.value.equals(value, ignoreCase = true) }
                    ?: throw IllegalArgumentException("Style not found for value: $value")
        }
    }
}