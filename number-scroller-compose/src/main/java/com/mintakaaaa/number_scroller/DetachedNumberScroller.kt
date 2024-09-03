package com.mintakaaaa.number_scroller

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
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
 * Data class representing the styling options for the [DetachedNumberScroller] component.
 *
 * @property scrollerHeight The height of the scroller, in density-independent pixels (dp). Default is 60.dp.
 * @property scrollerWidth The width of the scroller, in density-independent pixels (dp). Default is 40.dp.
 * @property scrollerColor The background color of the scroller. Default is Color.DarkGray.
 * @property lineColor The color of the scroller line. Default is Color.Gray.
 * @property scrollerRounding The shape of the corners of the scroller. Default is a RoundedCornerShape with a 6.dp radius.
 * @property lineRounding The shape of the corners of the scroller line. Default is a RoundedCornerShape with a 4.dp radius.
 * @property lineThickness The thickness of the scroller line, in density-independent pixels (dp). Default is 4.dp.
 * @property lineWidthFactor The proportion of the scroller width that the line occupies. Default is 0.8f.
 * @property scrollerDirection The direction in which the scroller operates. Default is [ScrollerDirection.VerticalUp].
 */
data class DetachedScrollerStyle(
    val scrollerHeight: Dp = 60.dp,
    val scrollerWidth: Dp = 40.dp,
    val scrollerColor: Color = Color.DarkGray,
    val lineColor: Color = Color.Gray,
    val scrollerRounding: RoundedCornerShape = RoundedCornerShape(6.dp),
    val lineRounding: RoundedCornerShape = RoundedCornerShape(4.dp),
    val lineThickness: Dp = 4.dp,
    val lineWidthFactor: Float = 0.8f,
    val scrollerDirection: ScrollerDirection = ScrollerDirection.VerticalUp,
)

/**
 * Data class representing the behaviour options for the [ScrollerTarget] component.
 * @property startNumber The initial value of the number displayed by the scroller. Default is 0f.
 * @property step The amount by which the number is incremented or decremented with each drag gesture. Default is 1f.
 * @property range The range of values that the number can be set to. Default is -10f to 10f.
 */
data class TargetBehaviour(
    val startNumber: Float = 0f,
    val step: Float = 1f,
    val range: ClosedFloatingPointRange<Float> = -10f..10f,
)

/**
 * Data class representing the behaviour options for the [DetachedNumberScroller] component.
 * @property scrollDistanceFactor The distance the user must drag to trigger a number change. Default is 100f.
 * @property lineSpeed The speed factor for scrolling line movement. Default is 1.5f.
 * @property syncLinePosWithNumber Whether to synchronize the position of the scroller line with the number value. Default is true.
 */
data class DetachedScrollerBehaviour(
    val scrollDistanceFactor: Float = 100f,
    val lineSpeed: Float = 1.5f,
    val syncLinePosWithNumber: Boolean = true,
)

/**
 * Data class representing the styling options for the [ScrollerTarget] component.
 *
 * @property numberColor The color of the number displayed in the target. Default is Color.Black.
 * @property numberFontSize The font size of the number, in scaled pixels (sp). Default is 30.sp.
 * @property boxPadding The padding around the number within the target box, in density-independent pixels (dp). Default is 5.dp.
 * @property boxWidth The width of the target box, in density-independent pixels (dp). Default is 50.dp.
 * @property boxHeight The height of the target box, in density-independent pixels (dp). Default is 50.dp.
 * @property background The background color of the target box. Default is Color.LightGray.
 * @property selectedBackground The background color of the target box when selected. Default is Color.Green.
 * @property boxRounding The rounding of the corners of the target box. Default is a RoundedCornerShape with 0.dp radius.
 */
data class TargetStyle(
    val numberColor: Color = Color.Black,
    val numberFontSize: TextUnit = 25.sp,
    val boxPadding: Dp = 5.dp,
    val boxWidth: Dp = 50.dp,
    val boxHeight: Dp = 50.dp,
    val background: Color = Color.LightGray,
    val selectedBackground: Color = Color.Green,
    val boxRounding: RoundedCornerShape = RoundedCornerShape(0.dp),
)

