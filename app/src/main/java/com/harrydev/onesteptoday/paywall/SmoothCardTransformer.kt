package com.harrydev.onesteptoday.paywall

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class SmoothCardTransformer : ViewPager2.PageTransformer {

    override fun transformPage(page: View, position: Float) {
        val scale = 0.9f + (1 - abs(position)) * 0.1f
        page.scaleX = scale
        page.scaleY = scale
        page.alpha = 0.7f + (1 - abs(position)) * 0.3f
    }
}
