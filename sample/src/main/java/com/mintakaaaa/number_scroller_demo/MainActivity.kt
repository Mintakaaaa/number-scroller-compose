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

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                horizontalAlignment = Alignment.Start
            ) {

                val customStyleVertical = ScrollerStyle(
                    scrollerWidth = 50.dp,
                    scrollerHeight = 100.dp,
                    scrollerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrollerRounding = RoundedCornerShape(20.dp),
                    scrollerDirection = ScrollerDirection.Vertical.Up,
                    numberFontSize = 30.sp,
                    numberDistanceToScroller = 30.dp,
                    numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    numberPosition = NumberPosition.Right,
                    lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    lineWidthFactor = 0.5f,
                    lineThickness = 8.dp,
                    lineRounding = RoundedCornerShape(5.dp),
                )
                val customStyleHorizontal = ScrollerStyle(
                    scrollerWidth = 100.dp,
                    scrollerHeight = 50.dp,
                    scrollerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrollerRounding = RoundedCornerShape(10.dp),
                    scrollerDirection = ScrollerDirection.Horizontal.Right,
                    numberFontSize = 30.sp,
                    numberDistanceToScroller = 0.dp,
                    numberColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    numberPosition = NumberPosition.Top,
                    lineColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    lineWidthFactor = 0.3f,
                    lineThickness = 8.dp,
                    lineRounding = RoundedCornerShape(5.dp),
                )
                Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)) {
                    Column() {
                        Text(text = "Default scroller", fontSize = 20.sp)
                        Text(text = "No custom style", fontSize = 20.sp)
                    }

                    Spacer(Modifier.weight(1f))

                    NumberScroller()
                }
                Spacer(Modifier.weight(1f))

                Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)) {

                    NumberScroller(
                        style = customStyleVertical,
                        step = 2f,
                        lineSpeed = 2.4f,
                        scrollDistanceFactor = 50f,
                    )
                    Spacer(Modifier.weight(1f))
                    Column() {
                        Text(text = "Vertical scroller", fontSize = 20.sp)
                        Text(text = "Right number", fontSize = 20.sp)
                    }
                }

                Spacer(Modifier.weight(1f))

                Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)) {
                    Column() {
                        Text(text = "Horizontal scroller", fontSize = 20.sp)
                        Text(text = "Top number", fontSize = 20.sp)
                        Text(text = "No number/line sync", fontSize = 20.sp)

                    }
                    Spacer(Modifier.weight(1f))
                    NumberScroller(
                        style = customStyleHorizontal,
                        step = 2f,
                        lineSpeed = 2.4f,
                        scrollDistanceFactor = 50f,
                        syncLinePosWithNumber = false,
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(text = "- Scroller directions [Vertical Up/Down, Horizontal Left/Right]", fontSize = 16.sp)
                Text(text = "- Adjustable number range & step size", fontSize = 16.sp)
                Text(text = "- Adjustable line speed & drag distance for number change", fontSize = 16.sp)
                Text(text = "- Scroller number positions [Top, Bottom, Left, Right]", fontSize = 16.sp)
            }
        }
    }
}