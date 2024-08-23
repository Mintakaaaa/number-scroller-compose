package com.mintakaaaa.number_scroller_demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mintakaaaa.number_scroller.NumberPosition
import com.mintakaaaa.number_scroller.NumberScroller
import com.mintakaaaa.number_scroller.ScrollerDirection
import com.mintakaaaa.number_scroller.ScrollerStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(15.dp),
                verticalArrangement = Arrangement.Center
            ) {

                // uncomment out the below composables to see number scroller presets:

                DefaultScroller()
//                VerticalScrollerUp() // scroll up to increment
//                VerticalScrollerDown() // scroll down to increment
//                HorizontalScrollerRight() // scroll right to increment

                // try making a HorizontalScrollerLeft yourself below :]

            }
        }
    }
}

@Composable
fun DefaultScroller() {
    /*
    * No parameters passed to number scroller
    * Style = Default (grey) style
    * Start number = 0
    * Step size = 1
    * Range = -10 to 10
    * Scroll distance factor = 100f
    * scroller line follows the number selected
    * scroller line speed = 1.5f (irrelevant as scroller line follows the number selected)
    * number is to the left of the scroller
    * on number selection nothing happens
    */
    Row(modifier = Modifier.fillMaxWidth(0.6f), horizontalArrangement = Arrangement.End) {
        NumberScroller()
    }
}

@Composable
fun VerticalScrollerUp() {
    /*
    * Completely custom scroller style provided
    * Scroller is vertical & must scroll UP to increment
    * Start number = 56
    * Step size = 2
    * Range = 50 to 150
    * Scroll distance factor = 25f (scroll 25f units to change the number)
    * scroller line follows the number selected
    * number is above the scroller
    * on number selection nothing happens
    */
    val customStyle = ScrollerStyle(
        scrollerWidth = 50.dp,
        scrollerHeight = 100.dp,
        scrollerColor = MaterialTheme.colorScheme.primaryContainer,
        scrollerRounding = RoundedCornerShape(20.dp),
        scrollerDirection = ScrollerDirection.VerticalUp,
        numberFontSize = 30.sp,
        numberDistanceToScroller = 30.dp,
        numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
        numberPosition = NumberPosition.Top,
        lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
        lineWidthFactor = 0.5f,
        lineThickness = 8.dp,
        lineRounding = RoundedCornerShape(5.dp),
    )

    Row(modifier = Modifier.fillMaxWidth(0.6f), horizontalArrangement = Arrangement.Start) {
        NumberScroller(
            style = customStyle,
            step = 2f,
            startNumber = 56f,
            min = 50f,
            max = 150f,
            scrollDistanceFactor = 25f,
        )
    }
}

@Composable
fun VerticalScrollerDown() {
    /*
    * Completely custom scroller style provided
    * Scroller is vertical & must scroll DOWN to increment
    * Start number = 15
    * Step size = 0.5
    * Range = -20 to 30
    * Scroll distance factor = 50f (scroll 50f units to change the number)
    * scroller line doesn't follow the number selected
    * scroller line speed = 0.5f
    * number is to the right of the scroller
    * on number selection nothing happens
    */
    val customStyle = ScrollerStyle(
        scrollerWidth = 50.dp,
        scrollerHeight = 100.dp,
        scrollerColor = MaterialTheme.colorScheme.primaryContainer,
        scrollerRounding = RoundedCornerShape(20.dp),
        scrollerDirection = ScrollerDirection.VerticalDown,
        numberFontSize = 30.sp,
        numberDistanceToScroller = 30.dp,
        numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
        numberPosition = NumberPosition.Right,
        lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
        lineWidthFactor = 0.5f,
        lineThickness = 8.dp,
        lineRounding = RoundedCornerShape(5.dp),
    )

    Row(modifier = Modifier.fillMaxWidth(0.6f), horizontalArrangement = Arrangement.Start) {
        NumberScroller(
            style = customStyle,
            step = 0.5f,
            startNumber = 15f,
            min = -20f,
            max = 30f,
            lineSpeed = 0.5f,
            scrollDistanceFactor = 50f,
            syncLinePosWithNumber = false,
        )
    }
}

@Composable()
fun HorizontalScrollerRight() {
    /*
    * Update text when user stops dragging!
    * Get this functionality by specifying what happens onDragEnd in the number scroller params
    */
    var text by remember { mutableStateOf("Scroll me!") }

    Text(text, style = TextStyle(fontSize = 40.sp))

    val updateText: (Float) -> Unit = { numberSelected ->
        text = "Scrolled to: $numberSelected"
    }

    /*
    * Completely custom scroller style provided
    * Scroller is horizontal & must scroll RIGHT to increment
    * Start number = 0
    * Step size = 1
    * Range = -10 to 10
    * Scroll distance factor = 50f (scroll 50f units to change the number)
    * scroller line doesn't follow the number selected
    * scroller line speed = 2.4f
    * number is below the scroller
    * on number selection, text above scroller is updated
    */
    val customStyle = ScrollerStyle(
        scrollerWidth = 100.dp,
        scrollerHeight = 50.dp,
        scrollerColor = MaterialTheme.colorScheme.primaryContainer,
        scrollerRounding = RoundedCornerShape(10.dp),
        scrollerDirection = ScrollerDirection.HorizontalRight,
        numberFontSize = 30.sp,
        numberDistanceToScroller = 0.dp,
        numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
        numberPosition = NumberPosition.Bottom,
        lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
        lineWidthFactor = 0.3f,
        lineThickness = 8.dp,
        lineRounding = RoundedCornerShape(5.dp),
    )

    Row(modifier = Modifier.fillMaxWidth(0.6f), horizontalArrangement = Arrangement.Center) {
        NumberScroller(
            style = customStyle,
            step = 2f,
            lineSpeed = 2.4f,
            scrollDistanceFactor = 50f,
            syncLinePosWithNumber = false,
            onDragEnd = { numberSelected -> updateText(numberSelected) }
        )
    }
}