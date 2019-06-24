package com.polidea.cockpitplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.BaseVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class CockpitPlugin : Plugin<Project> {

    override fun apply(project: Project?) {
        project?.plugins?.withId("com.android.application") {
            val android = project.extensions.getByType(AppExtension::class.java)
            android.applicationVariants.all { variant: BaseVariant ->

                val task = project.tasks.create("generate${variant.name.capitalize()}Cockpit", CockpitTask::class.java) {
                    it.variantName = variant.name
                    it.variantDirName = variant.dirName
                    it.buildTypeName = variant.buildType.name
                    it.flavorDimensionList = android.flavorDimensionList ?: emptyList()
                    it.productFlavorList = android.productFlavors.map { Flavor(it.name, it.dimension) } ?: emptyList()
                    it.buildTypeList = android.buildTypes.map { it.name } ?: emptyList()
                }

                val generateAssetsTask = project.tasks.find {
                    it.name.startsWith("generate", true) &&
                            it.name.contains("assets", true) &&
                            it.name.contains(variant.buildType.name, true)
                }
                generateAssetsTask?.dependsOn(task)

                variant.registerJavaGeneratingTask(task, task.getCockpitOutputDirectory())
                android.sourceSets.first { variant.name == it.name }
                        .apply {
                            java.setSrcDirs(java.srcDirs + task.getCockpitOutputDirectory())
                            assets.setSrcDirs(assets.srcDirs + task.getCockpitAssetsOutputDirectory())
                        }
            }
        }
    }
}