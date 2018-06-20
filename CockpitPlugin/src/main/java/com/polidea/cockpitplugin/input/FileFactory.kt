package com.polidea.cockpitplugin.input

import java.io.File

interface FileFactory {
    fun file(path: String): File
}