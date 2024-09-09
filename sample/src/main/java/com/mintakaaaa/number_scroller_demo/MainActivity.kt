package com.mintakaaaa.number_scroller_demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mintakaaaa.number_scroller.DetachedNumberScroller
import com.mintakaaaa.number_scroller.DetachedScrollerBehaviour
import com.mintakaaaa.number_scroller.DetachedScrollerStyle
import com.mintakaaaa.number_scroller.IncrementDirection
import com.mintakaaaa.number_scroller.NumberPosition
import com.mintakaaaa.number_scroller.NumberScroller
import com.mintakaaaa.number_scroller.ScrollerBehaviour
import com.mintakaaaa.number_scroller.ScrollerController
import com.mintakaaaa.number_scroller.ScrollerStyle
import com.mintakaaaa.number_scroller.ScrollerTarget
import com.mintakaaaa.number_scroller.TargetBehaviour
import com.mintakaaaa.number_scroller.TargetStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NumberScrollerSample()
//            DetachedScrollerSample()
        }
    }
}

@Composable
fun NumberScrollerSample() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(15.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Number Scroller Sample", fontSize = 30.sp)
        Spacer(Modifier.weight(1f))
        DefaultScroller()
        Spacer(Modifier.weight(1f))
        One()
        Spacer(Modifier.weight(1f))
        Two()
        Spacer(Modifier.weight(1f))
        Three()
        Spacer(Modifier.weight(1f))
        Four()
    }
}

@Composable
fun DetachedScrollerSample() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(15.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Detached number scroller", fontSize = 30.sp)
        Spacer(Modifier.weight(1f))
        Text(text = "Default", fontSize = 30.sp)
        HorizontalDivider(Modifier.padding(bottom = 10.dp))

        DetachedDefault() // SCROLLER

        Spacer(Modifier.weight(1f))
        Text(text = "Custom", fontSize = 30.sp)
        HorizontalDivider()

        DetachedCustom() // SCROLLER

        Spacer(Modifier.weight(1f))
    }
}

@Composable
fun DetachedDefault() {
    // controller uses default styles & behaviours for scroller and its targets
    val controller = remember { ScrollerController() }

    // set up your scroller targets however you like, providing them with a unique ID
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth(1f)) {
        Column {
            Row {
                ScrollerTarget(controller = controller, id = 1)
                Spacer(Modifier.width(10.dp))
                ScrollerTarget(controller = controller, id = 2)
                Spacer(Modifier.width(10.dp))
                ScrollerTarget(controller = controller, id = 3)
            }

            Spacer(Modifier.height(10.dp))

            Row {
                ScrollerTarget(controller = controller, id = 4)
                Spacer(Modifier.width(10.dp))
                ScrollerTarget(controller = controller, id = 5)
                Spacer(Modifier.width(10.dp))
                ScrollerTarget(controller = controller, id = 6)
            }
        }
        Spacer(Modifier.width(10.dp))
        // place your detached scroller anywhere on the screen
        DetachedNumberScroller(controller = controller, linkedTo = listOf(1, 2, 3, 4, 5, 6))
    }
}

