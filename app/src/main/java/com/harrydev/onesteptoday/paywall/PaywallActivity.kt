package com.harrydev.onesteptoday.paywall

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.harrydev.onesteptoday.R
import com.harrydev.onesteptoday.billing.BillingManager
import com.harrydev.onesteptoday.databinding.ActivityPaywallBinding
import com.harrydev.onesteptoday.fragments.HomeActivity
class PaywallActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaywallBinding
    private lateinit var billing: BillingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaywallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* ---------------- PLANS ---------------- */
        val plans = listOf(
            PlanUi(
                title = "Free Plan",
                price = "0 Cost",
                features = listOf(
                    "3 steps create",
                    "Daily reminder",
                    "Showing progress",
                    "24/7 support"
                )
            ),
            PlanUi(
                title = "Pro Plan",
                price = "$25.99 / Lifetime",
                features = listOf(
                    "Unlimited steps",
                    "Daily reminder",
                    "Regular updates",
                    "Weekly progress summary",
                    "Fast & stable performance",
                    "Enhanced visual experience",
                    "24/7 support"
                )
            )
        )
        /* ---------------- VIEWPAGER ---------------- */

        binding.planViewPager.adapter = PlanPagerAdapter(plans)
        binding.planViewPager.setPageTransformer(SmoothCardTransformer())
        binding.planViewPager.offscreenPageLimit = 2

        setupDots(plans.size)
        updateDots(0)

        binding.planViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updateDots(position)
                }
            }
        )
        /* ---------------- BILLING ---------------- */

        billing = BillingManager(this) { isPro ->
            if (isPro) goHome()
        }
        billing.startConnection()

        /* ---------------- BUTTONS ---------------- */

        binding.btnUpgrade.setOnClickListener {
            billing.launchPurchase(this)
        }
        binding.btnPrimary.setOnClickListener {
            goHome()
        }

        binding.tvPrivacy.setOnClickListener {
            open("https://harix-dev.vercel.app/apps/onesteptoday/privacy")
        }

        binding.tvTerms.setOnClickListener {
            open("https://harix-dev.vercel.app/apps/onesteptoday/terms")
        }
    }
    /* ---------------- DOT INDICATOR ---------------- */

    private fun setupDots(count: Int) {
        binding.dotContainer.removeAllViews()

        repeat(count) {
            val dot = ImageView(this)
            dot.setImageResource(R.drawable.dot_unselected)

            val params = android.widget.LinearLayout.LayoutParams(
                16,
                16
            )
            params.setMargins(8, 0, 8, 0)
            dot.layoutParams = params

            binding.dotContainer.addView(dot)
        }
    }
    private fun updateDots(selectedIndex: Int) {
        for (i in 0 until binding.dotContainer.childCount) {
            val dot = binding.dotContainer.getChildAt(i) as ImageView
            dot.setImageResource(
                if (i == selectedIndex)
                    R.drawable.dot_selected
                else
                    R.drawable.dot_unselected
            )
        }
    }
    /* ---------------- NAV ---------------- */

    private fun goHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finishAffinity()
    }
    private fun open(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
