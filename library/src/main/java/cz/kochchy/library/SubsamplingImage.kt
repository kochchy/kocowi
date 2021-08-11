package cz.kochchy.library

import android.graphics.drawable.BitmapDrawable
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
fun SubsamplingImage(modifier: Modifier, data: Any?) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier,
        factory = {
            val view = SubsamplingScaleImageView(it)


            return@AndroidView view
        },
        update = { imageView ->
            val context = imageView.context
            // lifecycle.coroutineScope.launch {
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
            //}
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


/*
lifecycleScope.launchBG {

}*/