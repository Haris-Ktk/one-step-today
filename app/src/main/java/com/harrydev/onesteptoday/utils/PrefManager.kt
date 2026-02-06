package com.harrydev.onesteptoday.utils

import android.content.Context

class PrefManager(context: Context) {

    private val prefs =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    fun saveName(name: String) {
        prefs.edit().putString("user_name", name).apply()
    }

    fun getName(): String {
        return prefs.getString("user_name", "User") ?: "User"
    }
    fun setOnboardingDone(done: Boolean = true) {
        prefs.edit().putBoolean("onboarding_done", done).apply()
    }

    fun isOnboardingDone(): Boolean {
        return prefs.getBoolean("onboarding_done", false)
    }
    private val KEY_TODAY_COUNT = "today_step_count"
    private val KEY_LAST_RESET = "last_step_reset"

    fun getTodayCount(): Int = prefs.getInt(KEY_TODAY_COUNT, 0)

    fun increaseTodayCount() {
        prefs.edit().putInt(KEY_TODAY_COUNT, getTodayCount() + 1).apply()
    }

    fun resetDailyCount() {
        prefs.edit().putInt(KEY_TODAY_COUNT, 0).apply()
    }

    fun getLastReset(): Long = prefs.getLong(KEY_LAST_RESET, 0L)

    fun setLastReset(time: Long) {
        prefs.edit().putLong(KEY_LAST_RESET, time).apply()
    }
    fun isProUser(): Boolean {
        return prefs.getBoolean("is_pro", false)
    }

    fun setProUser(value: Boolean) {
        prefs.edit().putBoolean("is_pro", value).apply()
    }
}
