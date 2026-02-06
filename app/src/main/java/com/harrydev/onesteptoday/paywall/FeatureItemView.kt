package com.harrydev.onesteptoday.paywall

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.harrydev.onesteptoday.databinding.ItemFeatureBinding
class FeatureItemView(context: Context) : LinearLayout(context) {
    private val binding =
        ItemFeatureBinding.inflate(LayoutInflater.from(context), this, true)
    fun setText(text: String) {
        binding.tvFeature.text = text
    }
}
