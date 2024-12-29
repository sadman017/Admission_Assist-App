package com.shaan.androiduicomponents.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shaan.androiduicomponents.fragments.*
import com.shaan.androiduicomponents.fragments.base.BaseSignupFragment

class SignupPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    private val fragments = mutableMapOf<Int, BaseSignupFragment>()

    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> SignupWelcomeFragment()
            1 -> SignupPersonalFragment()
            2 -> SignupContactFragment()
            3 -> SignupAcademicFragment()
            4 -> SignupPreferencesFragment()
            5 -> SignupFinalFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
        fragments[position] = fragment as BaseSignupFragment
        return fragment
    }

    fun getFragment(position: Int): BaseSignupFragment {
        return fragments[position] ?: throw IllegalStateException("Fragment not created for position $position")
    }
} 