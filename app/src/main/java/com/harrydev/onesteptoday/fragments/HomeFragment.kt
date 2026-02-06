package com.harrydev.onesteptoday.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.harrydev.onesteptoday.R
import com.harrydev.onesteptoday.adapter.StepAdapter
import com.harrydev.onesteptoday.databinding.FragmentHomeBinding
import com.harrydev.onesteptoday.paywall.PaywallActivity
import com.harrydev.onesteptoday.utils.PrefManager
import com.harrydev.onesteptoday.viewmodel.HomeViewModel
import nl.dionsegijn.konfetti.core.*
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val vm: HomeViewModel by viewModels()
    private lateinit var pref: PrefManager
    private val DAILY_LIMIT = 3

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        pref = PrefManager(requireContext())

        if (pref.isProUser()) {
            binding.cardDailyLimit.visibility = View.GONE
            binding.btnUpgrade.visibility = View.GONE
        }
        resetLimitIfNeeded()
        updateLimitUI()

        binding.txtWelcome.text = "Welcome ${pref.getName()}"

        val adapter = StepAdapter(
            onDone = { vm.toggleDone(it) },
            onDelete = { vm.delete(it) },
            onEdit = { step ->
                val intent = Intent(requireContext(), CreateStepActivity::class.java)
                intent.putExtra("STEP_ID", step.id)
                intent.putExtra("TITLE", step.title)
                intent.putExtra("SHORT", step.shortDesc)
                intent.putExtra("LONG", step.longDesc)
                startActivity(intent)
            },
            onCelebrate = { showCelebration() }
        )

        binding.recyclerHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHistory.adapter = adapter

        vm.steps.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.cardTodayStep.setOnClickListener {
            if (!pref.isProUser() && pref.getTodayCount() >= DAILY_LIMIT) {
                Toast.makeText(requireContext(), "Daily limit reached. Upgrade to Pro 🚀", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), PaywallActivity::class.java))
                return@setOnClickListener
            }
            startActivity(Intent(requireContext(), CreateStepActivity::class.java))
        }

        binding.btnUpgrade.setOnClickListener {
            startActivity(Intent(requireContext(), PaywallActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        if (pref.isProUser()) {
            binding.cardDailyLimit.visibility = View.GONE
            binding.btnUpgrade.visibility = View.GONE
        } else {
            binding.cardDailyLimit.visibility = View.VISIBLE
            binding.btnUpgrade.visibility = View.VISIBLE
        }

        resetLimitIfNeeded()
        updateLimitUI()
    }

    private fun updateLimitUI() {
        if (pref.isProUser()) return
        val used = pref.getTodayCount()
        binding.progressDaily.max = DAILY_LIMIT
        binding.progressDaily.progress = used
        binding.txtLimitCount.text = "$used / $DAILY_LIMIT used"
    }

    private fun resetLimitIfNeeded() {
        if (pref.isProUser()) return
        val last = pref.getLastReset()
        val now = System.currentTimeMillis()
        if (now - last > TimeUnit.HOURS.toMillis(24)) {
            pref.resetDailyCount()
            pref.setLastReset(now)
        }
    }

    private fun playSuccessSound() {
        val mp = MediaPlayer.create(requireContext(), R.raw.success)
        mp.start()
        mp.setOnCompletionListener { it.release() }
    }

    private fun showSuccessDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_success, null)
        val dialog = AlertDialog.Builder(requireContext()).setView(dialogView).create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()

        dialogView.findViewById<View>(R.id.btnAwesome).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showCelebration() {
        binding.konfettiView.start(
            Party(
                speed = 10f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(Color.YELLOW, Color.GREEN, Color.MAGENTA, Color.CYAN, Color.RED),
                emitter = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(300),
                position = Position.Relative(0.5, 0.3)
            )
        )
        playSuccessSound()
        showSuccessDialog()
    }
}
