package com.practicum.playlistmakerapp

import android.util.TypedValue
import android.widget.ImageView

object DpToPx {
    fun dpToPx(dp: Float, view: ImageView): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            view.resources.displayMetrics).toInt()
    }
}