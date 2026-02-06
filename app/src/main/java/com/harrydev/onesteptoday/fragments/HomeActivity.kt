package com.harrydev.onesteptoday.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.harrydev.onesteptoday.R
import com.harrydev.onesteptoday.adapter.MainPagerAdapter
import com.harrydev.onesteptoday.databinding.ActivityMainBinding
import kotlin.math.abs
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupBottomNav()
    }
    private fun setupViewPager() {
        binding.viewPager.adapter = MainPagerAdapter(this)
        binding.viewPager.isUserInputEnabled = true
        binding.viewPager.setPageTransformer { page, position ->
            page.apply {
                val scale = 0.92f + (1 - abs(position)) * 0.08f
                scaleX = scale
                scaleY = scale
                alpha = 0.85f + (1 - abs(position)) * 0.15f
            }
        }
        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updateSelection(position)
                }
            }
        )
    }
    private fun setupBottomNav() {
        binding.navHome.setOnClickListener { binding.viewPager.currentItem = 0 }
        binding.navAchieve.setOnClickListener { binding.viewPager.currentItem = 1 }
        binding.navHistory.setOnClickListener { binding.viewPager.currentItem = 2 }
        binding.navSetting.setOnClickListener { binding.viewPager.currentItem = 3 }
    }
    private fun updateSelection(position: Int) {
        resetIcons()

        val selectedView = when (position) {
            0 -> binding.navHome
            1 -> binding.navAchieve
            2 -> binding.navHistory
            else -> binding.navSetting
        }
        selectedView.setBackgroundResource(R.drawable.bg_nav_selected)

        // Floating + scale animation
        selectedView.animate()
            .scaleX(1.18f)
            .scaleY(1.18f)
            .translationY(-6f)
            .setDuration(200)
            .start()
    }
    private fun resetIcons() {
        val icons = listOf(
            binding.navHome,
            binding.navAchieve,
            binding.navHistory,
            binding.navSetting
        )
        icons.forEach {
            it.background = null
            it.animate()
                .scaleX(1f)
                .scaleY(1f)
                .translationY(0f)
                .setDuration(150)
                .start()
        }
    }
}
