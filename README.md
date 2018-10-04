# Cockpit
<p align="center">
    <img src="https://github.com/Polidea/Cockpit/blob/master/images/Cockpit_logo.jpg" width="594" height="328">
</p>

## Introduction

Cockpit is a helpful tool – a debug menu – for Android developers, testers and designers providing a way to define a set of params that can be used in the application and changed anytime without having to recompile the project. It also provides a compact built-in UI so using the library is as simple as possible.

It consists of three parts:  
- Gradle plugin generating a set of params based on user-defined yaml file,  
- Android library containing classes to manage and display those params,  
- CockpitCore module containing classes common for plugin and the library.

<img src="https://github.com/Polidea/Cockpit/blob/development/images/cockpit-2.1.1.gif" width="270" height="480">

Each defined value is called `param`. The set of params is called `cockpit`.

## Usage
In [Getting started](#getting-started) we show the most basic example of how to get Cockpit to work in your project. If you're interested in more, please take a look at [Documentation](#documentation) section below.

### Getting started
To start, you need to integrate Cockpit into your app. Please take a look at [Installation](#installation) section to learn how to do it. When you're all set up, create **cockpit.yml** file and put it in your application's **your_app_module_name/cockpit** folder. It can look like this:
```
color:
  type: color
  description: "Text color"
  value: "#00ff33"
colorDescription: "Calm green"
fontSize: 30
showDescription: true
```
After you've built your project, `Cockpit.java` file will get generated for you. You can use it like any other code in your project.

<p float="left">
    <img src="https://github.com/Polidea/Cockpit/blob/development/images/cockpit-full.png" width="216" height="384" />
    <img src="https://github.com/Polidea/Cockpit/blob/development/images/cockpit-half.png" width="216" height="384" />
    <img src="https://github.com/Polidea/Cockpit/blob/development/images/cockpit-list.png" width="216" height="384" />
</p>

### Documentation

Below, divided into sections, you can find a detailed description on how to use Cockpit and its features.
#### Defining params
To define cockpit, you need to create **cockpit.yml** file and place it in your application's **your_app_module_name/cockpit** folder. Params defined in **cockpit.yml** are applied to all flavors. In order to extend or change cockpit depending on the flavor, create additional files using following naming convention:
cockpit<flavor_name>.yml where `<flavorName>` is a desired flavor.

Examples:
```
cockpitDebug.yml
cockpitStaging.yml
cockpitStagingDebug.yml
```

For basic functionality you can use simple, flat yaml structure, for example:
```
text: "example"
endpointList: [ "staging", "testing", "prod" ]
fontSize: 18
showDialog: true
```

If you need some more attributes or want to use action param, you can use more complex structure. Currently Cockpit debug panel supports following param types:
##### primitives (integer, double, string, boolean)

```
fontSize:
  description: "Header font size" # this field is optional; if provided, it's used for display instead of param name
  value: 18
```

##### action
```
showDialog:
  type: action
  description: "Show dialog" # optional
  buttonText: "Perform" # this value is optional
```

##### list
```
endpointList:
  type: list
  values: [ "staging", "testing", "prod" ]
  selectedItemIndex: 1 # this field is optional; if not provided, 0 is assumed
```

##### range slider
```
lengthSlider:
  type: range
  min: 0
  max: 32
  value: 8 # optional; if not provided, min is assumed
  step: 0.1 # optional; if not provided, 1 is assumed
  description: "Length" # optional
```

#### color
```
fontColor:
  type: color
  description: "Font color" # optional
  value: "#223344" # supported color formats are #AARRGGBB and #RRGGBB
```

#### read only param
```
appVersion:
  type: read_only
  description: "Version" # optional
```

`read_only` param can be used when you have some information you want to display in Cockpit panel, but you don't need to change it. Build version is a good example. To set value of such param, you'll need to use generated setter for that param before displaying Cockpit panel. Value will be displayed in a TextView.

> Supported param types are integer, double, string, boolean, list and action. All items inside a list have to be the same type.

> Please note that param names are case-sensitive and have to be unique.

> You can create **groups of params** and name them. To define a group, you need to use extended structure of a param and add "group" field. All parameters without defined groups will be listed as part of "Default" group.
```
fontSize:
  description: "Font size"
  value: 18
  group: "Header"
fontColor:
  type: color
  description: "Font color"
  value: "#223344"
  group: "Header"
```

#### Generating Cockpit

CockpitPlugin will generate `Cockpit.java` file for you.
Cockpit functionality is by design available only in debug builds, so `Cockpit.java` file in the release build will contain:
- only getters for primitive param types,
- selected value getter for list type,
- nothing for action type.

This is to prevent any unauthorized param value changes.

#### Accessing param values
You can access the params via generated getters and setters. Each primitive type param has corresponding `getParamName()` and `setParamName()`, where `paramName` is your param's name. List param has `getParamNameSelectedValue()`.

#### Listening for value changes
You can listen for value changes by adding appropriate PropertyChangeListeners.\
Each changeable param has methods `addOnParamNameChangeListener()` and `removeOnParamNameChangeListener()`, where `paramName` is param's name.

#### Listening for action requests
Action params don't change their values. They request performing an action every time you click on the corresponding button.
To listen for those requests action param has `addParamNameActionRequestCallback()` and `removeParamNameActionRequestCallback()` methods, where `paramName` is param's name.

#### Displaying Cockpit
Cockpit library offers an easy way to display and edit all the params you defined. All you need to do is call
`Cockpit.showCockpit(fragmentManager: FragmentManager)`
This will display our compact, built-in developer menu UI where you can edit the params' values. When you're done, just dismiss the dialog. To make it easy to observe your changes, you can pull the Cockpit fragment down to fit half of the screen's height and pull it up to get it displayed in full.

#### Restoring default values
After you've made some changes in Cockpit and decided you didn't like them, it's helpful to be able to restore default values. You can do it globally or for selected param only using curved arrow icon.

## Installation
To integrate Cockpit debug menu into your project, you'll need to add several pieces of code to your `build.gradle`.

First, add plugin declaration:
```
apply plugin: 'com.polidea.cockpit'
```

Add `mavenCentral()` and `maven` into your `buildscript#repositories` section:
```
buildscript {  
    repositories {
        mavenCentral()  
        maven {  
            url "https://plugins.gradle.org/m2/"
        }
    }
}

```

Then add CockpitPlugin classpath into your `buildscript#dependencies`:

```
buildscript {  
    dependencies {  
        classpath "gradle.plugin.com.polidea.cockpit:CockpitPlugin:2.1.0"  
   }  
}
```
Last thing is to add Cockpit library dependency:

```
dependencies {
    debugImplementation 'com.polidea.cockpit:cockpit:2.1.0'  
}
```

## Building sample app
When you attempt to build the sample project for the first time, you're most likely to encounter `plugin not found` error. That's because sample app uses local build of the plugin. To fix the problem:
- on MacOS/Linux run `./pluginAndCore.sh -b` from your project and then build sample app,
- on Windows build CockpitCore, CockpitPlugin and, at last, sample app.

## Integration ideas
When it comes to library integration with your app, it really depends on what is available in your particular case. We think it's a nice idea to use Seismic library by Square (https://github.com/square/seismic) and launch Cockpit panel on shake:
```kotlin
class SampleActivity: AppCompatActivity(), ShakeDetector.Listener {  

    private val shakeDetector = ShakeDetector(this)  

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        initShakeDetection()
    }  

    private fun initShakeDetection() {
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        shakeDetector.start(sensorManager)
    }  

    override fun onStop() {
        super.onStop()
        shakeDetector.stop()
    }

    override fun hearShake() {
        Cockpit.showCockpit(supportFragmentManager)
    }
}
```

Another simple idea is to launch Cockpit debug panel on button click or on specific gesture (multi-touch, double tap, etc.).

No matter which way you choose, to display Cockpit, you just have to call `Cockpit#showCockpit(FragmentManager fragmentManager)` method.
## Troubleshooting
You may come across following error:
```
org.gradle.api.tasks.StopExecutionException: Configuration on demand is not supported by the current version of the Android Gradle plugin since you are using Gradle version 4.6 or above.
Suggestion: disable configuration on demand by setting org.gradle.configureondemand=false in your gradle.properties file or use a Gradle version less than 4.6.
```

To get it to work, you need to disable configuration on demand in your Android Studio's settings (`Build, Execution, Deployment` -> `Compiler`, uncheck `configure on demand` option).

## License
```
Copyright 2018 Polidea

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
