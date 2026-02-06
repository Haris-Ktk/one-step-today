package com.harrydev.onesteptoday.fragments

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.harrydev.onesteptoday.R
import com.harrydev.onesteptoday.billing.BillingManager
import com.harrydev.onesteptoday.databinding.FragmentSettingsBinding
import com.harrydev.onesteptoday.paywall.PaywallActivity
import com.harrydev.onesteptoday.utils.PrefManager
import com.harrydev.onesteptoday.viewmodel.HomeViewModel
import java.util.*

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var vm: HomeViewModel
    private lateinit var prefs: SharedPreferences
    private lateinit var prefManager: PrefManager
    private lateinit var billingManager: BillingManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        prefManager = PrefManager(requireContext())
        vm = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        billingManager = BillingManager(requireContext()) { hasPro ->
            prefManager.setProUser(hasPro)
            updateUpgradeUI()
            updateRefundUI()
            Toast.makeText(
                requireContext(),
                if (hasPro) "Pro active 👑" else "Free plan active",
                Toast.LENGTH_SHORT
            ).show()
        }
        billingManager.startConnection()

        setupProfile()
        setupReminder()
        setupReset()
        setupLegal()
        setupUpgradeCard()
        setupRestore()
        setupRefundCard()

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        billingManager.restorePurchases()
    }

    /* ---------------- PROFILE ---------------- */
    private fun setupProfile() {
        val name = prefs.getString("user_name", "User") ?: "User"
        binding.cardNameText.text = name

        binding.cardEditName.setOnClickListener {
            val input = EditText(requireContext())
            input.setText(name)

            AlertDialog.Builder(requireContext())
                .setTitle("Edit Name")
                .setView(input)
                .setPositiveButton("Save") { _, _ ->
                    prefs.edit().putString("user_name", input.text.toString()).apply()
                    binding.cardNameText.text = input.text.toString()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
    /* ---------------- REMINDER ---------------- */

    private fun setupReminder() {
        binding.switchReminder.isChecked =
            prefs.getBoolean("reminder_enabled", false)

        binding.switchReminder.setOnCheckedChangeListener { _, enabled ->
            prefs.edit().putBoolean("reminder_enabled", enabled).apply()
            if (enabled) pickTime() else cancelReminder()
        }
    }
    private fun pickTime() {
        val cal = Calendar.getInstance()

        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                prefs.edit()
                    .putInt("reminder_hour", hour)
                    .putInt("reminder_minute", minute)
                    .apply()

                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                cal.set(Calendar.SECOND, 0)

                scheduleReminder(cal)
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            false
        ).show()
    }
    private fun scheduleReminder(cal: Calendar) {
        val intent = Intent(requireContext(), ReminderReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val am = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
    }
    private fun cancelReminder() {
        val intent = Intent(requireContext(), ReminderReceiver::class.java)
        val pi = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val am = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(pi)
    }
    /* ---------------- RESET ---------------- */

    private fun setupReset() {
        binding.cardReset.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Reset Progress")
                .setMessage("All steps will be deleted.")
                .setPositiveButton("Reset") { _, _ ->
                    vm.resetAllSteps()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
    /* ---------------- UPGRADE ---------------- */

    private fun setupUpgradeCard() {
        binding.cardUpgrade.setOnClickListener {
            if (!prefManager.isProUser()) {
                startActivity(Intent(requireContext(), PaywallActivity::class.java))
            }
        }
    }
    /* ---------------- RESTORE ---------------- */

    private fun setupRestore() {
        binding.cardRestore.setOnClickListener {
            billingManager.restorePurchases()
        }
    }
    /* ---------------- REFUND ---------------- */
    private fun setupRefundCard() {
        binding.cardRefund.setOnClickListener {

            if (!prefManager.isProUser()) return@setOnClickListener

            AlertDialog.Builder(requireContext())
                .setTitle("Refund via Google Play")
                .setMessage("You’ll be redirected to Google Play to request a refund.")
                .setPositiveButton("Continue") { _, _ ->
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/account")
                        )
                    )
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
    private fun updateRefundUI() {
        binding.cardRefund.isEnabled = prefManager.isProUser()
        binding.cardRefund.alpha = if (prefManager.isProUser()) 1f else 0.4f
    }

    private fun updateUpgradeUI() {
        if (prefManager.isProUser()) {
            binding.cardUpgrade.isEnabled = false
            binding.cardUpgradeText.text = "Pro Plan Active 👑"
        } else {
            binding.cardUpgrade.isEnabled = true
            binding.cardUpgradeText.text = "Upgrade to Pro"
        }
    }

    /* ---------------- LEGAL ---------------- */

    private fun setupLegal() {
        binding.cardPrivacy.setOnClickListener {
            open("https://harix-dev.vercel.app/apps/onesteptoday/privacy")
        }
        binding.cardTerms.setOnClickListener {
            open("https://harix-dev.vercel.app/apps/onesteptoday/terms")
        }
        binding.cardSupport.setOnClickListener {
            open("https://harix-dev.vercel.app/contact")
        }
    }

    private fun open(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
