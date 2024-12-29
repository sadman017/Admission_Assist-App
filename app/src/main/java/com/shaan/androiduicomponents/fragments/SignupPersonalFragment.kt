package com.shaan.androiduicomponents.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.shaan.androiduicomponents.R
import com.shaan.androiduicomponents.SignupActivity
import com.shaan.androiduicomponents.fragments.base.BaseSignupFragment
import com.shaan.androiduicomponents.models.PersonalInfo
import java.util.Calendar

class SignupPersonalFragment : BaseSignupFragment() {
    private lateinit var nameInput: TextInputEditText
    private lateinit var dobInput: TextInputEditText
    private lateinit var genderDropdown: AutoCompleteTextView
    private lateinit var nationalityDropdown: AutoCompleteTextView
    private lateinit var nidInput: TextInputEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup_personal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        nameInput = view.findViewById(R.id.nameInput)
        dobInput = view.findViewById(R.id.dobInput)
        genderDropdown = view.findViewById(R.id.genderDropdown)
        nationalityDropdown = view.findViewById(R.id.nationalityDropdown)
        nidInput = view.findViewById(R.id.nidInput)

        setupGenderDropdown()
        setupNationalityDropdown()
        setupDatePicker()
    }

    private fun setupGenderDropdown() {
        val genders = arrayOf("Male", "Female", "Other", "Prefer not to say")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, genders)
        genderDropdown.setAdapter(adapter)
    }

    private fun setupNationalityDropdown() {
        val nationalities = arrayOf("Bangladeshi", "Other")
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, nationalities)
        nationalityDropdown.setAdapter(adapter)
        nationalityDropdown.setText("Bangladeshi", false)
    }

    private fun setupDatePicker() {
        dobInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    dobInput.setText("$day/${month + 1}/$year")
                },
                calendar.get(Calendar.YEAR) - 18,
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    override fun isValid(): Boolean {
        if (nameInput.text.toString().isEmpty()) {
            nameInput.error = "Name is required"
            return false
        }

        if (dobInput.text.toString().isEmpty()) {
            dobInput.error = "Date of birth is required"
            return false
        }

        if (genderDropdown.text.toString().isEmpty()) {
            genderDropdown.error = "Gender is required"
            return false
        }

        return true
    }

    override fun saveData() {
        val personalInfo = PersonalInfo(
            fullName = nameInput.text.toString().trim(),
            dateOfBirth = dobInput.text.toString().trim(),
            gender = genderDropdown.text.toString().trim(),
            nationality = nationalityDropdown.text.toString().trim(),
            nidNumber = nidInput.text.toString().trim()
        )
        (activity as? SignupActivity)?.userData?.put("personalInfo", personalInfo)
    }

    fun getPersonalInfo(): PersonalInfo {
        return PersonalInfo(
            fullName = nameInput.text.toString().trim(),
            dateOfBirth = dobInput.text.toString().trim(),
            gender = genderDropdown.text.toString().trim(),
            nationality = nationalityDropdown.text.toString().trim(),
            nidNumber = nidInput.text.toString().trim()
        )
    }
} 