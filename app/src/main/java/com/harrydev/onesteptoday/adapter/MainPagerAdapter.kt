package com.harrydev.onesteptoday.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.harrydev.onesteptoday.fragments.AchieveFragment
import com.harrydev.onesteptoday.fragments.HistoryFragment
import com.harrydev.onesteptoday.fragments.HomeFragment
import com.harrydev.onesteptoday.fragments.SettingsFragment

class MainPagerAdapter(
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> AchieveFragment()
            2 -> HistoryFragment()
            3 -> SettingsFragment()
            else -> HomeFragment()
        }
    }
}
