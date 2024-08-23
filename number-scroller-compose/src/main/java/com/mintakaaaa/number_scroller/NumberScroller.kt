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
import androidx.compose.runtime.MutableFloatState
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

/**
 * Data class representing the styling options for the [NumberScroller] component.
 *
 * @property scrollerHeight The height of the scroller, in density-independent pixels (dp). Default is 60.dp.
 * @property scrollerWidth The width of the scroller, in density-independent pixels (dp). Default is 40.dp.
 * @property scrollerColor The background color of the scroller. Default is Color.DarkGray.
 * @property lineColor The color of the scroller line. Default is Color.Gray.
 * @property numberColor The color of the number text. Default is Color.Black.
 * @property scrollerRounding The shape of the corners of the scroller. Default is a RoundedCornerShape with a 6.dp radius.
 * @property lineRounding The shape of the corners of the scroller line. Default is a RoundedCornerShape with a 4.dp radius.
 * @property lineThickness The thickness of the scroller line, in density-independent pixels (dp). Default is 4.dp.
 * @property lineWidthFactor The proportion of the scroller width that the line occupies. Default is 0.8f.
 * @property numberFontSize The font size of the number text. Default is 30.sp.
 * @property numberDistanceToScroller The distance between the number text and the scroller, in density-independent pixels (dp). Default is 30.dp.
 * @property numberPosition The position of the number relative to the scroller. Default is [NumberPosition.Left].
 * @property scrollerDirection The direction in which the scroller operates. Default is [ScrollerDirection.VerticalUp].
 */
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
    val scrollerDirection: ScrollerDirection = ScrollerDirection.VerticalUp,
)

/**
 *  * Data class representing the behaviour options for the [NumberScroller] component.
 *
 * @param startNumber The initial value of the number displayed by the scroller. Default is 0f.
 * @param step The amount by which the number is incremented or decremented with each drag gesture. Default is 1f.
 * @param range The range of values that the number can be set to. Default is -10f to 10f.
 * @param scrollDistanceFactor The distance the user must drag to trigger a number change. Default is 100f.
 * @param lineSpeed The speed factor for scrolling line movement. Default is 1.5f.
 * @param syncLinePosWithNumber Whether to synchronize the position of the scroller line with the number value. Default is true.
**/
data class ScrollerBehaviour(
    val startNumber: Float = 0f,
    val step: Float = 1f,
    val range: ClosedFloatingPointRange<Float> = -10f..10f,
    val scrollDistanceFactor: Float = 100f,
    val lineSpeed: Float = 1.5f,
    val syncLinePosWithNumber: Boolean = true,
)

/**
 * Enum representing the possible positions of the number relative to the scroller.
 */
enum class NumberPosition {
    Above, Below, Left, Right
}

/**
 * Enum representing the possible directions in which the scroller can operate.
 */
enum class ScrollerDirection {
    VerticalUp, VerticalDown, HorizontalLeft, HorizontalRight
}

/**
 * Composable function that displays a number scroller UI component.
 *
 * The [NumberScroller] allows users to scroll a number up/down/left/right based on drag gestures.
 *
 * @param style The styling options for the scroller. Default is [ScrollerStyle].
 * @param behaviour The behaviour options for the scroller. Default is [ScrollerBehaviour].
 * @param onDragEnd Callback function to be called when the drag operation ends, with the current number as the parameter.
 */
