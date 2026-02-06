package com.harrydev.onesteptoday.welcome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.scaleIn
import com.harrydev.onesteptoday.databinding.ActivityWelcome2Binding
import com.harrydev.onesteptoday.utils.fadeSlideIn
import com.harrydev.onesteptoday.utils.scaleIn

class Welcome2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcome2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcome2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        animateUI()

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, Welcome3Activity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun animateUI() {
        binding.imgSteps.scaleIn()
        binding.title.fadeSlideIn(200)
        binding.btnNext.fadeSlideIn(500)
    }
}
