package com.mintakaaaa.number_scroller

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

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
)

/**
 * Data class representing the behaviour options for the [ScrollerTarget] component.
 * @property startNumber The initial value of the number displayed by the scroller. Default is 0f.
 * @property step The amount by which the number is incremented or decremented with each drag gesture. Default is 1f.
 * @property range The range of values that the number can be set to. Default is -10f to 10f.
 * @property scrollDistanceFactor The distance the user must drag to trigger a number change. Default is 100f.
 * @property useDynamicDistanceFactor Whether to apply a dynamic scroll distance factor that speeds up incrementing/decrementing of selected target number. Default is false.
 * @property dynamicDistanceScalingFactor The factor by which to speed up the incrementing/decrementing of selected target number. Default is 4f.
 * @property autoIncrementOnFarScroll Whether to increment the selected target automatically once a threshold is passed. Default is true.
 * @property farScrollThreshold The threshold that must be passed to activate auto incrementation of selected target. Default is 0.99f.
 * @property autoIncrementDelay The delay between automatic incrementing/decrementing of the selected target number. Default is 100.
 * @property doubleTapToEdit Whether double-tapping a target should enable editing mode for that target. Default is false.
 */
data class TargetBehaviour(
    val startNumber: Float = 0f,
    val step: Float = 1f,
    val range: ClosedFloatingPointRange<Float> = -10f..10f,
    val scrollDistanceFactor: Float = 100f,
    val useDynamicDistanceFactor: Boolean = false,
    val dynamicDistanceScalingFactor: Float = 4f,
    val autoIncrementOnFarScroll: Boolean = false,
    val farScrollThreshold: Float = 0.99f,
    val autoIncrementDelay: Long = 100,
    val doubleTapToEdit: Boolean = false,
)

/**
 * Data class representing the behaviour options for the [DetachedNumberScroller] component.
 * @property lineSpeed The speed factor for scrolling line movement. Default is 1.5f.
 * @property syncLinePosWithNumber Whether to synchronize the position of the scroller line with the number value. Default is true.
 * @property incrementDirection The direction in which the scroller increments. Default is [IncrementDirection.Up].
 */