@Composable
fun NumberScroller(
    style: ScrollerStyle = ScrollerStyle(),
    behaviour: ScrollerBehaviour = ScrollerBehaviour(),
    onDragEnd: (Float) -> Unit = {})
{

    // CRUCIAL: Convert dp to px for accurate positioning
    val scrollerHeightPx = with(LocalDensity.current) { style.scrollerHeight.toPx() }
    val scrollerWidthPx = with(LocalDensity.current) { style.scrollerWidth.toPx() }

    var number by remember { mutableFloatStateOf(behaviour.startNumber) }
    val lineOffset = remember { mutableFloatStateOf(0f) }

    val repositionLineByNumber: () -> Unit = {
        val normalizedValue = (number - behaviour.range.start) / (behaviour.range.endInclusive - behaviour.range.start)
        val dimensionPx = if (style.scrollerDirection in listOf(ScrollerDirection.HorizontalLeft, ScrollerDirection.HorizontalRight)) {
            scrollerWidthPx // confine scroller line within scroller WIDTH
        } else {
            scrollerHeightPx // confine scroller line within scroller HEIGHT
        }

        val offset = (normalizedValue * dimensionPx - dimensionPx / 2)
        lineOffset.floatValue = when (style.scrollerDirection) {
            ScrollerDirection.HorizontalLeft, ScrollerDirection.VerticalUp -> -offset
            ScrollerDirection.HorizontalRight, ScrollerDirection.VerticalDown -> offset
        }
    }

    val repositionLineByDrag: (Float) -> Unit = { dragAmount ->
        val dimensionPx = if (style.scrollerDirection in listOf(ScrollerDirection.HorizontalLeft, ScrollerDirection.HorizontalRight)) {
            scrollerWidthPx // confine scroller line within scroller WIDTH
        } else {
            scrollerHeightPx // confine scroller line within scroller HEIGHT
        }

        lineOffset.floatValue = (lineOffset.floatValue + (dragAmount * (behaviour.lineSpeed / 8)))
            .coerceIn(-dimensionPx / 2, dimensionPx / 2) // confine line within dimension selected
    }

    if (behaviour.syncLinePosWithNumber) repositionLineByNumber()
    else repositionLineByDrag(0f)

    val updateNumber: (Float) -> Unit = { totalDrag ->
        number = when (style.scrollerDirection) {
            ScrollerDirection.VerticalUp, ScrollerDirection.HorizontalLeft -> {
                if (totalDrag <= -behaviour.scrollDistanceFactor) { // dragging up/left past threshold
                    (number + behaviour.step).coerceAtMost(behaviour.range.endInclusive)
                } else { // dragging down/right past threshold
                    (number - behaviour.step).coerceAtLeast(behaviour.range.start)
                }
            }

            ScrollerDirection.VerticalDown, ScrollerDirection.HorizontalRight -> {
                if (totalDrag <= -behaviour.scrollDistanceFactor) { // dragging up/left past threshold
                    (number - behaviour.step).coerceAtLeast(behaviour.range.start)
                } else { // dragging down/right past threshold
                    (number + behaviour.step).coerceAtMost(behaviour.range.endInclusive)
                }
            }
        }
    }

    when (style.numberPosition) {
        NumberPosition.Left, NumberPosition.Right -> { // place text to left/right of scroller
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (style.numberPosition == NumberPosition.Left) Arrangement.Start else Arrangement.End
            ) {
                if (style.numberPosition == NumberPosition.Left) {
                    NumberText(style, behaviour.step, number)
                    Spacer(Modifier.width(style.numberDistanceToScroller))
                    ScrollerBox(style, behaviour, lineOffset, onDragEnd = { onDragEnd(number) }, updateNumber, repositionLineByDrag, repositionLineByNumber)
                } else {
                    ScrollerBox(style, behaviour, lineOffset, onDragEnd = { onDragEnd(number) }, updateNumber, repositionLineByDrag, repositionLineByNumber)
                    Spacer(Modifier.width(style.numberDistanceToScroller))
                    NumberText(style, behaviour.step, number)
                }
            }
        }

        NumberPosition.Above, NumberPosition.Below -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = if (style.numberPosition == NumberPosition.Above) Arrangement.Top else Arrangement.Bottom
            ) {
                if (style.numberPosition == NumberPosition.Above) { // place text to top/below of scroller
                    NumberText(style, behaviour.step, number)
                    Spacer(Modifier.height(style.numberDistanceToScroller))
                    ScrollerBox(style, behaviour, lineOffset, onDragEnd = { onDragEnd(number) }, updateNumber, repositionLineByDrag, repositionLineByNumber)
                } else {
                    ScrollerBox(style, behaviour, lineOffset, onDragEnd = { onDragEnd(number) }, updateNumber, repositionLineByDrag, repositionLineByNumber)
                    Spacer(Modifier.height(style.numberDistanceToScroller))
                    NumberText(style, behaviour.step, number)
                }
            }
        }
    }
}

