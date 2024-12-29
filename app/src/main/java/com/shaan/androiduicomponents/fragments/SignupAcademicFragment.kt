package com.shaan.androiduicomponents.fragments

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
import com.shaan.androiduicomponents.models.AcademicInfo
import com.shaan.androiduicomponents.models.EducationDetails

class  SignupAcademicFragment : BaseSignupFragment() {
    private lateinit var sscInstitutionInput: TextInputEditText
    private lateinit var sscYearDropdown: AutoCompleteTextView
    private lateinit var sscBoardDropdown: AutoCompleteTextView
    private lateinit var sscGpaInput: TextInputEditText
    private lateinit var hscInstitutionInput: TextInputEditText
    private lateinit var hscYearDropdown: AutoCompleteTextView
    private lateinit var hscBoardDropdown: AutoCompleteTextView
    private lateinit var hscGpaInput: TextInputEditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup_academic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initializeViews(view)
        setupDropdowns()
    }

    private fun initializeViews(view: View) {
        sscInstitutionInput = view.findViewById(R.id.sscInstitutionInput)
        sscYearDropdown = view.findViewById(R.id.sscYearDropdown)
        sscBoardDropdown = view.findViewById(R.id.sscBoardDropdown)
        sscGpaInput = view.findViewById(R.id.sscGpaInput)
        hscInstitutionInput = view.findViewById(R.id.hscInstitutionInput)
        hscYearDropdown = view.findViewById(R.id.hscYearDropdown)
        hscBoardDropdown = view.findViewById(R.id.hscBoardDropdown)
        hscGpaInput = view.findViewById(R.id.hscGpaInput)
    }

    private fun setupDropdowns() {
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        val years = (currentYear downTo currentYear - 10).map { it.toString() }
        val boards = arrayOf("Dhaka", "Chittagong", "Rajshahi", "Khulna", "Sylhet", "Barishal", "Cumilla", "Dinajpur", "Madrasah", "Technical")

        val yearAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, years)
        val boardAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, boards)

        sscYearDropdown.setAdapter(yearAdapter)
        hscYearDropdown.setAdapter(yearAdapter)
        sscBoardDropdown.setAdapter(boardAdapter)
        hscBoardDropdown.setAdapter(boardAdapter)
    }

    override fun isValid(): Boolean {
        if (sscInstitutionInput.text.toString().isEmpty()) {
            sscInstitutionInput.error = "SSC institution is required"
            return false
        }

        if (sscYearDropdown.text.toString().isEmpty()) {
            sscYearDropdown.error = "SSC year is required"
            return false
        }

        if (sscBoardDropdown.text.toString().isEmpty()) {
            sscBoardDropdown.error = "SSC board is required"
            return false
        }

        val sscGpa = sscGpaInput.text.toString().toFloatOrNull()
        if (sscGpa == null || sscGpa < 1.0 || sscGpa > 5.0) {
            sscGpaInput.error = "Enter valid GPA (1.0-5.0)"
            return false
        }

        if (hscInstitutionInput.text.toString().isEmpty()) {
            hscInstitutionInput.error = "HSC institution is required"
            return false
        }

        return true
    }

    override fun saveData() {
        val academicInfo = AcademicInfo(
            ssc = EducationDetails(
                institutionName = sscInstitutionInput.text.toString().trim(),
                passingYear = sscYearDropdown.text.toString().trim(),
                board = sscBoardDropdown.text.toString().trim(),
                gpa = sscGpaInput.text.toString().toFloatOrNull() ?: 0f
            ),
            hsc = EducationDetails(
                institutionName = hscInstitutionInput.text.toString().trim(),
                passingYear = hscYearDropdown.text.toString().trim(),
                board = hscBoardDropdown.text.toString().trim(),
                gpa = hscGpaInput.text.toString().toFloatOrNull() ?: 0f
            )
        )
        (activity as? SignupActivity)?.userData?.put("academicInfo", academicInfo)
    }

    fun getAcademicInfo(): AcademicInfo {
        return AcademicInfo(
            ssc = EducationDetails(
                institutionName = sscInstitutionInput.text.toString().trim(),
                passingYear = sscYearDropdown.text.toString().trim(),
                board = sscBoardDropdown.text.toString().trim(),
                gpa = sscGpaInput.text.toString().toFloatOrNull() ?: 0f
            ),
            hsc = EducationDetails(
                institutionName = hscInstitutionInput.text.toString().trim(),
                passingYear = hscYearDropdown.text.toString().trim(),
                board = hscBoardDropdown.text.toString().trim(),
                gpa = hscGpaInput.text.toString().toFloatOrNull() ?: 0f
            )
        )
    }
} 