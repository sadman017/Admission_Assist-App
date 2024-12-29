package com.shaan.androiduicomponents.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import com.shaan.androiduicomponents.*

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners(view)
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<MaterialCardView>(R.id.universityFinderCard).setOnClickListener {
            startActivity(Intent(requireContext(), UniversityListActivity::class.java))
        }

        view.findViewById<MaterialCardView>(R.id.eligibilityCheckCard).setOnClickListener {
            startActivity(Intent(requireContext(), EligibilityCheckActivity::class.java))
        }

        view.findViewById<MaterialCardView>(R.id.deadlinesCard).setOnClickListener {
            startActivity(Intent(requireContext(), DeadlinesActivity::class.java))
        }

        view.findViewById<MaterialCardView>(R.id.shortlistCard).setOnClickListener {
            startActivity(Intent(requireContext(), ShortlistActivity::class.java))
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
} 