package com.mintakaaaa.number_scroller

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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

data class ScrollerStyle(
    val scrollerHeight: Dp = 60.dp,
    val scrollerWidth: Dp = 40.dp,
    val scrollerColor: Color = Color.DarkGray,
    val lineColor: Color = Color.Gray,
    val numberColor: Color = Color.Black,
    val scrollerRounding: RoundedCornerShape = RoundedCornerShape(6.dp),
    val lineRounding: RoundedCornerShape = RoundedCornerShape(4.dp),
    val lineThickness: Dp = 4.dp,
    val lineWidthFactor: Float = 0.8f,
    val numberFontSize: TextUnit = 30.sp,
    val numberDistanceToScroller: Dp = 30.dp,
    val numberPosition: NumberPosition = NumberPosition.Left,
    val scrollerDirection: ScrollerDirection = ScrollerDirection.Vertical.Up,
)

sealed class NumberPosition {
    data object Left: NumberPosition()
    data object Top: NumberPosition()
    data object Right: NumberPosition()
    data object Bottom: NumberPosition()
}

sealed class ScrollerDirection {
    sealed class Horizontal : ScrollerDirection() {
        data object Left : Horizontal()
        data object Right : Horizontal()
    }
    sealed class Vertical : ScrollerDirection() {
        data object Up : Vertical()
        data object Down : Vertical()
    }
}

