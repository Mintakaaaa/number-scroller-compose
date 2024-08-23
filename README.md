# Documentation is in the works.

# Android Jetpack Compose Number Scroller Composable

This package contains a customisable composable function for Android Jetpack Compose that provides the user a way to scroll through specified numbers (decimals supported).

[![](https://jitpack.io/v/Mintakaaaa/number-scroller-compose.svg)](https://jitpack.io/#Mintakaaaa/number-scroller-compose)

# Demo

![alt text](https://github.com/Mintakaaaa/number-scroller-compose/blob/main/images/demo.gif "Scroller Demo")

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
  implementation 'com.github.Mintakaaaa:number-scroller-compose:1.0.0'
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
  <version>1.0.0</version>
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
libraryDependencies += "com.github.Mintakaaaa" % "number-scroller-compose" % "1.0.0"	
```
## Leiningen
1. Add the JitPack repository to your build file
   
   Add it in your project.clj at the end of repositories:
```
:repositories [["jitpack" "https://jitpack.io"]]
```
2. Add the dependency
```
:dependencies [[com.github.Mintakaaaa/number-scroller-compose "1.0.0"]]
```

# Using The Number Scroller


**Default number scroller** 

```kotlin
NumberScroller()
```


**Fully customised number scroller**

```kotlin
val customStyle = ScrollerStyle(
        scrollerWidth = 50.dp,
        scrollerHeight = 100.dp,
	scrollerRounding = RoundedCornerShape(20.dp),
        scrollerDirection = ScrollerDirection.VerticalUp,
        scrollerColor = MaterialTheme.colorScheme.primaryContainer,
        lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
        numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
        numberFontSize = 30.sp,
        numberDistanceToScroller = 30.dp,
        numberPosition = NumberPosition.Above,
        lineWidthFactor = 0.5f,
        lineThickness = 8.dp,
        lineRounding = RoundedCornerShape(5.dp)
)
val customBehaviour = ScrollerBehaviour(
	step = 2f,
	startNumber = 56f,
	range = 50f..150f,
	scrollDistanceFactor = 25f,
	lineSpeed = 2f,
	syncLinePosWithNumber = false
)
NumberScroller(
	style = customStyle,
	behaviour = customBehaviour
)

```
