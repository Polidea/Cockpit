package com.polidea.tweaksplugin

import org.gradle.api.Plugin
import org.gradle.api.Project


class TweaksPlugin: Plugin<Project> {
    override fun apply(project: Project?) {
        val task: TweaksTask? = project?.tasks?.create("generateTweaks", TweaksTask::class.java)
    }
}