/**
 * Composable function that displays the number text with styling.
 *
 * @param style The styling options for the number text.
 * @param step The step value for formatting the number.
 * @param number The current number to be displayed.
 */
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

/**
 * Composable function that displays the scroller box with a draggable line.
 *
 * @param style The styling options for the scroller box.
 * @param behaviour The behaviour options for the scroller box.
 * @param lineOffset The current offset of the scroller line.
 * @param onDragEnd Callback function to be called when the drag operation ends.
 * @param updateNumber Function to update the number based on drag amount.
 * @param repositionLineByDrag A function to reposition the scroller line based on drag amount.
 * @param repositionLineByNumber A function to reposition the scroller line based on the current number value.
 */
@Composable
fun ScrollerBox(
    style: ScrollerStyle,
    behaviour: ScrollerBehaviour,
    lineOffset: MutableState<Float>,
    onDragEnd: () -> Unit,
    updateNumber: (Float) -> Unit,
    repositionLineByDrag: (Float) -> Unit,
    repositionLineByNumber: () -> Unit,
) {
    var totalDrag by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .width(style.scrollerWidth)
            .height(style.scrollerHeight)
            .clip(style.scrollerRounding)
            .background(style.scrollerColor)
            .pointerInput(Unit) {
                when (style.scrollerDirection) {
                    ScrollerDirection.HorizontalRight, ScrollerDirection.HorizontalLeft -> {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (!behaviour.syncLinePosWithNumber) lineOffset.value = 0f
                                onDragEnd()
                            }
                        ) { _, dragAmount ->
                            totalDrag += dragAmount // calculate total drag distance
                            if (totalDrag <= -behaviour.scrollDistanceFactor || totalDrag >= behaviour.scrollDistanceFactor) {
                                updateNumber(totalDrag)
                                totalDrag = 0f
                            }
                            if (behaviour.syncLinePosWithNumber) repositionLineByNumber()
                            else repositionLineByDrag(dragAmount)
                        }
                    }

                    ScrollerDirection.VerticalUp, ScrollerDirection.VerticalDown -> {
                        detectVerticalDragGestures(
                            onDragEnd = {
                                if (!behaviour.syncLinePosWithNumber) lineOffset.value = 0f
                                onDragEnd()
                            }
                        ) { _, dragAmount ->
                            totalDrag += dragAmount // calculate total drag distance
                            if (totalDrag <= -behaviour.scrollDistanceFactor || totalDrag >= behaviour.scrollDistanceFactor) {
                                updateNumber(totalDrag)
                                totalDrag = 0f
                            }
                            if (behaviour.syncLinePosWithNumber) repositionLineByNumber()
                            else repositionLineByDrag(dragAmount)
                        }
                    }
                }
            }
    ) {
        Box( // Scroller Line
            modifier = Modifier
                .then(
                    when (style.scrollerDirection) {
                        ScrollerDirection.HorizontalRight, ScrollerDirection.HorizontalLeft -> {
                            Modifier
                                .width(style.lineThickness)
                                .height(style.scrollerWidth * style.lineWidthFactor)
                                .offset { IntOffset(lineOffset.value.toInt(), 0) }
                        }

                        ScrollerDirection.VerticalUp, ScrollerDirection.VerticalDown -> {
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

/**
 * Gets the number of decimal places in a float value based on the step size.
 *
 * @param value The float value for which to get the number of decimal places.
 * @return The number of decimal places in the value.
 */
fun getDecimalPlaces(value: Float): Int {
    val valueString = value.toString()
    return if (valueString.contains('.')) {
        valueString.substringAfter('.').length
    } else 0
}

/**
 * Truncates trailing zeros from a string representation of a number.
 *
 * @param x The string representation of the number.
 * @return The number with trailing zeros removed.
 */
fun truncateTrailingZeros(x: String): Number {
    val result = x.toBigDecimal().stripTrailingZeros()
    return if (result.scale() <= 0) result.toInt() else result.toFloat()
}
