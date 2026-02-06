package com.harrydev.onesteptoday.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.harrydev.onesteptoday.databinding.ActivityWelcome3Binding
import com.harrydev.onesteptoday.utils.PrefManager
import com.harrydev.onesteptoday.utils.fadeSlideIn
import com.harrydev.onesteptoday.utils.scaleIn

class Welcome3Activity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcome3Binding
    private lateinit var prefManager: PrefManager
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcome3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager(this)

        animateUI()

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnContinue.setOnClickListener {
            val name = binding.etName.text.toString().trim()

            if (name.isEmpty()) {
                binding.etName.error = "Please enter your name"
                return@setOnClickListener
            }

            showLoading(true)

            saveNameToFirestore(name)
        }
    }
    private fun saveNameToFirestore(name: String) {

        val userData = hashMapOf(
            "name" to name,
            "createdAt" to System.currentTimeMillis()
        )

        firestore
            .collection("users")
            .document(getUserId())
            .set(userData)
            .addOnSuccessListener {
                prefManager.saveName(name)

                showLoading(false)

                startActivity(Intent(this, Welcome4Activity::class.java))
                overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                )
            }
            .addOnFailureListener { e ->
                showLoading(false)
                e.printStackTrace()
            }
    }
    private fun getUserId(): String {
        return prefManager.getName().ifEmpty {
            System.currentTimeMillis().toString()
        }
    }
    private fun showLoading(show: Boolean) {
        binding.loading.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnContinue.isEnabled = !show
        binding.btnContinue.alpha = if (show) 0.6f else 1f
    }
    private fun animateUI() {
        binding.appIcon.scaleIn()
        binding.titleSmall.fadeSlideIn(150)
        binding.title.fadeSlideIn(300)
        binding.nameInputLayout.fadeSlideIn(450)
        binding.btnContinue.fadeSlideIn(600)
    }
}
