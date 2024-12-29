package com.shaan.androiduicomponents.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shaan.androiduicomponents.R

class AdmissionGuideFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_admission_guide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdmissionGuide()
    }

    private fun setupAdmissionGuide() {

    }

    companion object {
        fun newInstance() = AdmissionGuideFragment()
    }
} 