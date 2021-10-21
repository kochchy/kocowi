package io.github.kochchy

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.Keep
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

@Keep
internal class SubsamplingScaleTouchHandledWithTwoFingersImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SubsamplingScaleImageView(context, attrs) {

    var enableMoveByOneFinger = false

    var touchable: Boolean = false

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (enableMoveByOneFinger) return super.dispatchTouchEvent(event)
        touchable = event.pointerCount > 1
        return if (touchable) super.dispatchTouchEvent(event) else true
    }
}