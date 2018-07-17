# Cockpit
<p align="center">
    <img src="https://github.com/Polidea/Cockpit/blob/master/images/Cockpit_logo.jpg" width="594" height="328">
</p>

## Introduction

Cockpit is a helpful tool for Android developers providing a way to define a set of params that can be used in the application and changed anytime without having to recompile the project. It also provides a compact built-in UI so using the library is as simple as possible.

It consists of two parts:  
– Gradle plugin generating a set of params based on user-defined yaml file,  
– Android library containing classes to manage and display those params.

<img src="https://github.com/Polidea/Cockpit/blob/master/images/sample_gif.gif" width="270" height="480">

## Usage
Each defined value is called `param`. The set of params is called `cockpit`.

### Defining params
To define cockpit, you need to create **cockpit.yml** file and place it in your application's **your_app_module_name/cockpit** folder. Params defined in **cockpit.yml** are applied to all flavors. In order to extend cockpit depending on a flavor, create additional files using following naming convention:
cockpitFlavorNames.yml where **FlavorNames** is a list of desired flavors.

Examples:
```
cockpitDebug.yml
cockpitStaging.yml
cockpitStagingDebug.yml
```

File structure is as follows:

- for primitive types (integer, double, string, boolean) you can use either simple or complex structure:
```
paramName1: paramValue1
```
```
paramName2:
  description: "paramName2 description" # this field is optional; if provided, it's used for display instead of param name
  value: paramValue2
```

- for action type:
```
actionTypeName:
  type: action
  description: "Action description"
  buttonText: "Perform" # this value if optional
```

- for list type you can use either simple or complex structure:
```
listComplexTypeName:
  type: list
  values: [ "staging", "testing", "prod" ]
  selectedItemIndex: 1 # this field is optional; if not provided, 0 is assumed
```

```
listSimpleTypeName: [ "staging", "testing", "prod" ]
```

Let's consider example cockpit.yml file:
```
double_param: 1.0  
string_param: "testValue"  
int_param: 1  
boolean_param: true
action_param:
  type: action
  description: "Action description"
  buttonText: "Perform"
```

This will get parsed into:
```
Double double_param = 1.0;
String string_param = "testValue";
Integer int_param = 1;
Boolean boolean_param = true;
CockpitAction actionParam: "action description"
```
> Supported param types are integer, double, string, boolean, list and action.

> Please note that param names are case-sensitive.

### Generating Cockpit

CockpitPlugin will generate `Cockpit.java` file for you. 
Cockpit functionality is by design available only for debug builds, so `Cockpit.java` file will contain only getters in the release build. This is to prevent any unauthorized param value changes.

### Accessing param values
You can access the params via generated getters and setters. Each param has corresponding `getParam_name()` and `setParam_name()`, where `param_name` is your param's name.

### Listening for value changes
You can listen for value changes by adding PropertyChangeListeners.\
Each changeable param has methods: `addOnParamNameChangeListener()` and `removeOnParamNameChangeListener()` where `paramName` is param's name.

### Listening for action requests
Action params don't change theirs values. They request taking some action every time you click the corresponding button.
To listen for that requests every action param has methods: `addParamNameActionRequestCallback()` and `removeParamNameActionRequestCallback()` where `paramName` is param's name.

### Displaying cockpit
Cockpit library offers you an easy way to display and edit all the params you defined. All you need to do is call
`Cockpit.showCockpit(fragmentManager: FragmentManager)`
This will display our compact, built-in UI where you can edit the params' values. When you're done, just dismiss the dialog.

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
        classpath "gradle.plugin.com.polidea.cockpit:CockpitPlugin:2.0.0"  
   }  
}

dependencies {
    debugImplementation 'com.polidea.cockpit:cockpit:2.0.0'  
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
