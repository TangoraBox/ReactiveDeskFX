# ReactiveDeskFX (JavaFX and Google Guice MVVM Pattern micro-framework)

 JavaFX micro-framework to develop very fast JavaFX components with minimal code following MVVM architecture pattern with passive view.
 
 FXML auto-loading with Google Guice dependency Injection.

[![License: LGPL v3](https://img.shields.io/badge/License-LGPLv3-blue.svg)](https://opensource.org/licenses/LGPL-3.0)
[![Build Status](https://travis-ci.com/TangoraBox/ReactiveDeskFX.svg?branch=master)](https://travis-ci.com/TangoraBox/ReactiveDeskFX)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.tangorabox%3Areactive-desk-fx&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.tangorabox%3Areactive-desk-fx)

---

## Installation
 
This library is compatible with java 11 or higher version, lower versions are not supported

### Maven artifact

```xml
<dependency>
  <groupId>com.tangorabox</groupId>
  <artifactId>reactive-desk-fx</artifactId>
  <version>1.0</version>
</dependency>
```

## Features

- An a 'ready to go' `ReactiveApplication` class that extends `Application` to auto-instance the Guice Injector, simply extend it and override the `startReactiveApp(Stage primaryStage)` to start your JavaFX Application with Guice Injection. @Inject attributes of your Application class are ready to use in startReactiveApp method call

- Automatic FXML file loading (your component must be inyected, don't use `new`)
  
- `ReactiveModel` combines multiple JavaFX properties in only one model which has invalidation events
  
- Injection of custom components inside your view (through @Inject)
  
- Convention over Configuration: you don't need to specify the component of .fxml, simply place .fxml with the same name in the same location of your component (you must set up maven accord for that)
  
- Optional tag `fxml:controller´ in your .fxml (only needed if you want IDE features)

---

## Example project

If you want to see how to code with this framework please visit my [ResumeFX project](https://github.com/TangoraBox/ResumeFX) witch uses that.

---

## Usage Steps
1. .fxml file must have `<fx:root>` tag (`fx:controller` attribute it's optional)
2. View class must extend the class of your top node in level hierarchy of your .fxml (`type` attribute of `fx:root`)
3. View class must be annotated with `@FXMLView`  (`com.tangorabox.reactivedesk.FXMLView`) 
4. After injection logic can be done in `@FML void initialize()` method as usually in JavaFX (or implementing `Initializable` interface)

### Maven configuration for nested .fxml
.fxml files must be placed at the same level of it's .java component, If you want place .fxml in the same package, not in resources folder,
you must set up maven accordingly:

```xml
<build>
    <resources>
            <resource>
                <directory>src/main/resources</directory>
            </resource>
            <resource>
                <directory>${project.build.sourceDirectory}</directory>
                <excludes>
                    <exclude>**/*.java</exclude>
                </excludes>
            </resource>
        </resources>

        <testResources>
            <testResource>
                <directory>src/test/resources</directory>
            </testResource>
            <testResource>
                <directory>${project.build.testSourceDirectory}</directory>
                <excludes>
                    <exclude>**/*.java</exclude>
                </excludes>
            </testResource>
        </testResources>
</build>

```
---

### Limitations
When custom component is included inside another .fxml, there should be no customization properties in the tag, or fxml loading will crash on runtime.

#### Non compliant example:

```xml
  <CustomComponent fx:id="myComponent" text="Hello World" maxWidth="800"/>
```

#### Compliant example

```xml
  <HBox maxWidth="800">
    <CustomComponent fx:id="myComponent"/>
  </HBox>
``` 

As you can see, restrict size values through a wrapping Parent, or set this in fxml or your CustomComponent, or in the view code.

If you want to set the text "hello world" please do it programmatically in your view code.

## Inspiration Projects

- [Afterburner.fx](https://github.com/AdamBien/afterburner.fx): I think that it's FXMLView class generate multiple empty useless classes in projects, one for each view

- [JuiceFx](https://github.com/garzy/JuiceFX): my fork of Afterburner.fx to add @Guice injection features

- [mvvmFX](https://github.com/sialcasa/mvvmFX): interesting alternative but seems to have problems with java11+

- [fx-guice](https://github.com/cathive/fx-guice): very similar to my project, and I could have used it, but it seems the project is very old and is dead


## Contributing

> If you want to contribute to upgrade this project with new features or fixing bugs, you're welcome, please make a pull request.

---

## Team


| <a href="https://github.com/garzy" target="_blank">**GaRzY**</a> | 
| :---: 
| [![GaRzY](https://avatars0.githubusercontent.com/u/10849239?s=200)](https://github.com/garzy)
| <a href="https://github.com/garzy" target="_blank">`github.com/garzy`</a> | 


---

## Support

Reach out to me at one of the following places!

- Mail to [info@tangorabox.com](mailto:info@tangorabox.com)
- Twitter at <a href="http://twitter.com/garzydj" target="_blank">`@garzydj`</a>

---


## License

[![License: LGPL v3](https://img.shields.io/badge/License-LGPLv3-blue.svg)](https://opensource.org/licenses/LGPL-3.0)
