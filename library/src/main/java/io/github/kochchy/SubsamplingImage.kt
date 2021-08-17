package io.github.kochchy

import android.graphics.drawable.BitmapDrawable
import androidx.annotation.Keep
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.*
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@Composable
@Keep
fun SubsamplingImage(modifier: Modifier, data: Any?, maxScale : Float = 2f) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier,
        factory = {
            return@AndroidView SubsamplingScaleImageView(it).apply {
                this.maxScale = maxScale
            }
        },
        update = { imageView ->
            val context = imageView.context
            lifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED, block = {
                runCatching {
                    val loader = ImageLoader(context)
                    val request = ImageRequest.Builder(context)
                        .data(data)
                        .allowHardware(false) // Disable hardware bitmaps.
                        .build()

                    val result = (loader.execute(request) as SuccessResult).drawable
                    val bitmap = (result as? BitmapDrawable)?.bitmap
                    bitmap?.let {
                        withContext(Dispatchers.Main) {
                            imageView.setImage(ImageSource.bitmap(it))
                        }
                    }
                }
            })
        }
    )
}

fun LifecycleOwner.addRepeatingJob(
    state: Lifecycle.State,
    coroutineContext: CoroutineContext = kotlin.coroutines.EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
): Job = lifecycleScope.launch(coroutineContext) {
    lifecycle.repeatOnLifecycle(state, block)
}