class ScrollerController(
    val style: DetachedScrollerStyle = DetachedScrollerStyle(),
    val targetStyle: TargetStyle = TargetStyle(),
    val scrollerBehaviour: DetachedScrollerBehaviour = DetachedScrollerBehaviour(),
    val defaultTargetBehaviour: TargetBehaviour? = TargetBehaviour()
) {
    val targets = mutableMapOf<Int, Target>() // all scroller targets

    var selectedTargetId: Int? by mutableStateOf(null)
        private set // set selected target by ID if one is found, else null

    data class Target( // logical equivalent of ScrollerTarget composable
        val state: MutableFloatState,
        val behaviour: TargetBehaviour,
        val onDragEnd: ((Float) -> Unit)?
    )

    fun registerTarget(id: Int, behaviour: TargetBehaviour, onDragEnd: ((Float) -> Unit)? = null): MutableFloatState {
        // register a target and return its state
        val state = mutableFloatStateOf(behaviour.startNumber)
        targets[id] = Target(state, behaviour, onDragEnd)
        return state
    }

    fun linkScrollerTo(vararg ids: Int) { // link scroller to specific targets by ID
        selectedTargetId = ids.firstOrNull { targets.containsKey(it) }
    }

    fun getTarget(id: Int): Target? { // get target state by ID
        return targets[id]
    }

    fun selectTarget(id: Int) { // set selected target by ID
        if (targets.containsKey(id)) {
            selectedTargetId = id
        }
    }

    fun updateSelectedTarget(totalDrag: Float) { // update selected target number based on scroll
        selectedTargetId?.let { id ->
            targets[id]?.let { target ->
                target.state.floatValue = when (style.scrollerDirection) {
                    ScrollerDirection.VerticalUp, ScrollerDirection.HorizontalLeft -> {
                        if (totalDrag <= -scrollerBehaviour.scrollDistanceFactor) { // dragging up/left past threshold
                            (target.state.floatValue + target.behaviour.step).coerceAtMost(target.behaviour.range.endInclusive)
                        } else { // dragging down/right past threshold
                            (target.state.floatValue - target.behaviour.step).coerceAtLeast(target.behaviour.range.start)
                        }
                    }

                    ScrollerDirection.VerticalDown, ScrollerDirection.HorizontalRight -> {
                        if (totalDrag <= -scrollerBehaviour.scrollDistanceFactor) { // dragging up/left past threshold
                            (target.state.floatValue - target.behaviour.step).coerceAtLeast(target.behaviour.range.start)
                        } else { // dragging down/right past threshold
                            (target.state.floatValue + target.behaviour.step).coerceAtMost(target.behaviour.range.endInclusive)
                        }
                    }
                }
            }
        }
    }

    fun triggerDragEnd() { // trigger onDragEnd callback for selected target
        selectedTargetId?.let { id ->
            targets[id]?.let { target ->
                target.onDragEnd?.invoke(target.state.floatValue)
            }
        }
    }
}


@Composable
fun ScrollerTarget(controller: ScrollerController, targetBehaviour: TargetBehaviour? = null, id: Int, onDragEnd: (Float) -> Unit = {}) {

    // if behaviour for this target is passed, use it, else use default target behaviour
    val effectiveTargetBehaviour = targetBehaviour ?: controller.defaultTargetBehaviour!!

    // register target and get its state
    val targetState = remember { controller.registerTarget(id, effectiveTargetBehaviour, onDragEnd) }
    val isSelected = controller.selectedTargetId == id

    Box(
        Modifier
            .clip(controller.targetStyle.boxRounding)
            .width(controller.targetStyle.boxWidth)
            .height(controller.targetStyle.boxHeight)
            .background(
                if (isSelected) controller.targetStyle.selectedBackground
                else controller.targetStyle.background
            )
            .padding(controller.targetStyle.boxPadding)
            .clickable { controller.selectTarget(id) },
        contentAlignment = Alignment.Center
    ) {
        NumberText(controller.targetStyle, step = effectiveTargetBehaviour.step, number = targetState.floatValue)
    }
}