data class DetachedScrollerBehaviour(
    val lineSpeed: Float = 1.5f,
    val syncLinePosWithNumber: Boolean = true,
    val incrementDirection: IncrementDirection = IncrementDirection.Up,
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
    val scrollerStyle: DetachedScrollerStyle = DetachedScrollerStyle(),
    val targetStyle: TargetStyle = TargetStyle(),
    val scrollerBehaviour: DetachedScrollerBehaviour = DetachedScrollerBehaviour(),
    val defaultTargetBehaviour: TargetBehaviour? = TargetBehaviour()
) {
    val targets = mutableMapOf<Int, Target>() // all scroller targets

    var selectedTargetId: Int? by mutableStateOf(null)
        private set // set selected target by ID if one is found, else null

    // ---------------------------------------------------------------------------------------------
    // TARGET EDIT VARS / FUNCS
    var editableTargetId by mutableStateOf<Int?>(null) // ID of currently editable target

    var textFieldValue by mutableStateOf(TextFieldValue("")) // shared basic text field value
    var isEditingTarget by mutableStateOf(false) // global target editing state
    val focusRequester = FocusRequester() // shared focus requester for KB

    fun startEditingTarget(targetId: Int, initialValue: Float) {
        // if ends with .0, convert to Int, else, leave as is.
        val formattedValue = if (initialValue % 1 == 0f) initialValue.toInt().toString()
        else initialValue.toString()

        editableTargetId = targetId
        textFieldValue = TextFieldValue(formattedValue)
        isEditingTarget = true
    }

    fun stopEditingTarget() {
        editableTargetId = null
        isEditingTarget = false
    }
    // ---------------------------------------------------------------------------------------------

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

    fun updateSelectedTarget(totalDrag: Float, scrollFactorMultiplier: Float) { // update selected target number based on scroll
        selectedTargetId?.let { id ->
            targets[id]?.let { target ->
                println("total drag: $totalDrag")

                target.state.floatValue = when (scrollerBehaviour.incrementDirection) {
                    IncrementDirection.Up, IncrementDirection.Left -> {
                        if (totalDrag <= -target.behaviour.scrollDistanceFactor * scrollFactorMultiplier) { // dragging up/left past threshold
                            (target.state.floatValue + target.behaviour.step).coerceAtMost(target.behaviour.range.endInclusive)
                        } else { // dragging down/right past threshold
                            (target.state.floatValue - target.behaviour.step).coerceAtLeast(target.behaviour.range.start)
                        }
                    }

                    IncrementDirection.Down, IncrementDirection.Right -> {
                        if (totalDrag <= -target.behaviour.scrollDistanceFactor * scrollFactorMultiplier) { // dragging up/left past threshold
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

/**
 * Composable function that displays a `ScrollerTarget` UI component.
 *
 * The [ScrollerTarget] is a target area that can be linked to a scroller and used to display a number.
 *
 * @param controller The controller managing the scroller and its targets.
 * @param targetBehaviour The behaviour options specific to this target. If null, the default behaviour from the controller will be used. Default is null.
 * @param id The unique identifier for this target.
 * @param onDragEnd Callback function called when the drag operation ends, with the current number as the parameter. Default is an empty function.
 */
@Composable
fun ScrollerTarget(controller: ScrollerController, targetBehaviour: TargetBehaviour? = null, id: Int, onDragEnd: (Float) -> Unit = {}) {
    // if behaviour for this target is passed, use it, else use default target behaviour
    val effectiveTargetBehaviour = targetBehaviour ?: controller.defaultTargetBehaviour!!

    require(!(effectiveTargetBehaviour.autoIncrementOnFarScroll && controller.scrollerBehaviour.syncLinePosWithNumber)) { "autoIncrementOnFarScroll cannot be true while syncLinePosWithNumber is true" }
    require(effectiveTargetBehaviour.farScrollThreshold in 0f..0.99f) { "farScrollThreshold must be between 0f and 0.99f" }
    require(effectiveTargetBehaviour.autoIncrementDelay >= 50) { "autoIncrementDelay must be greater or equal to 50" }

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
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (effectiveTargetBehaviour.doubleTapToEdit && !controller.isEditingTarget)
                            controller.startEditingTarget(id, targetState.floatValue)
                    },
                    onPress = {
                        if (!controller.isEditingTarget) controller.selectTarget(id)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (controller.editableTargetId == id && controller.isEditingTarget) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = controller.textFieldValue,
                    onValueChange = { newText -> controller.textFieldValue = newText },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            controller.stopEditingTarget()
                            // set new number to new text field value within TargetBehaviour.range
                            targetState.floatValue = controller.textFieldValue.text.toFloatOrNull()?.coerceIn(
                                effectiveTargetBehaviour.range.start,
                                effectiveTargetBehaviour.range.endInclusive
                            ) ?: targetState.floatValue // retain old number if parsing fails

                            controller.triggerDragEnd() // trigger callback to pass new target #
                        }
                    ),
                    modifier = Modifier.focusRequester(controller.focusRequester),
                    textStyle = TextStyle(fontWeight = FontWeight.Bold, color = controller.targetStyle.numberColor, textAlign = TextAlign.Center, fontSize = controller.targetStyle.numberFontSize)
                )
            }
        } else { // not editing; show number composable
            NumberText(controller.targetStyle, step = effectiveTargetBehaviour.step, number = targetState.floatValue)
        }

        // request focus and show keyboard on target-editing start
        LaunchedEffect(controller.isEditingTarget, controller.editableTargetId) {
            if (controller.isEditingTarget && controller.editableTargetId == id) {
                controller.textFieldValue = controller.textFieldValue.copy(selection = TextRange(controller.textFieldValue.text.length)) // move cursor to end
                controller.focusRequester.requestFocus()
            }
        }
    }
}

/**
 * Composable function that displays a number scroller UI component.
 *
 * The [DetachedScrollerBehaviour] allows users to scroll a number up/down/left/right based on drag gestures.
 *
 * @param controller The controller managing this scroller. Default is None.
 * @param linkedTo A list of target IDs to link the scroller to. Default is Empty list.
 */
@Composable
fun DetachedNumberScroller(controller: ScrollerController, linkedTo: List<Int>) {
    LaunchedEffect(controller) {
        controller.linkScrollerTo(*linkedTo.toIntArray())
    }

    var selectedTargetId: Int
    var selectedTarget: ScrollerController.Target

    val scrollerHeightPx = with(LocalDensity.current) { controller.scrollerStyle.scrollerHeight.toPx() }
    val scrollerWidthPx = with(LocalDensity.current) { controller.scrollerStyle.scrollerWidth.toPx() }

    val lineOffset = remember { mutableFloatStateOf(0f) }

    val repositionLineByNumber: () -> Unit = {
        controller.selectedTargetId?.let { // if target is selected, reposition line
            selectedTargetId = controller.selectedTargetId!!
            selectedTarget = controller.getTarget(selectedTargetId)!!

            val normalizedValue = (selectedTarget.state.floatValue - selectedTarget.behaviour.range.start) / (selectedTarget.behaviour.range.endInclusive - selectedTarget.behaviour.range.start)
            val dimensionPx = getDimensionPx(controller, scrollerWidthPx, scrollerHeightPx)

            val offset = (normalizedValue * dimensionPx - dimensionPx / 2)
            lineOffset.floatValue = when (controller.scrollerBehaviour.incrementDirection) {
                IncrementDirection.Left, IncrementDirection.Up -> -offset
                IncrementDirection.Right, IncrementDirection.Down -> offset
            }
        }
    }

    val repositionLineByDrag: (Float) -> Unit = { dragAmount ->
        val dimensionPx = getDimensionPx(controller, scrollerWidthPx, scrollerHeightPx)

        lineOffset.floatValue = (lineOffset.floatValue + (dragAmount * (controller.scrollerBehaviour.lineSpeed / 8)))
            .coerceIn(-dimensionPx / 2, dimensionPx / 2) // confine line within dimension selected
    }

    if (controller.scrollerBehaviour.syncLinePosWithNumber) repositionLineByNumber()
    else repositionLineByDrag(0f)

    ScrollerBox(repositionLineByDrag, repositionLineByNumber, lineOffset, controller)
}

/**
 * Composable function that displays the scroller box with a draggable line.
 *
 * @param repositionLineByDrag A function to reposition the scroller line based on drag amount.
 * @param repositionLineByNumber A function to reposition the scroller line based on the current number value.
 * @param lineOffset The current offset of the scroller line.
 * @param controller The controller managing this scroller.
 */
@Composable
fun ScrollerBox(
    repositionLineByDrag: (Float) -> Unit,
    repositionLineByNumber: () -> Unit,
    lineOffset: MutableState<Float>,
    controller: ScrollerController,
) {
    var totalDrag by remember { mutableFloatStateOf(0f) }
    var lastDragTime by remember { mutableLongStateOf(0L) }
    var scrollFactorMultiplier by remember { mutableFloatStateOf(1f) }
    var isDragging by remember { mutableStateOf(false) }

    val scrollerHeightPx = with(LocalDensity.current) { controller.scrollerStyle.scrollerHeight.toPx() }
    val scrollerWidthPx = with(LocalDensity.current) { controller.scrollerStyle.scrollerWidth.toPx() }

    if (controller.selectedTargetId?.let { controller.getTarget(it)!!.behaviour.autoIncrementOnFarScroll } == true) {
        val selectedTarget = controller.getTarget(controller.selectedTargetId!!)!!

        LaunchedEffect(isDragging, selectedTarget) {
            if (selectedTarget.behaviour.autoIncrementOnFarScroll) {
                while (isDragging) {
                    val dimensionPx = getDimensionPx(controller, scrollerWidthPx, scrollerHeightPx) // width or height in PX
                    val thresholdPx = (dimensionPx / 2 * selectedTarget.behaviour.farScrollThreshold)

                    if (kotlin.math.abs(lineOffset.value) >= thresholdPx) {
                        val direction = if (lineOffset.value >= thresholdPx) 1 else -1
                        controller.updateSelectedTarget(
                            totalDrag = direction * selectedTarget.behaviour.scrollDistanceFactor,
                            scrollFactorMultiplier = 1f // no dynamic scaling
                        )
                    }
                    delay(selectedTarget.behaviour.autoIncrementDelay)
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .width(controller.scrollerStyle.scrollerWidth)
            .height(controller.scrollerStyle.scrollerHeight)
            .clip(controller.scrollerStyle.scrollerRounding)
            .background(controller.scrollerStyle.scrollerColor)
            .pointerInput(Unit) {
                if (!controller.isEditingTarget) { // don't allow scrolling if editing target
                    when (controller.scrollerBehaviour.incrementDirection) {
                        IncrementDirection.Left, IncrementDirection.Right -> {
                            detectHorizontalDragGestures(
                                onDragStart = { isDragging = true },
                                onDragEnd = {
                                    isDragging = false
                                    if (!controller.scrollerBehaviour.syncLinePosWithNumber) lineOffset.value =
                                        0f
                                    controller.triggerDragEnd()
                                }
                            ) { _, dragAmount ->
                                totalDrag += dragAmount // calculate total drag distance

                                controller.selectedTargetId?.let { id -> // if target is selected
                                    val target = controller.getTarget(id)!!

                                    if (target.behaviour.useDynamicDistanceFactor) {
                                        val currentTime = System.currentTimeMillis()
                                        val timeElapsed = currentTime - lastDragTime
                                        lastDragTime = currentTime

                                        // calculate drag velocity (drag amount / time elapsed)
                                        val velocity =
                                            kotlin.math.abs(dragAmount / timeElapsed.coerceAtLeast(1))
                                        scrollFactorMultiplier = computeScrollFactorMultiplier(
                                            velocity,
                                            target.behaviour.dynamicDistanceScalingFactor
                                        )
                                    }

                                    val adjustedFactor =
                                        target.behaviour.scrollDistanceFactor * scrollFactorMultiplier

                                    if (totalDrag <= -adjustedFactor || totalDrag >= adjustedFactor) {
                                        controller.updateSelectedTarget(
                                            totalDrag,
                                            scrollFactorMultiplier
                                        ) // update number
                                        totalDrag = 0f
                                    }
                                    if (controller.scrollerBehaviour.syncLinePosWithNumber) repositionLineByNumber()
                                    else repositionLineByDrag(dragAmount)
                                }
                            }
                        }

                        IncrementDirection.Up, IncrementDirection.Down -> {
                            detectVerticalDragGestures(
                                onDragStart = { isDragging = true },
                                onDragEnd = {
                                    isDragging = false
                                    if (!controller.scrollerBehaviour.syncLinePosWithNumber) lineOffset.value =
                                        0f
                                    controller.triggerDragEnd()
                                }
                            ) { _, dragAmount ->
                                totalDrag += dragAmount

                                controller.selectedTargetId?.let { id ->
                                    val target = controller.getTarget(id)!!

                                    if (target.behaviour.useDynamicDistanceFactor) {
                                        val currentTime = System.currentTimeMillis()
                                        val timeElapsed = currentTime - lastDragTime
                                        lastDragTime = currentTime

                                        val velocity =
                                            kotlin.math.abs(dragAmount / timeElapsed.coerceAtLeast(1))
                                        scrollFactorMultiplier = computeScrollFactorMultiplier(
                                            velocity,
                                            target.behaviour.dynamicDistanceScalingFactor
                                        )
                                    }

                                    val adjustedFactor =
                                        target.behaviour.scrollDistanceFactor * scrollFactorMultiplier

                                    if (totalDrag <= -adjustedFactor || totalDrag >= adjustedFactor) {
                                        controller.updateSelectedTarget(
                                            totalDrag,
                                            scrollFactorMultiplier
                                        )
                                        totalDrag = 0f
                                    }
                                    if (controller.scrollerBehaviour.syncLinePosWithNumber) repositionLineByNumber()
                                    else repositionLineByDrag(dragAmount)
                                }
                            }
                        }
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .then(
                    when (controller.scrollerBehaviour.incrementDirection) {
                        IncrementDirection.Left, IncrementDirection.Right -> {
                            Modifier
                                .width(controller.scrollerStyle.lineThickness)
                                .height(controller.scrollerStyle.scrollerHeight * controller.scrollerStyle.lineWidthFactor)
                                .offset { IntOffset(lineOffset.value.toInt(), 0) }
                        }

                        IncrementDirection.Up, IncrementDirection.Down -> {
                            Modifier
                                .width(controller.scrollerStyle.scrollerWidth * controller.scrollerStyle.lineWidthFactor)
                                .height(controller.scrollerStyle.lineThickness)
                                .offset { IntOffset(0, lineOffset.value.toInt()) }
                        }
                    }
                )
                .align(Alignment.Center)
                .clip(controller.scrollerStyle.lineRounding)
                .background(controller.scrollerStyle.lineColor)
        )
    }
}

fun computeScrollFactorMultiplier(velocity: Float, scalingFactor: Float = 0f): Float {
    // use scaling factor from target behaviour config or 0f (i.e. no scaling)
    return 1f / (1 + scalingFactor * velocity * velocity) // inverse quadratic: f(x) = a / (b + x^2)
}

fun getDimensionPx(controller: ScrollerController, widthPx: Float, heightPx: Float): Float {
    return if (controller.scrollerBehaviour.incrementDirection in listOf(IncrementDirection.Left, IncrementDirection.Right)) widthPx else heightPx
}

/**
 * Composable function that displays the number text with styling.
 *
 * @param targetStyle The styling options for the number text.
 * @param step The step value for formatting the number.
 * @param number The current number to be displayed.
 */
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