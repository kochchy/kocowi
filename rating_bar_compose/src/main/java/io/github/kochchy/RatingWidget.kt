package io.github.kochchy

import android.content.res.Resources
import androidx.annotation.Keep
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
@Keep
fun RatingWidget(
    modifier: Modifier = Modifier,
    size: Dp = 18.dp,
    spacing: Dp = 2.dp,
    rating: Float,
    max: Int = 5,
    step: Float? = null,
    painterFullImage: Painter = rememberVectorPainter(image = Icons.Default.Star),
    painterEmptyImage: Painter = rememberVectorPainter(image = Icons.Default.StarOutline),
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .wrapContentWidth()
                .align(Alignment.Center)
        ) {
            RatingWidgetRender(
                rating = step?.let { roundStep(rating, step) } ?: rating,
                size = size,
                spacing = spacing,
                max = max,
                painterFullImage = painterFullImage,
                painterEmptyImage = painterEmptyImage,
            )
        }
    }
}

@Composable
@Keep
fun RatingWidget(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    spacing: Dp = 2.dp,
    initialValue: Float = 0f,
    step: Float = 0.5f,
    min: Float = 0f,
    max: Int = 5,
    painterFullImage: Painter = rememberVectorPainter(image = Icons.Default.Star),
    painterEmptyImage: Painter = rememberVectorPainter(image = Icons.Default.StarOutline),
    ratingChanged: (Float) -> Unit
) {
    val initialValueRounded = roundStep(initialValue, step)
    val pointerPosition = remember { mutableStateOf(0f) }
    val ratingState = remember(calculation = { mutableStateOf(initialValueRounded) })
    val width = size * max + spacing * max * 2

    Box(modifier = modifier) {
        Row(modifier = Modifier
            .width(width = width)
            .draggable(
                state = rememberDraggableState(onDelta = { dragAmount ->
                    var x = pointerPosition.value
                    x += dragAmount

                    pointerPosition.value = x

                    val rating = calcRating(x, width, step, max, min)
                    ratingChanged.invoke(rating)
                    ratingState.value = rating
                }),
                orientation = Orientation.Horizontal
            )
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    val x = it.x
                    pointerPosition.value = x

                    val rating = calcRating(x, width, step, max, min)
                    ratingChanged.invoke(rating)
                    ratingState.value = rating
                })
            }
            .wrapContentWidth()
            .align(Alignment.Center)) {
            RatingWidgetRender(ratingState.value, size, spacing, max, painterFullImage, painterEmptyImage)
        }
    }
}

@Composable
fun RatingWidgetRender(rating: Float, size: Dp, spacing: Dp, max: Int, painterFullImage: Painter, painterEmptyImage: Painter) {
    val beforeStarts = floor(rating).toInt()
    val splitValue = rating.rem(1f)
    var rest = max - beforeStarts
    val showSplitStar = splitValue > 0f
    if (showSplitStar) rest--

    repeat(beforeStarts) {
        Image(
            modifier = Modifier
                .size(size = size + spacing * 2)
                .padding(spacing),
            contentDescription = "",
            painter = painterFullImage,
            contentScale = ContentScale.FillHeight
        )
    }
    if (showSplitStar) {
        Row(
            Modifier
                .size(size = size + spacing * 2)
                .padding(spacing)
        ) {
            Image(
                modifier = Modifier
                    .weight(splitValue)
                    .height(size),
                contentDescription = "",
                painter = painterFullImage,
                contentScale = ContentScale.FillHeight,
                alignment = Alignment.CenterStart
            )
            Image(
                modifier = Modifier
                    .weight(1f - splitValue)
                    .height(size),
                contentDescription = "",
                painter = painterEmptyImage,
                contentScale = ContentScale.FillHeight,
                alignment = Alignment.CenterEnd
            )
        }
    }
    repeat(rest) {
        Image(
            modifier = Modifier
                .size(size = size + spacing * 2)
                .padding(spacing),
            contentDescription = "",
            painter = painterEmptyImage,
            contentScale = ContentScale.FillHeight
        )
    }
}

private fun calcRating(pointerPosition: Float, width: Dp, step: Float, max: Int, minRating: Float): Float {
    val progress = max(((pxToDp(pointerPosition).dp) / width), 0f)
    val checkedProgress = min(1f, progress)
    return roundStep((max.toFloat() * checkedProgress).coerceAtLeast(minRating), step)
}

private fun roundStep(input: Float, step: Float): Float {
    return (input / step).roundToInt() * step
}

private fun pxToDp(px: Float): Float {
    return (px / Resources.getSystem().displayMetrics.density)
}
