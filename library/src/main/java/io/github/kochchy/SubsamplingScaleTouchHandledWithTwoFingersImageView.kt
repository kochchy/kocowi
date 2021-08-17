package io.github.kochchy

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView

internal class SubsamplingScaleTouchHandledWithTwoFingersImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : SubsamplingScaleImageView(context, attrs) {

    var touchable: Boolean = false

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        touchable = ev.pointerCount > 1
        return if (touchable) super.dispatchTouchEvent(ev) else true
    }
}