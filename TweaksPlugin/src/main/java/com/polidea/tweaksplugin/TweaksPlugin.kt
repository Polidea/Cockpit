package com.polidea.tweaksplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

class TweaksPlugin : Plugin<Project> {

    override fun apply(project: Project?) {
        project?.plugins?.withId("com.android.application") {
            val android = project.extensions.getByType(AppExtension::class.java)
            android.applicationVariants.all { variant: BaseVariant ->

                val task = project.tasks.create("generate${variant.name.capitalize()}Tweaks", TweaksTask::class.java) {
                    it.variantName = variant.dirName
                    it.buildTypeName = variant.buildType.name
                }

                variant.registerJavaGeneratingTask(task, task.getTweaksOutputDirectory())
                android.sourceSets.first { variant.name == it.name }
                        .apply {
                            java.setSrcDirs(java.srcDirs + task.getTweaksOutputDirectory())
                        }
            }
        }
    }
}