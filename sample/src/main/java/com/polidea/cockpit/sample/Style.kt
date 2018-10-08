package com.polidea.cockpit.sample

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes

enum class Style(val value: String,
                 @ColorRes val statusBarColorResId: Int,
                 @DrawableRes val backgroundDrawableResId: Int,
                 @DrawableRes val bubbleBackgroundDrawableResId: Int,
                 @ColorRes val itemNameTextColorResId: Int,
                 @ColorRes val successColorResId: Int,
                 @StyleRes val alertDialogStyleResId: Int) {
    RED("red",
            R.color.redStatusBarColor,
            R.drawable.red_theme_background,
            R.drawable.red_theme_bubble,
            R.color.redThemeItemTextColor,
            R.color.redButtonDoneColor,
            R.style.AppTheme_Dialog_Red
    ),
    BLUE("blue",
            R.color.blueStatusBarColor,
            R.drawable.blue_theme_background,
            R.drawable.blue_theme_bubble,
            R.color.blueThemeItemTextColor,
            R.color.blueButtonDoneColor,
            R.style.AppTheme_Dialog_Blue
    );

    companion object {
        fun forValue(value: String): Style {
            return values().find { it.value.equals(value, ignoreCase = true) }
                    ?: throw IllegalArgumentException("Style not found for value: $value")
        }
    }
}