@Composable
fun NumberScroller(
    style: ScrollerStyle = ScrollerStyle(),
    startNumber: Float = 0f,
    step: Float = 1f,
    min: Float = -10f,
    max: Float = 10f,
    scrollDistanceFactor: Float = 100f,
    lineSpeed: Float = 1.5f,
    syncLinePosWithNumber: Boolean = true,
    onDragEnd: (Float) -> Unit = {})
{

    // CRUCIAL: Convert dp to px for accurate positioning
    val scrollerHeightPx = with(LocalDensity.current) { style.scrollerHeight.toPx() }
    val scrollerWidthPx = with(LocalDensity.current) { style.scrollerWidth.toPx() }

    var number by remember { mutableFloatStateOf(startNumber) }
    var totalDrag by remember { mutableFloatStateOf(0f) }
    val lineOffset = remember { mutableFloatStateOf(0f) }

    fun repositionLine(currentNumber: Float, dragAmount: Float) {
        if (syncLinePosWithNumber) {

            val offset: Float
            val normalizedValue = (currentNumber - min) / (max - min)

            when (style.scrollerDirection) {
                is ScrollerDirection.Horizontal -> {
                    offset = (normalizedValue * scrollerWidthPx - scrollerWidthPx / 2)
                    when (style.scrollerDirection) {
                        ScrollerDirection.Horizontal.Left -> {
                            lineOffset.floatValue = -offset
                        }
                        ScrollerDirection.Horizontal.Right -> {
                            lineOffset.floatValue = offset
                        }
                    }
                }

                is ScrollerDirection.Vertical -> {
                    offset = (normalizedValue * scrollerHeightPx - scrollerHeightPx / 2)
                    when (style.scrollerDirection) {
                        ScrollerDirection.Vertical.Up -> {
                            lineOffset.floatValue = -offset
                        }
                        ScrollerDirection.Vertical.Down -> {
                            lineOffset.floatValue = offset
                        }
                    }
                }
            }
        }
        else { // when not syncing scroller line with number
            when (style.scrollerDirection) {
                is ScrollerDirection.Horizontal -> { // keep line inside scroller width
                    lineOffset.floatValue = (lineOffset.floatValue + (dragAmount * (lineSpeed / 8)))
                        .coerceIn(-scrollerWidthPx / 2, scrollerWidthPx / 2)
                }

                is ScrollerDirection.Vertical -> { // keep line inside scroller height
                    lineOffset.floatValue = (lineOffset.floatValue + (dragAmount * (lineSpeed / 8)))
                        .coerceIn(-scrollerHeightPx / 2, scrollerHeightPx / 2)
                }
            }
        }
    }

    LaunchedEffect(startNumber) { // reposition line on init
        repositionLine(startNumber, 0f)
    }

    val updateNumber: (Float) -> Unit = { dragAmount ->
        totalDrag += dragAmount // calculate total drag distance

        repositionLine(number, dragAmount)

        // get direction of drag and update scroller number accordingly
        when {
            totalDrag <= -scrollDistanceFactor -> { // scrolling up/left
                when (style.scrollerDirection) {
                    is ScrollerDirection.Vertical -> {
                        when (style.scrollerDirection) {
                            is ScrollerDirection.Vertical.Up -> {
                                number = (number + step).coerceAtMost(max) // increment without exceeding min
                            }
                            is ScrollerDirection.Vertical.Down -> {
                                number = (number - step).coerceAtLeast(min) // decrement without exceeding min
                            }
                        }
                    }
                    is ScrollerDirection.Horizontal -> {
                        when (style.scrollerDirection) {
                            is ScrollerDirection.Horizontal.Right -> {
                                number = (number - step).coerceAtLeast(min) // decrement without exceeding min
                            }
                            is ScrollerDirection.Horizontal.Left -> {
                                number = (number + step).coerceAtMost(max) // increment without exceeding min
                            }
                        }
                    }
                }
                totalDrag = 0f
            }

            totalDrag >= scrollDistanceFactor -> { // scrolling down/right
                when (style.scrollerDirection) { // is set to top (increment by scroll up)
                    is ScrollerDirection.Vertical -> {
                        when (style.scrollerDirection) {
                            is ScrollerDirection.Vertical.Up -> {
                                number = (number - step).coerceAtLeast(min) // decrement without exceeding min
                            }
                            is ScrollerDirection.Vertical.Down -> {
                                number = (number + step).coerceAtMost(max) // increment without exceeding min
                            }
                        }
                    }
                    is ScrollerDirection.Horizontal -> {
                        when (style.scrollerDirection) {
                            is ScrollerDirection.Horizontal.Right -> {
                                number = (number + step).coerceAtMost(max) // increment without exceeding min
                            }
                            is ScrollerDirection.Horizontal.Left -> {
                                number = (number - step).coerceAtLeast(min) // decrement without exceeding min
                            }
                        }
                    }
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
                    NumberText(style, step, number)
                    Spacer(Modifier.width(style.numberDistanceToScroller))
                    ScrollerBox(style, lineOffset, syncLinePosWithNumber, style.scrollerDirection, onDragEnd = { onDragEnd(number) }, updateNumber)
                } else {
                    ScrollerBox(style, lineOffset, syncLinePosWithNumber, style.scrollerDirection, onDragEnd = { onDragEnd(number) }, updateNumber)
                    Spacer(Modifier.width(style.numberDistanceToScroller))
                    NumberText(style, step, number)
                }
            }
        }

        is NumberPosition.Top, is NumberPosition.Bottom -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (style.numberPosition is NumberPosition.Top) Arrangement.Top else Arrangement.Bottom
            ) {
                if (style.numberPosition is NumberPosition.Top) { // place text to top/bottom of scroller
                    NumberText(style, step, number)
                    Spacer(Modifier.height(style.numberDistanceToScroller))
                    ScrollerBox(style, lineOffset, syncLinePosWithNumber, style.scrollerDirection, onDragEnd = { onDragEnd(number) }, updateNumber)
                } else {
                    ScrollerBox(style, lineOffset, syncLinePosWithNumber, style.scrollerDirection, onDragEnd = { onDragEnd(number) }, updateNumber)
                    Spacer(Modifier.height(style.numberDistanceToScroller))
                    NumberText(style, step, number)
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun NumberText(style: ScrollerStyle, step: Float, number: Float) {
    val formattedNumber = String.format("%.${getDecimalPlaces(step)}f", number)

    Text(
        text = "${truncateTrailingZeros(formattedNumber)}",
        fontSize = style.numberFontSize,
        fontWeight = FontWeight.Bold,
        color = style.numberColor,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun ScrollerBox(
    style: ScrollerStyle,
    lineOffset: MutableState<Float>,
    syncLinePosWithNumber: Boolean,
    scrollerDirection: ScrollerDirection,
    onDragEnd: () -> Unit,
    updateNumber: (Float) -> Unit
) {
    Box(
        modifier = Modifier
            .width(style.scrollerWidth)
            .height(style.scrollerHeight)
            .clip(style.scrollerRounding)
            .background(style.scrollerColor)
            .pointerInput(Unit) {
                when (scrollerDirection) {
                    is ScrollerDirection.Horizontal -> {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (!syncLinePosWithNumber) lineOffset.value = 0f
                                onDragEnd()
                            }
                        ) { _, dragAmount -> updateNumber(dragAmount) }
                    }
                    is ScrollerDirection.Vertical -> {
                        detectVerticalDragGestures(
                            onDragEnd = {
                                if (!syncLinePosWithNumber) lineOffset.value = 0f
                                onDragEnd()
                            }
                        ) { _, dragAmount -> updateNumber(dragAmount) }
                    }
                }
            }
    ) {
        Box( // Scroller Line
            modifier = Modifier
                .then(
                    when (scrollerDirection) {
                        is ScrollerDirection.Horizontal -> {
                            Modifier
                                .width(style.lineThickness)
                                .height(style.scrollerWidth * style.lineWidthFactor)
                                .offset { IntOffset(lineOffset.value.toInt(), 0) }
                        }
                        is ScrollerDirection.Vertical -> {
                            Modifier
                                .width(style.scrollerWidth * style.lineWidthFactor)
                                .height(style.lineThickness)
                                .offset { IntOffset(0, lineOffset.value.toInt()) }
                        }
                    }
                )
                .align(Alignment.Center)
                .clip(style.lineRounding)
                .background(style.lineColor)
        )
    }
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