@Composable
fun DetachedNumberScroller(controller: ScrollerController, linkedTo: List<Int>) {
    LaunchedEffect(controller) {
        controller.linkScrollerTo(*linkedTo.toIntArray())
    }

    var selectedTargetId: Int
    var selectedTarget: ScrollerController.Target

    val scrollerHeightPx = with(LocalDensity.current) { controller.style.scrollerHeight.toPx() }
    val scrollerWidthPx = with(LocalDensity.current) { controller.style.scrollerWidth.toPx() }

    val lineOffset = remember { mutableFloatStateOf(0f) }

    val repositionLineByNumber: () -> Unit = {
        controller.selectedTargetId?.let { // if target is selected, reposition line
            selectedTargetId = controller.selectedTargetId!!
            selectedTarget = controller.getTarget(selectedTargetId)!!

            val normalizedValue = (selectedTarget.state.floatValue - selectedTarget.behaviour.range.start) / (selectedTarget.behaviour.range.endInclusive - selectedTarget.behaviour.range.start)
            val dimensionPx = if (controller.style.scrollerDirection in listOf(ScrollerDirection.HorizontalLeft, ScrollerDirection.HorizontalRight)) {
                scrollerWidthPx // confine scroller line within scroller WIDTH
            } else {
                scrollerHeightPx // confine scroller line within scroller HEIGHT
            }

            val offset = (normalizedValue * dimensionPx - dimensionPx / 2)
            lineOffset.floatValue = when (controller.style.scrollerDirection) {
                ScrollerDirection.HorizontalLeft, ScrollerDirection.VerticalUp -> -offset
                ScrollerDirection.HorizontalRight, ScrollerDirection.VerticalDown -> offset
            }
        }
    }

    val repositionLineByDrag: (Float) -> Unit = { dragAmount ->
        val dimensionPx = if (controller.style.scrollerDirection in listOf(ScrollerDirection.HorizontalLeft, ScrollerDirection.HorizontalRight)) {
            scrollerWidthPx // confine scroller line within scroller WIDTH
        } else {
            scrollerHeightPx // confine scroller line within scroller HEIGHT
        }

        lineOffset.floatValue = (lineOffset.floatValue + (dragAmount * (controller.scrollerBehaviour.lineSpeed / 8)))
            .coerceIn(-dimensionPx / 2, dimensionPx / 2) // confine line within dimension selected
    }

    if (controller.scrollerBehaviour.syncLinePosWithNumber) repositionLineByNumber()
    else repositionLineByDrag(0f)

    val updateNumber: (Float) -> Unit = { totalDrag ->
        controller.updateSelectedTarget(totalDrag)
    }

    ScrollerBox(updateNumber, repositionLineByDrag, repositionLineByNumber, lineOffset, controller)
}


@Composable
fun ScrollerBox(
    updateNumber: (Float) -> Unit,
    repositionLineByDrag: (Float) -> Unit,
    repositionLineByNumber: () -> Unit,
    lineOffset: MutableState<Float>,
    controller: ScrollerController,
    ) {
    var totalDrag by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .width(controller.style.scrollerWidth)
            .height(controller.style.scrollerHeight)
            .clip(controller.style.scrollerRounding)
            .background(controller.style.scrollerColor)
            .pointerInput(Unit) {
                when (controller.style.scrollerDirection) {
                    ScrollerDirection.HorizontalRight, ScrollerDirection.HorizontalLeft -> {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (!controller.scrollerBehaviour.syncLinePosWithNumber) lineOffset.value = 0f
                                controller.triggerDragEnd()
                            }
                        ) { _, dragAmount ->
                            totalDrag += dragAmount // calculate total drag distance
                            if (totalDrag <= -controller.scrollerBehaviour.scrollDistanceFactor || totalDrag >= controller.scrollerBehaviour.scrollDistanceFactor) {
                                updateNumber(totalDrag)
                                totalDrag = 0f
                            }
                            if (controller.scrollerBehaviour.syncLinePosWithNumber) repositionLineByNumber()
                            else repositionLineByDrag(dragAmount)
                        }
                    }

                    ScrollerDirection.VerticalUp, ScrollerDirection.VerticalDown -> {
                        detectVerticalDragGestures(
                            onDragEnd = {
                                if (!controller.scrollerBehaviour.syncLinePosWithNumber) lineOffset.value = 0f
                                controller.triggerDragEnd()
                            }
                        ) { _, dragAmount ->
                            totalDrag += dragAmount // calculate total drag distance
                            if (totalDrag <= -controller.scrollerBehaviour.scrollDistanceFactor || totalDrag >= controller.scrollerBehaviour.scrollDistanceFactor) {
                                updateNumber(totalDrag)
                                totalDrag = 0f
                            }
                            if (controller.scrollerBehaviour.syncLinePosWithNumber) repositionLineByNumber()
                            else repositionLineByDrag(dragAmount)
                        }
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .then(
                    when (controller.style.scrollerDirection) {
                        ScrollerDirection.HorizontalRight, ScrollerDirection.HorizontalLeft -> {
                            Modifier
                                .width(controller.style.lineThickness)
                                .height(controller.style.scrollerHeight * controller.style.lineWidthFactor)
                                .offset { IntOffset(lineOffset.value.toInt(), 0) }
                        }

                        ScrollerDirection.VerticalUp, ScrollerDirection.VerticalDown -> {
                            Modifier
                                .width(controller.style.scrollerWidth * controller.style.lineWidthFactor)
                                .height(controller.style.lineThickness)
                                .offset { IntOffset(0, lineOffset.value.toInt()) }
                        }
                    }
                )
                .align(Alignment.Center)
                .clip(controller.style.lineRounding)
                .background(controller.style.lineColor)

        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun NumberText(targetStyle: TargetStyle, step: Float, number: Float) {
    val formattedNumber = String.format("%.${getDecimalPlaces(step)}f", number)

    Text(
        text = "${truncateTrailingZeros(formattedNumber)}",
        fontSize = targetStyle.numberFontSize,
        fontWeight = FontWeight.Bold,
        color = targetStyle.numberColor,
        textAlign = TextAlign.Center,
    )
}