@Composable
fun DetachedCustom() {
    // optional scroller & target styles
    val detachedStyle = DetachedScrollerStyle(
        scrollerWidth = 260.dp,
        scrollerHeight = 20.dp,
        scrollerColor = MaterialTheme.colorScheme.primaryContainer,
        scrollerRounding = RoundedCornerShape(20.dp),
        lineWidthFactor = 0.5f,
        lineThickness = 7.dp,
        lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
        lineRounding = RoundedCornerShape(4.dp),
    )
    val targetStyle = TargetStyle(
        numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
        numberFontSize = 22.sp,
        numberFontFamily = FontFamily.Monospace,
        numberFontWeight = FontWeight.SemiBold,
        background = MaterialTheme.colorScheme.primaryContainer,
        selectedBackground = MaterialTheme.colorScheme.tertiaryContainer,
        boxRounding = RoundedCornerShape(6.dp),
        boxWidth = 80.dp,
        boxHeight = 40.dp
    )
    // optional scroller & target behaviour parameters
    val scrollerBehaviour = DetachedScrollerBehaviour(
        syncLinePosWithNumber = false,
        lineSpeed = 10f,
        incrementDirection = IncrementDirection.Right,
    )
    val targetOneBehaviour = TargetBehaviour( // uses auto increment when scrolling to end
        step = 2f,
        startNumber = 50f,
        range = 0f..100f,
        scrollDistanceFactor = 100f,

        autoIncrementOnFarScroll = true,
        autoIncrementDelay = 400,
        farScrollThreshold = 0.9f
    )
    val targetTwoBehaviour = TargetBehaviour( // uses dynamic scroll distance factor
        step = 0.5f,
        startNumber = -5f,
        range = -10f..10f,
        scrollDistanceFactor = 300f,

        useDynamicDistanceFactor = true,
        dynamicDistanceScalingFactor = 8f,
    )
    val targetThreeBehaviour = TargetBehaviour( // uses double tap to edit
        step = 4f,
        startNumber = -4f,
        range = -12f..12f,
        scrollDistanceFactor = 100f,

        doubleTapToEdit = true
    )
    val defaultTargetBehaviour = TargetBehaviour(
        step = 1f,
        startNumber = 0f,
        range = -5f..5f,
        scrollDistanceFactor = 100f
    )
    // ...are supplied to scroller controller
    val controller = remember { ScrollerController(scrollerStyle = detachedStyle, targetStyle = targetStyle, scrollerBehaviour = scrollerBehaviour, defaultTargetBehaviour = defaultTargetBehaviour) }

    // updating text based on which target is updated
    var text by remember { mutableStateOf("Scroll me!") }
    Text(text, style = TextStyle(fontSize = 40.sp), modifier = Modifier.padding(bottom = 20.dp))
    val updateText: (Int, Float) -> Unit = { id, numberSelected ->
        text = "id: $id, number: $numberSelected"
    }
    // ----------------------------------------------

    // set up your scroller targets however you like, providing them with a unique ID
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth(1f)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "1", fontSize = 25.sp, modifier = Modifier.padding(bottom = 4.dp))
            ScrollerTarget(controller = controller, targetBehaviour = targetOneBehaviour, id = 1, onDragEnd = { number ->
                updateText(1, number)
            })
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(horizontal = 10.dp)) {
            Text(text = "2", fontSize = 25.sp, modifier = Modifier.padding(bottom = 4.dp))
            ScrollerTarget(controller = controller, targetBehaviour = targetTwoBehaviour, id = 2, onDragEnd = { number ->
                updateText(2, number)
            })
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "3", fontSize = 25.sp, modifier = Modifier.padding(bottom = 4.dp))
            ScrollerTarget(controller = controller, targetBehaviour = targetThreeBehaviour, id = 3, onDragEnd = { number ->
                updateText(3, number)
            })
        }
    }

    Spacer(Modifier.height(10.dp))
    // place your detached scroller anywhere on the screen
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth(1f)) {
        DetachedNumberScroller(controller = controller, linkedTo = listOf(1, 2, 3, 4, 5, 6))
    }
    Spacer(Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth(1f)) {
        // note below targets don't have targetBehaviour param... using default target behaviour...
        ScrollerTarget(controller = controller, id = 4, onDragEnd = { number ->
            updateText(4, number)
        })
        Spacer(Modifier.width(10.dp))
        ScrollerTarget(controller = controller, id = 5, onDragEnd = { number ->
            updateText(5, number)
        })
        Spacer(Modifier.width(10.dp))
        ScrollerTarget(controller = controller, id = 6, onDragEnd = { number ->
            updateText(6, number)
        })
    }

    Text("1 - Auto increment on far scroll", fontSize = 25.sp, modifier = Modifier.padding(top = 40.dp))
    Text("2 - Dynamic scroll distance factor", fontSize = 25.sp)
    Text("3 - double tap to edit", fontSize = 25.sp)
}

