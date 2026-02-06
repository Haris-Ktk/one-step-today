package com.harrydev.onesteptoday.utils

import android.view.View

fun View.fadeSlideIn(delay: Long = 0) {
    alpha = 0f
    translationY = 40f
    animate()
        .alpha(1f)
        .translationY(0f)
        .setDuration(700)
        .setStartDelay(delay)
        .start()
}

fun View.scaleIn(delay: Long = 0) {
    scaleX = 0.9f
    scaleY = 0.9f
    alpha = 0f
    animate()
        .scaleX(1f)
        .scaleY(1f)
        .alpha(1f)
        .setDuration(600)
        .setStartDelay(delay)
        .start()
}
