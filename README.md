# Android Jetpack Compose Number Scroller

The `NumberScroller` component is a customisable UI element that allows users to scroll a number (decimals supported) through a specified range using up/down and left/right drag gestures. 

The `DetachedNumberScroller` component is the almost the same, but instead supports more than one scrollable number. 

[![](https://jitpack.io/v/Mintakaaaa/number-scroller-compose.svg)](https://jitpack.io/#Mintakaaaa/number-scroller-compose)

# Demo

![alt text](https://github.com/Mintakaaaa/number-scroller-compose/blob/main/images/number-scroller-demo.gif "Scroller Demo")
![alt text](https://github.com/Mintakaaaa/number-scroller-compose/blob/main/images/detached-scroller-demo-1.1.1.gif "Scroller Demo")

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
  implementation 'com.github.Mintakaaaa:number-scroller-compose:1.1.1'
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
  <version>1.1.1</version>
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
libraryDependencies += "com.github.Mintakaaaa" % "number-scroller-compose" % "1.1.1"	
```
## Leiningen
1. Add the JitPack repository to your build file
   
   Add it in your project.clj at the end of repositories:
```
:repositories [["jitpack" "https://jitpack.io"]]
```
2. Add the dependency
```
:dependencies [[com.github.Mintakaaaa/number-scroller-compose "1.1.1"]]
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

# NumberScroller Component Configuration

## Data Classes

### ScrollerStyle

Defines the styling options for the `NumberScroller`.

| Parameter                    | Type                 | Description                                                  | Default Value                  |
|------------------------------|----------------------|--------------------------------------------------------------|--------------------------------|
| **scrollerHeight**           | `Dp`                 | The height of the scroller.                                  | `60.dp`                        |
| **scrollerWidth**            | `Dp`                 | The width of the scroller.                                   | `40.dp`                        |
| **scrollerColor**            | `Color`              | The background color of the scroller.                        | `Color.DarkGray`               |
| **lineColor**                | `Color`              | The color of the scroller line.                              | `Color.Gray`                   |
| **numberColor**              | `Color`              | The color of the number text.                                | `Color.Black`                  |
| **scrollerRounding**         | `RoundedCornerShape` | The shape of the corners of the scroller.                    | `RoundedCornerShape(6.dp)`     |
| **lineRounding**             | `RoundedCornerShape` | The shape of the corners of the scroller line.               | `RoundedCornerShape(4.dp)`     |
| **lineThickness**            | `Dp`                 | The thickness of the scroller line.                          | `4.dp`                         |
| **lineWidthFactor**          | `Float`              | The proportion of the scroller width that the line occupies. | `0.8f`                         |
| **numberFontSize**           | `TextUnit`           | The font size of the number text.                            | `30.sp`                        |
| **numberDistanceToScroller** | `Dp`                 | The distance between the number text and the scroller.       | `30.dp`                        |
| **numberPosition**           | `NumberPosition`     | The position of the number relative to the scroller.         | `NumberPosition.Left`          |
| **scrollerDirection**        | `ScrollerDirection`  | The direction in which the scroller operates.                | `ScrollerDirection.VerticalUp` |

### ScrollerBehaviour

Defines the behaviour options for the `NumberScroller`.

| Parameter                 | Type                              | Description                                                                          | Default Value |
|---------------------------|-----------------------------------|--------------------------------------------------------------------------------------|---------------|
| **startNumber**           | `Float`                           | The initial value of the number displayed by the scroller.                           | `0f`          |
| **step**                  | `Float`                           | The amount by which the number is incremented or decremented with each drag gesture. | `1f`          |
| **range**                 | `ClosedFloatingPointRange<Float>` | The range of values that the number can be set to.                                   | `-10f..10f`   |
| **scrollDistanceFactor**  | `Float`                           | The distance the user must drag to trigger a number change.                          | `100f`        |
| **lineSpeed**             | `Float`                           | The speed factor for scrolling line movement.                                        | `1.5f`        |
| **syncLinePosWithNumber** | `Boolean`                         | Whether to synchronize the position of the scroller line with the number value.      | `true`        |

## Enums

### NumberPosition

Represents the possible positions of the number relative to the scroller.

| Value     | Description                                            |
|-----------|--------------------------------------------------------|
| **Above** | The number is positioned above the scroller.           |
| **Below** | The number is positioned below the scroller.           |
| **Left**  | The number is positioned to the left of the scroller.  |
| **Right** | The number is positioned to the right of the scroller. |

### IncrementDirection

Represents the possible directions in which the scroller can increment.

| Value     | Description                                     |
|-----------|-------------------------------------------------|
| **Up**    | The scroller increments scrolling upwards.      |
| **Down**  | The scroller increments scrolling downwards.    |
| **Left**  | The scroller increments scrolling to the left.  |
| **Right** | The scroller increments scrolling to the right. |

## NumberScroller Composable Function

Displays the `NumberScroller` UI component.

| Parameter     | Type                | Description                                                                                      | Default Value         |
|---------------|---------------------|--------------------------------------------------------------------------------------------------|-----------------------|
| **style**     | `ScrollerStyle`     | The styling options for the scroller.                                                            | `ScrollerStyle()`     |
| **behaviour** | `ScrollerBehaviour` | The behaviour options for the scroller.                                                          | `ScrollerBehaviour()` |
| **onDragEnd** | `(Float) -> Unit`   | Callback function called when the drag operation ends, with the current number as the parameter. | Empty function        |


# Detached Number Scroller

**Default detached number scroller**

```kotlin
val controller = remember { ScrollerController() }
ScrollerTarget(controller = controller, id = 1)
DetachedNumberScroller(controller = controller, linkedTo = listOf(1))
```


**Fully customised number scroller**

```kotlin
val customScrollerStyle = DetachedScrollerStyle(
    scrollerWidth = 50.dp,
    scrollerHeight = 100.dp,
    scrollerRounding = RoundedCornerShape(20.dp),
    scrollerDirection = ScrollerDirection.VerticalUp,
    scrollerColor = MaterialTheme.colorScheme.primaryContainer,
    lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
    lineWidthFactor = 0.5f,
    lineThickness = 8.dp,
    lineRounding = RoundedCornerShape(5.dp)
)

val customTargetStyle = TargetStyle(
    numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
    numberFontSize = 30.sp,
    boxPadding = 10.dp,
    boxWidth = 60.dp,
    boxHeight = 60.dp,
    background = MaterialTheme.colorScheme.background,
    selectedBackground = MaterialTheme.colorScheme.primary,
    boxRounding = RoundedCornerShape(8.dp)
)

val customScrollerBehaviour = ScrollerBehaviour(
    scrollDistanceFactor = 25f,
    lineSpeed = 2f,
    syncLinePosWithNumber = false
)

val customTargetBehaviour = TargetBehaviour(
    step = 2f,
    startNumber = 56f,
    range = 50f..150f
)

val targetOneBehaviour = TargetBehaviour(
    step = 4f,
    startNumber = 14f,
    range = 10f..30f
)

val controller = remember { ScrollerController(
    scrollerStyle = customScrollerStyle,
    targetStyle = customTargetStyle,
    scrollerBehaviour = customScrollerBehaviour,
    defaultTargetBehaviour = customTargetBehaviour,
) }

ScrollerTarget(controller = controller, targetBehaviour = targetOneBehaviour, id = 1, onDragEnd = { number ->
    // Do whatever you want with the resulting number upon scroll finish
})
ScrollerTarget(controller = controller, id = 2) // this uses default target behaviour & does not return number
ScrollerTarget(controller = controller, id = 3) // this uses default target behaviour & does not return number

DetachedNumberScroller(
    controller = controller,
    linkedTo = listOf(1, 2, 3)
)
```

# DetachedNumberScroller Component Configuration

## Data Classes

### DetachedScrollerStyle

Defines the styling options for the `DetachedNumberScroller`.

| Parameter             | Type                 | Description                                                  | Default Value                  |
|-----------------------|----------------------|--------------------------------------------------------------|--------------------------------|
| **scrollerHeight**    | `Dp`                 | The height of the scroller.                                  | `60.dp`                        |
| **scrollerWidth**     | `Dp`                 | The width of the scroller.                                   | `40.dp`                        |
| **scrollerColor**     | `Color`              | The background color of the scroller.                        | `Color.DarkGray`               |
| **lineColor**         | `Color`              | The color of the scroller line.                              | `Color.Gray`                   |
| **scrollerRounding**  | `RoundedCornerShape` | The shape of the corners of the scroller.                    | `RoundedCornerShape(6.dp)`     |
| **lineRounding**      | `RoundedCornerShape` | The shape of the corners of the scroller line.               | `RoundedCornerShape(4.dp)`     |
| **lineThickness**     | `Dp`                 | The thickness of the scroller line.                          | `4.dp`                         |
| **lineWidthFactor**   | `Float`              | The proportion of the scroller width that the line occupies. | `0.8f`                         |
| **scrollerDirection** | `ScrollerDirection`  | The direction in which the scroller operates.                | `ScrollerDirection.VerticalUp` |

### TargetBehaviour

Defines the behaviour options for the `ScrollerTarget`.

| Parameter       | Type                              | Description                                                                          | Default Value |
|-----------------|-----------------------------------|--------------------------------------------------------------------------------------|---------------|
| **startNumber** | `Float`                           | The initial value of the number displayed by the scroller.                           | `0f`          |
| **step**        | `Float`                           | The amount by which the number is incremented or decremented with each drag gesture. | `1f`          |
| **range**       | `ClosedFloatingPointRange<Float>` | The range of values that the number can be set to.                                   | `-10f..10f`   |

### DetachedScrollerBehaviour

Defines the behaviour options for the `DetachedNumberScroller`.

| Parameter                 | Type      | Description                                                                     | Default Value |
|---------------------------|-----------|---------------------------------------------------------------------------------|---------------|
| **scrollDistanceFactor**  | `Float`   | The distance the user must drag to trigger a number change.                     | `100f`        |
| **lineSpeed**             | `Float`   | The speed factor for scrolling line movement.                                   | `1.5f`        |
| **syncLinePosWithNumber** | `Boolean` | Whether to synchronize the position of the scroller line with the number value. | `true`        |

### TargetStyle

Defines the styling options for the `ScrollerTarget`.

| Parameter              | Type                 | Description                                           | Default Value              |
|------------------------|----------------------|-------------------------------------------------------|----------------------------|
| **numberColor**        | `Color`              | The color of the number displayed in the target.      | `Color.Black`              |
| **numberFontSize**     | `TextUnit`           | The font size of the number, in scaled pixels (sp).   | `25.sp`                    |
| **boxPadding**         | `Dp`                 | The padding around the number within the target box.  | `5.dp`                     |
| **boxWidth**           | `Dp`                 | The width of the target box.                          | `50.dp`                    |
| **boxHeight**          | `Dp`                 | The height of the target box.                         | `50.dp`                    |
| **background**         | `Color`              | The background color of the target box.               | `Color.LightGray`          |
| **selectedBackground** | `Color`              | The background color of the target box when selected. | `Color.Green`              |
| **boxRounding**        | `RoundedCornerShape` | The rounding of the corners of the target box.        | `RoundedCornerShape(0.dp)` |

## ScrollerController

Manages the state and behavior of the scroller and its targets.

| Parameter                  | Type                    | Description                                   | Default Value             |
|----------------------------|-------------------------|-----------------------------------------------|---------------------------|
| **scrollerStyle**          | `DetachedScrollerStyle` | The styling options for the scroller.         | `DetachedScrollerStyle()` |
| **targetStyle**            | `TargetStyle`           | The styling options for the scroller targets. | `TargetStyle()`           |
| **scrollerBehaviour**      | `ScrollerBehaviour`     | The behavior options for the scroller.        | `ScrollerBehaviour()`     |
| **defaultTargetBehaviour** | `TargetBehaviour`       | The behavior options for all targets.         | `TargetBehaviour()`       |

## DetachedNumberScroller Composable Function

Displays the `DetachedNumberScroller` UI component.

| Parameter      | Type                 | Description                                           | Default Value |
|----------------|----------------------|-------------------------------------------------------|---------------|
| **controller** | `ScrollerController` | The controller managing the scroller and its targets. | None          |
| **linkedTo**   | `List<Int>`          | A list of target IDs to link the scroller to.         | Empty list    |


# Miscellaneous

1. `lineSpeed` is irrelevant when `syncLinePosWithNumber = true` as the line snaps to the correct position dictated by the selected number and the range.
