package com.harrydev.onesteptoday.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.harrydev.onesteptoday.databinding.ActivityWelcome4Binding
import com.harrydev.onesteptoday.paywall.PaywallActivity
import com.harrydev.onesteptoday.utils.PrefManager
import com.harrydev.onesteptoday.utils.fadeSlideIn
import com.harrydev.onesteptoday.utils.scaleIn

class Welcome4Activity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcome4Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcome4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        animateUI()

        binding.btnCreate.setOnClickListener {
            PrefManager(this).setOnboardingDone()
            startActivity(Intent(this, PaywallActivity::class.java))
            finish()
        }
    }
    private fun animateUI() {
        binding.imgBenefits.scaleIn()
        binding.title.fadeSlideIn(200)
        binding.benefitsText.fadeSlideIn(400)
        binding.btnCreate.fadeSlideIn(600)
    }
}
