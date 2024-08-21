package com.mintakaaaa.number_scroller

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// NOTE: bar scroller where the line is a bar that goes with the number (always sticky)
// NOTE: horizontal scroller (sticky too or not)
// NOTE: detached number scroller for scrolling the number of different elements
// NOTE: increment by floats

data class ScrollerStyle(
    val scrollerHeight: Dp = 60.dp,
    val scrollerWidth: Dp = 30.dp,
    val scrollerColor: Color = Color.DarkGray,
    val lineColor: Color = Color.Gray,
    val numberColor: Color = Color.Black,
    val scrollerRounding: RoundedCornerShape = RoundedCornerShape(6.dp),
    val lineRounding: RoundedCornerShape = RoundedCornerShape(4.dp),
    val lineThickness: Dp = 4.dp,
    val lineWidthFactor: Float = 0.8f,
    val numberFontSize: TextUnit = 30.sp,
    val numberDistanceToScroller: Dp = 70.dp,
    val numberPosition: NumberPosition = NumberPosition.Left
)

sealed class NumberPosition {
    data object Left: NumberPosition()
    data object Top: NumberPosition()
    data object Right: NumberPosition()
    data object Bottom: NumberPosition()
}

fun getDecimalPlaces(value: Float): Int {
    val valueString = value.toString()
    return if (valueString.contains('.')) {
        valueString.substringAfter('.').length
    } else 0
}

fun truncateTrailingZeros(x: String): Number {
    val result = x.toBigDecimal().stripTrailingZeros()
    return if (result.scale() <= 0) result.toInt() else result.toFloat()
}

@Composable
fun VerticalNumberScrollerGit(
    style: ScrollerStyle = ScrollerStyle(),
    startNumber: Float = 0f,
    step: Float = 1f,
    min: Float = -10f,
    max: Float = 10f,
    upToIncrement: Boolean = true,
    scrollDistanceFactor: Float = 100f, // how much drag is needed to increment/decrement scroller
    lineSpeed: Float = 1f,
    syncLinePosWithNumber: Boolean = true,
    onDragEnd: (Float) -> Unit = {})
{

    // CRUCIAL: Convert dp to px for accurate positioning
    val scrollerHeightPx = with(LocalDensity.current) { style.scrollerHeight.toPx() }

    var number by remember { mutableFloatStateOf(startNumber) }
    var totalDrag by remember { mutableFloatStateOf(0f) }
    val lineOffset = remember { mutableFloatStateOf(0f) }

    fun repositionLine(currentNumber: Float, dragAmount: Float) {
        if (syncLinePosWithNumber) {
            val normalizedValue = (currentNumber - min).toFloat() / (max - min)
            val offset = (normalizedValue * scrollerHeightPx - scrollerHeightPx / 2)
            lineOffset.floatValue = if (upToIncrement) -offset else offset
        } else {
            lineOffset.floatValue = (lineOffset.floatValue + (dragAmount * (lineSpeed / 8)))
                .coerceIn(-scrollerHeightPx / 2, scrollerHeightPx / 2)
        }
    }

    // Run updateLineOffset on initialization
    LaunchedEffect(startNumber) {
        repositionLine(startNumber, 0f)
    }

    val updateNumber: (Float) -> Unit = { dragAmount ->
        totalDrag += dragAmount // calculate total drag distance

        repositionLine(number, dragAmount)

        // get direction of drag and update scroller number accordingly
        when {
            totalDrag <= -scrollDistanceFactor -> { // scrolling up
                number = if (upToIncrement) {
                    (number + step).coerceAtMost(max) // decrement without exceeding min
                } else {
                    (number - step).coerceAtLeast(min) // decrement without exceeding min
                }
                totalDrag = 0f
            }

            totalDrag >= scrollDistanceFactor -> { // scrolling down
                number = if (upToIncrement) {
                    (number - step).coerceAtLeast(min) // decrement without exceeding min
                } else {
                    (number + step).coerceAtMost(max) // increment without exceeding max
                }
                totalDrag = 0f
            }
        }
    }

    when (style.numberPosition) {
        is NumberPosition.Left, is NumberPosition.Right -> { // place text to left/right of scroller
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (style.numberPosition is NumberPosition.Left) Arrangement.Start else Arrangement.End
            ) {
                if (style.numberPosition is NumberPosition.Left) {
                    NumberText(style, step, number, style.numberPosition)
                    ScrollerBox(style, number, lineOffset, syncLinePosWithNumber, onDragEnd, updateNumber)
                } else {
                    ScrollerBox(style, number, lineOffset, syncLinePosWithNumber, onDragEnd, updateNumber)
                    NumberText(style, step, number, style.numberPosition)
                }
            }
        }

        is NumberPosition.Top, is NumberPosition.Bottom -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (style.numberPosition is NumberPosition.Top) Arrangement.Top else Arrangement.Bottom
            ) {
                if (style.numberPosition is NumberPosition.Top) { // place text to top/bottom of scroller
                    NumberText(style, step, number, style.numberPosition)
                    ScrollerBox(style, number, lineOffset, syncLinePosWithNumber, onDragEnd, updateNumber)
                } else {
                    ScrollerBox(style, number, lineOffset, syncLinePosWithNumber, onDragEnd, updateNumber)
                    NumberText(style, step, number, style.numberPosition)
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun NumberText(style: ScrollerStyle, step: Float, number: Float, position: NumberPosition) {
    val formattedNumber = String.format("%.${getDecimalPlaces(step)}f", number)

    val modifier = when (position) { // distance to scroller based on position of number
        is NumberPosition.Left, is NumberPosition.Right -> Modifier.width(style.numberDistanceToScroller)
        is NumberPosition.Top, is NumberPosition.Bottom -> Modifier.height(style.numberDistanceToScroller)
    }

    Text(
        text = "${truncateTrailingZeros(formattedNumber)}",
        fontSize = style.numberFontSize,
        fontWeight = FontWeight.Bold,
        color = style.numberColor,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun ScrollerBox(
    style: ScrollerStyle,
    number: Float,
    lineOffset: MutableState<Float>,
    syncLinePosWithNumber: Boolean,
    onDragEnd: (Float) -> Unit,
    updateNumber: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .width(style.scrollerWidth)
            .height(style.scrollerHeight)
            .clip(style.scrollerRounding)
            .background(style.scrollerColor)
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        if (!syncLinePosWithNumber) lineOffset.value = 0f
                        onDragEnd(number)
                    }
                ) { _, dragAmount ->
                    updateNumber(dragAmount)
                }
            }
    ) {
        Box( // Scroller Line
            modifier = Modifier
                .width(style.scrollerWidth * style.lineWidthFactor)
                .height(style.lineThickness)
                .align(Alignment.Center)
                .offset { IntOffset(0, lineOffset.value.toInt()) }
                .clip(style.lineRounding)
                .background(style.lineColor)
        )
    }
}