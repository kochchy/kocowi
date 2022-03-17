package io.github.kochchy

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.Keep
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.davemorrissey.labs.subscaleview.ImageSource
import kotlinx.coroutines.launch

@Composable
@Keep
fun SubsamplingImage(modifier: Modifier, data: Any?, maxScale: Float = 2f, enableMoveByOneFinger : Boolean = false) {
    val context = LocalContext.current

    val bitmapState = remember(data) {
        mutableStateOf<Bitmap?>(null)
    }

    val scope = rememberCoroutineScope()
    DisposableEffect(key1 = data, bitmapState.value == null, effect = {
        val job = scope.launch {
            val bmp = loadBitmap(context = context, data)
            bitmapState.value = bmp
        }

        onDispose {
            job.cancel()
        }
    })

    AndroidView(
        modifier = modifier,
        factory = {
            return@AndroidView SubsamplingScaleTouchHandledWithTwoFingersImageView(it).apply {
                this.enableMoveByOneFinger = enableMoveByOneFinger
                /*setOnTouchListener { v, event ->
                    val scale = this.scale
                    return@setOnTouchListener false
                }*/
            }
        },
        update = { imageView ->
            imageView.maxScale = maxScale
            bitmapState.value?.let {
                imageView.setImage(ImageSource.bitmap(it))
            }
        }
    )
}

private suspend fun loadBitmap(context: Context, data: Any?): Bitmap? {
    val loader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(data)
        .allowHardware(false) // Disable hardware bitmaps.
        .build()

    val result = (loader.execute(request) as? SuccessResult?)?.drawable
    return (result as? BitmapDrawable?)?.bitmap
}

