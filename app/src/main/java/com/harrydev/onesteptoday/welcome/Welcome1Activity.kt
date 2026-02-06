package com.harrydev.onesteptoday.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.harrydev.onesteptoday.databinding.ActivityWelcome1Binding
import com.harrydev.onesteptoday.fragments.HomeActivity
import com.harrydev.onesteptoday.utils.PrefManager
import com.harrydev.onesteptoday.utils.fadeSlideIn
import com.harrydev.onesteptoday.utils.scaleIn

class Welcome1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcome1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefManager = PrefManager(this)
        if (prefManager.isOnboardingDone()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        binding = ActivityWelcome1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        animateUI()

        binding.btnStart.setOnClickListener {
            startActivity(Intent(this, Welcome2Activity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun animateUI() {
        binding.imgHero.scaleIn()
        binding.titleText.fadeSlideIn(200)
        binding.subText.fadeSlideIn(400)
        binding.btnStart.fadeSlideIn(600)
    }
}
