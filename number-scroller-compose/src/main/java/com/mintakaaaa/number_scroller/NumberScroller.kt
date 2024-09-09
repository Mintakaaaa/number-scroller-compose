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
import androidx.compose.ui.text.font.FontFamily
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
 * @property numberFontFamily The font family of the number. Default is FontFamily.SansSerif.
 * @property numberFontWeight The font weight of the number. Default is FontWeight.Bold.
 * @property numberDistanceToScroller The distance between the number text and the scroller, in density-independent pixels (dp). Default is 30.dp.
 * @property numberPosition The position of the number relative to the scroller. Default is [NumberPosition.Left].
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
    val numberFontFamily: FontFamily = FontFamily.SansSerif,
    val numberFontWeight: FontWeight = FontWeight.Bold,
    val numberDistanceToScroller: Dp = 30.dp,
    val numberPosition: NumberPosition = NumberPosition.Left,
)

/**
 *  * Data class representing the behaviour options for the [NumberScroller] component.
 *
 * @property startNumber The initial value of the number displayed by the scroller. Default is 0f.
 * @property step The amount by which the number is incremented or decremented with each drag gesture. Default is 1f.
 * @property range The range of values that the number can be set to. Default is -10f to 10f.
 * @property scrollDistanceFactor The distance the user must drag to trigger a number change. Default is 100f.
 * @property lineSpeed The speed factor for scrolling line movement. Default is 1.5f.
 * @property syncLinePosWithNumber Whether to synchronize the position of the scroller line with the number value. Default is true.
 * @property incrementDirection The direction in which the scroller increments. Default is [IncrementDirection.Up].
 **/
data class ScrollerBehaviour(
    val startNumber: Float = 0f,
    val step: Float = 1f,
    val range: ClosedFloatingPointRange<Float> = -10f..10f,
    val scrollDistanceFactor: Float = 100f,
    val lineSpeed: Float = 1.5f,
    val syncLinePosWithNumber: Boolean = true,
    val incrementDirection: IncrementDirection = IncrementDirection.Up,
)

/**
 * Enum representing the possible positions of the number relative to the scroller.
 */
enum class NumberPosition {
    Above, Below, Left, Right
}

/**
 * Enum representing the possible directions in which the scroller can increment.
 */
enum class IncrementDirection {
    Up, Down, Left, Right
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
        val dimensionPx = if (behaviour.incrementDirection in listOf(IncrementDirection.Left, IncrementDirection.Right)) {
            scrollerWidthPx // confine scroller line within scroller WIDTH
        } else {
            scrollerHeightPx // confine scroller line within scroller HEIGHT
        }

        val offset = (normalizedValue * dimensionPx - dimensionPx / 2)
        lineOffset.floatValue = when (behaviour.incrementDirection) {
            IncrementDirection.Left, IncrementDirection.Up -> -offset
            IncrementDirection.Right, IncrementDirection.Down -> offset
        }
    }

    val repositionLineByDrag: (Float) -> Unit = { dragAmount ->
        val dimensionPx = if (behaviour.incrementDirection in listOf(IncrementDirection.Left, IncrementDirection.Right)) {
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
        number = when (behaviour.incrementDirection) {
            IncrementDirection.Up, IncrementDirection.Left -> {
                if (totalDrag <= -behaviour.scrollDistanceFactor) { // dragging up/left past threshold
                    (number + behaviour.step).coerceAtMost(behaviour.range.endInclusive)
                } else { // dragging down/right past threshold
                    (number - behaviour.step).coerceAtLeast(behaviour.range.start)
                }
            }

            IncrementDirection.Down, IncrementDirection.Right -> {
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
        fontFamily = style.numberFontFamily,
        fontSize = style.numberFontSize,
        fontWeight = style.numberFontWeight,
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
                when (behaviour.incrementDirection) {
                    IncrementDirection.Left, IncrementDirection.Right -> {
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

                    IncrementDirection.Up, IncrementDirection.Down -> {
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
                    when (behaviour.incrementDirection) {
                        IncrementDirection.Left, IncrementDirection.Right -> {
                            Modifier
                                .width(style.lineThickness)
                                .height(style.scrollerHeight * style.lineWidthFactor)
                                .offset { IntOffset(lineOffset.value.toInt(), 0) }
                        }

                        IncrementDirection.Up, IncrementDirection.Down -> {
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

