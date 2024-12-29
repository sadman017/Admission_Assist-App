package com.shaan.androiduicomponents.fragments.base

import androidx.fragment.app.Fragment

abstract class BaseSignupFragment : Fragment() {
    abstract fun isValid(): Boolean
    abstract fun saveData()
} 