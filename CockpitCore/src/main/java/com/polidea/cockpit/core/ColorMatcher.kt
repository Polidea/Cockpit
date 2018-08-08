package com.polidea.cockpit.core

import java.util.regex.Pattern

object ColorMatcher {

    private const val hexColorRegex = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})\$"
    private val hexColorPattern = Pattern.compile(hexColorRegex)

    fun isHexColor(color: String) = hexColorPattern.matcher(color).matches()
}