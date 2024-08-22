# Documentation is in the works.

# Android Jetpack Compose Number Scroller Composable

This package contains a customisable composable function for Android Jetpack Compose that provides the user a way to scroll through specified numbers (decimals supported).

[![](https://jitpack.io/v/Mintakaaaa/number-scroller-compose.svg)](https://jitpack.io/#Mintakaaaa/number-scroller-compose)

# Demo

# Installation
## Gradle
1. Add the JitPack repository to your build file
   
   Add it in your root build.gradle at the end of repositories:
```
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenCentral()
		maven { url 'https://jitpack.io' }
	}
}
```
2. Add the dependency
```
dependencies {
  implementation 'com.github.Mintakaaaa:number-scroller-compose:1.0'
}
```
## Maven
1. Add the JitPack repository to your build file
```
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
2. Add the dependency
```
<dependency>
  <groupId>com.github.Mintakaaaa</groupId>
  <artifactId>number-scroller-compose</artifactId>
  <version>1.0</version>
</dependency>
```
## Sbt
1. Add the JitPack repository to your build file
   
   Add it in your build.sbt at the end of resolvers:
```
resolvers += "jitpack" at "https://jitpack.io"
```
2. Add the dependency
```
libraryDependencies += "com.github.Mintakaaaa" % "number-scroller-compose" % "1.0"	
```
## leiningen
1. Add the JitPack repository to your build file
   
   Add it in your project.clj at the end of repositories:
```
:repositories [["jitpack" "https://jitpack.io"]]
```
2. Add the dependency
```
:dependencies [[com.github.Mintakaaaa/number-scroller-compose "1.0"]]
```