@Composable
fun DefaultScroller() {
    Row { Text(text = "Default", fontSize = 30.sp) }
    HorizontalDivider(Modifier.padding(bottom = 10.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        NumberScroller()
    }
}

@Composable
fun One() {
    val customStyle = ScrollerStyle(
        scrollerWidth = 50.dp,
        scrollerHeight = 100.dp,
        scrollerColor = MaterialTheme.colorScheme.primaryContainer,
        scrollerRounding = RoundedCornerShape(20.dp),
        numberFontSize = 30.sp,
        numberDistanceToScroller = 30.dp,
        numberFontFamily = FontFamily.Monospace,
        numberFontWeight = FontWeight.SemiBold,
        numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
        numberPosition = NumberPosition.Above,
        lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
        lineWidthFactor = 0.5f,
        lineThickness = 8.dp,
        lineRounding = RoundedCornerShape(5.dp),
    )

    Row { Text(text = "Custom", fontSize = 30.sp) }
    HorizontalDivider(Modifier.padding(bottom = 10.dp))

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        NumberScroller(
            style = customStyle,
            behaviour = ScrollerBehaviour(
                step = 2f,
                startNumber = 56f,
                range = 50f..150f,
                scrollDistanceFactor = 25f,
                incrementDirection = IncrementDirection.Up
            )
        )
    }
}

@Composable
fun Two() {
    val customStyle = ScrollerStyle(
        scrollerWidth = 50.dp,
        scrollerHeight = 100.dp,
        scrollerColor = MaterialTheme.colorScheme.primaryContainer,
        scrollerRounding = RoundedCornerShape(20.dp),
        numberFontSize = 30.sp,
        numberDistanceToScroller = 30.dp,
        numberFontFamily = FontFamily.Monospace,
        numberFontWeight = FontWeight.SemiBold,
        numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
        numberPosition = NumberPosition.Right,
        lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
        lineWidthFactor = 0.5f,
        lineThickness = 8.dp,
        lineRounding = RoundedCornerShape(5.dp),
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        NumberScroller(
            style = customStyle,
            behaviour = ScrollerBehaviour(
                startNumber = 5f,
                step = 0.5f,
                range = -20f..30f,
                lineSpeed = 0.5f,
                scrollDistanceFactor = 50f,
                syncLinePosWithNumber = false,
                incrementDirection = IncrementDirection.Down
            )
        )
    }
}

@Composable
fun Three() {
    val customStyle = ScrollerStyle(
        scrollerWidth = 200.dp,
        scrollerHeight = 20.dp,
        scrollerColor = MaterialTheme.colorScheme.primaryContainer,
        scrollerRounding = RoundedCornerShape(20.dp),
        numberFontSize = 30.sp,
        numberDistanceToScroller = 30.dp,
        numberFontFamily = FontFamily.Cursive,
        numberFontWeight = FontWeight.Bold,
        numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
        numberPosition = NumberPosition.Left,
        lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
        lineWidthFactor = 0.4f,
        lineThickness = 8.dp,
        lineRounding = RoundedCornerShape(5.dp),
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        NumberScroller(
            style = customStyle,
            behaviour = ScrollerBehaviour(
                startNumber = 5f,
                step = 0.5f,
                range = -20f..30f,
                lineSpeed = 5f,
                scrollDistanceFactor = 50f,
                syncLinePosWithNumber = false,
                incrementDirection = IncrementDirection.Left
            )
        )
    }
}

@Composable
fun Four() {
    /*
    * Update text when user stops dragging!
    * Get this functionality by specifying what happens onDragEnd in the number scroller params
    */
    var text by remember { mutableStateOf("Scroll me!") }

    Text(text, style = TextStyle(fontSize = 40.sp), modifier = Modifier.padding(bottom = 20.dp))

    val updateText: (Float) -> Unit = { numberSelected ->
        text = "Scrolled to: $numberSelected"
    }

    val customStyle = ScrollerStyle(
        scrollerWidth = 100.dp,
        scrollerHeight = 50.dp,
        scrollerColor = MaterialTheme.colorScheme.primaryContainer,
        scrollerRounding = RoundedCornerShape(10.dp),
        numberFontSize = 30.sp,
        numberFontFamily = FontFamily.Cursive,
        numberFontWeight = FontWeight.Bold,
        numberDistanceToScroller = 0.dp,
        numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
        numberPosition = NumberPosition.Below,
        lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
        lineWidthFactor = 0.3f,
        lineThickness = 8.dp,
        lineRounding = RoundedCornerShape(5.dp),
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        NumberScroller(
            style = customStyle,
            behaviour = ScrollerBehaviour(
                step = 2f,
                lineSpeed = 2.4f,
                startNumber = 5f,
                scrollDistanceFactor = 50f,
                syncLinePosWithNumber = false,
                incrementDirection = IncrementDirection.Right
            ),
            onDragEnd = { numberSelected -> updateText(numberSelected) }
        )
    }
}