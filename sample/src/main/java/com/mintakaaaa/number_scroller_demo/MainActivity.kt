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
import com.mintakaaaa.number_scroller.ScrollerBehaviour
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
    }
}

@Composable
fun DefaultScroller() {
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
        scrollerDirection = ScrollerDirection.VerticalUp,
        numberFontSize = 30.sp,
        numberDistanceToScroller = 30.dp,
        numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
        numberPosition = NumberPosition.Above,
        lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
        lineWidthFactor = 0.5f,
        lineThickness = 8.dp,
        lineRounding = RoundedCornerShape(5.dp),
    )

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        NumberScroller(
            style = customStyle,
            behaviour = ScrollerBehaviour(
                step = 2f,
                startNumber = 56f,
                range = 50f..150f,
                scrollDistanceFactor = 25f,
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
        scrollerDirection = ScrollerDirection.HorizontalLeft,
        numberFontSize = 30.sp,
        numberDistanceToScroller = 30.dp,
        numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
        numberPosition = NumberPosition.Left,
        lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
        lineWidthFactor = 0.03f,
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
            )
        )
    }
}

@Composable()
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
        scrollerDirection = ScrollerDirection.HorizontalRight,
        numberFontSize = 30.sp,
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
                syncLinePosWithNumber = false
            ),
            onDragEnd = { numberSelected -> updateText(numberSelected) }
        )
    }
}