# Cockpit

<img src="https://github.com/Polidea/Cockpit/blob/master/images/sample_gif.gif" width="270" height="480">

## Introduction

Cockpit is a helpful tool for Android developers providing a way to define a set of params that can be used in the application and changed anytime without having to recompile the project. It also provides a compact built-in UI so using the usage of the library is as simple as possible.

It consists of two parts:  
– Gradle plugin generating a set of params based on user-defined yaml file,  
– Android library containing classes to manage and display those params.  
## Usage
Each defined value is called `param`. The set of params is called `cockpit`.

### Defining params
To define cockpit, you need to create **cockpit.yml** file and place it in your application's src/main/assets folder.

File structure is as follows:
```
paramName1: paramValue1
paramName2: paramValue2
...
```
Let's consider example cockpit.yml file:
```
double_param: 1.0  
string_param: "testValue"  
int_param: 1  
boolean_param: true
```

This will get parsed into:
```
Double double_param = 1.0;
String string_param = "testValue";
Integer int_param = 1;
Boolean boolean_param = true;
```
> Supported param types are integer, double, string and boolean. Only flat yaml structure is supported in current version.

> Please note that param names are case-sensitive.

### Generating Cockpit

CockpitPlugin will generate `Cockpit.java` file for you. Cockpit functionality is by design available only for debug builds, so `Cockpit.java` file won't contain any setters in the release build. This is to prevent any unauthorized param value changes.

### Accessing param values
You can access the params via generated getters and setters. Each param has corresponding `getparam_name()` and `setparam_name()`, where `param_name` is your param's name.
>Setters are generated only for debug builds. 

### Displaying cockpit
Cockpit library offers you an easy way to display and edit all the params you defined. All you need to do is call
`Cockpit.showCockpit(context: Context)`
This will display our compact, built-in UI where you can edit the params' values. When you're done, just tap `save` button.

## Download
```
apply plugin: 'com.polidea.cockpit'  

buildscript {  
    repositories {
        mavenCentral()  
        maven {  
            url "https://plugins.gradle.org/m2/"
        }
    }  
    dependencies {  
        classpath "gradle.plugin.com.polidea.cockpit:CockpitPlugin:1.0.0"  
   }  
}

dependencies {
    debugImplementation 'com.polidea.cockpit:cockpit:1.0.0'  
}
```
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
