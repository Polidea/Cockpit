package com.polidea.cockpitplugin.input

import com.polidea.cockpitplugin.Flavor
import java.io.File

class InputFilesProvider(private val cockpitDirectoryPath: String,
                         private val fileFactory: FileFactory) {

    fun getAllCockpitFilesForCurrentVariant(dimensions: List<String>, flavors: List<Flavor>, variantName: String, buildTypes: List<String>): List<File> {

        // Let's assume we define following flavors in build.gradle file:
        //
        //        flavorDimensions "api", "mode"
        //
        //        productFlavors {
        //            demo {
        //                dimension "mode"
        //                applicationIdSuffix ".demo"
        //                versionNameSuffix "-demo"
        //            }
        //            full {
        //                dimension "mode"
        //                applicationIdSuffix ".full"
        //                versionNameSuffix "-full"
        //            }
        //            prod {
        //                dimension "api"
        //                applicationIdSuffix ".prod"
        //            }
        //            staging {
        //                dimension "api"
        //                applicationIdSuffix ".staging"
        //            }
        //        }
        //
        // Let's assume also, we are currently building a 'stagingDemoDebug' variant.
        // Let's take a look, what parameters will take our function:
        //
        // dimensions == ["api", "mode"]
        // flavors == [Flavor("demo", "mode"), Flavor("full", "mode"), Flavor("prod", "api"), Flavor("staging", "api")
        // variantName == "stagingDemoDebug"
        // buildTypes == ["debug", "release"]
        //
        // According to:
        //
        // https://developer.android.com/studio/write/add-resources#resource_merging
        //
        // resource merging prority is as follows:
        // main source set < product flavor < build type < build variant
        // For example:
        // main < demo < debug < stagingDemoDebug
        //
        // Unfortunately this rule doesn't cover all cases. For example it's not clear which flavor
        // (demo or staging) has higher priority.
        //
        // According to:
        //
        // https://developer.android.com/studio/build/build-variants#flavor-dimensions
        //
        // "... product flavors belonging to higher-priority flavor dimension appear first,
        // followed by those from lower-priority dimensions, followed by the build type."
        //
        // It means that `staging` flavor has higher priority than `demo` flavor, because it appears first
        // in variant's name: `stagingDemoDebug`. Moreover, more complex flavors has higher priority than less complex,
        // so `stagingDemo` flavor has higher priority than either `staging` or `demo` flavors.
        //
        // Someone could ask a question: "Why `staging` flavor is first in variant's name `stagingDemoDebug`?
        // Why the variant name is `stagingDemoDebug` not `demoStagingDebug`?" The name (and flavors' priority) is determined by
        // order of flavorDimensions in build.gradle file. In our case this is:
        //
        //        flavorDimensions "api", "mode"
        //
        // `staging` belongs to `api` dimension, so it's the first
        // `demo` belongs to `mode` dimesion, so it's the second in `stagingDemoDebug` variant's name
        //
        // Finally, resource merging priority is following:
        // main < demo < staging < demoStaging < debug < stagingDemoDebug
        //
        // It corresponds to Cockpit files priority:
        //
        // cockpit.yml < cockpitDemo.yml < cockpitStaging.yml < cockpitDemoStaging.yml < cockpitDebug.yml < cockpitStagingDemoDebug.yml
        //
        // Let's find out how to create that list of files:
        // [cockpit.yml, cockpitDemo.yml, cockpitStaging.yml, cockpitDemoStaging.yml, cockpitDebug.yml, cockpitStagingDemoDebug.yml]
        //

        val variantsOrderedFromLowestPriority = ArrayList<String>()

        // `main` has the lowest priority, so we add it as a first
        // Empty string corresponds with `main` variant
        // because cockpit.yml doesn't contains "main" string in its name
        variantsOrderedFromLowestPriority.add("")

        // Next, the lowest priority has `demo` and `staging` flavors.
        //
        // Declared dimension's order is following:
        //
        //        flavorDimensions "api", "mode"
        //
        // so we have to reverse dimensions list, to add `demo` (from `mode` dimension) first
        // and `staging` (from `api` dimension) after `demo`
        //
        // Only `staging` and `demo` flavors should be considered, because only those two flavors appear in
        // variant `stagingDemoDebug`. So we find only those variants which appears in variant's name.

        val singleFlavors = dimensions.reversed().mapNotNull { dimension ->
            flavors.find { it.dimension == dimension && variantName.contains(it.name, ignoreCase = true) }?.name
        }.map { it.capitalize() } // Demo, Staging

        variantsOrderedFromLowestPriority.addAll(singleFlavors)

        // Composite flavor exists only when application has more than 1 flavor dimension
        if (dimensions.size > 1) {

            // Next, the lowest priority has `stagingDemo` flavor
            //
            // singleFlavors is equals ["Demo", "Staging"], so we have to reverse again the list
            // to have ["Staging", "Demo"] and append each item to a single string to get "StagingDemo"

            val compositeFlavor = singleFlavors.reversed().fold(StringBuilder()) { acc: StringBuilder, flavor: String ->
                acc.append(flavor)
            }.toString() //StagingDemo

            variantsOrderedFromLowestPriority.add(compositeFlavor)
        }

        // If application has no flavors at all, build type is identical to variant's name and it shouldn't be
        // added twice
        if (dimensions.isNotEmpty()) {

            // Next, the lowest priority has build type.
            buildTypes.find { variantName.contains(it, ignoreCase = true) }?.let {
                variantsOrderedFromLowestPriority.add(it.capitalize())
            } // debug
        }

        // At the end, we should add variantName as a last item, as variantName has the highest priority in
        // resource merging order
        variantsOrderedFromLowestPriority.add(variantName.capitalize()) // stagingDemoDebug

        // Now we converts ["", "Demo", "Staging", "DemoStaging", ...] list to
        // ["cockpit.yml", "cockpitDemo.yml", "cockpitStaging.yml", "cockpitStagingDemo.yml", ... ] list
        return variantsOrderedFromLowestPriority.map { fileFactory.file(cockpitDirectoryPath + "cockpit" + it + ".yml") }
    }
}