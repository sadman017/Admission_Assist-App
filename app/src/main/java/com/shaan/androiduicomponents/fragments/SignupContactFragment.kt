package com.shaan.androiduicomponents.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textfield.TextInputEditText
import com.shaan.androiduicomponents.R
import com.shaan.androiduicomponents.SignupActivity
import com.shaan.androiduicomponents.fragments.base.BaseSignupFragment
import com.shaan.androiduicomponents.models.ContactInfo

class SignupContactFragment : BaseSignupFragment() {
    private lateinit var mobileInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var emergencyContactInput: TextInputEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        mobileInput = view.findViewById(R.id.mobileInput)
        emailInput = view.findViewById(R.id.emailInput)
        emergencyContactInput = view.findViewById(R.id.emergencyContactInput)
    }

    override fun isValid(): Boolean {
        if (mobileInput.text.toString().length != 10) {
            mobileInput.error = "Please enter a valid mobile number"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput.text.toString()).matches()) {
            emailInput.error = "Please enter a valid email address"
            return false
        }

        if (emergencyContactInput.text?.isNotEmpty() == true && emergencyContactInput.text?.length != 10) {
            emergencyContactInput.error = "Please enter a valid emergency contact number"
            return false
        }

        return true
    }

    override fun saveData() {
        val contactInfo = ContactInfo(
            mobileNumber = mobileInput.text.toString().trim(),
            email = emailInput.text.toString().trim(),
            emergencyContact = emergencyContactInput.text.toString().trim()
        )
        (activity as? SignupActivity)?.userData?.put("contactInfo", contactInfo)
    }

    fun getContactInfo(): ContactInfo {
        return ContactInfo(
            mobileNumber = mobileInput.text.toString().trim(),
            email = emailInput.text.toString().trim(),
            emergencyContact = emergencyContactInput.text.toString().trim()
        )
    }
} 