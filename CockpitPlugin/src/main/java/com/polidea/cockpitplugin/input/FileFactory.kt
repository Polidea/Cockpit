package com.polidea.cockpitplugin.input

import java.io.File

internal interface FileFactory {
    fun file(path: String): File
}