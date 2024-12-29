package com.shaan.androiduicomponents.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.shaan.androiduicomponents.R
import com.shaan.androiduicomponents.SignupActivity
import com.shaan.androiduicomponents.fragments.base.BaseSignupFragment
import com.shaan.androiduicomponents.models.AdmissionPreferences

class SignupPreferencesFragment : BaseSignupFragment() {
    private lateinit var universitiesDropdown: AutoCompleteTextView
    private lateinit var subjectsDropdown: AutoCompleteTextView
    private lateinit var selectedUniversitiesChipGroup: ChipGroup
    private lateinit var selectedSubjectsChipGroup: ChipGroup
    private lateinit var publicUniversityCheck: MaterialCheckBox
    private lateinit var medicalCollegeCheck: MaterialCheckBox
    private lateinit var privateUniversityCheck: MaterialCheckBox

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initializeViews(view)
        setupDropdowns()
    }

    private fun initializeViews(view: View) {
        universitiesDropdown = view.findViewById(R.id.universitiesDropdown)
        subjectsDropdown = view.findViewById(R.id.subjectsDropdown)
        selectedUniversitiesChipGroup = view.findViewById(R.id.selectedUniversitiesChipGroup)
        selectedSubjectsChipGroup = view.findViewById(R.id.selectedSubjectsChipGroup)
        publicUniversityCheck = view.findViewById(R.id.publicUniversityCheck)
        medicalCollegeCheck = view.findViewById(R.id.medicalCollegeCheck)
        privateUniversityCheck = view.findViewById(R.id.privateUniversityCheck)
    }

    private fun setupDropdowns() {
        val universities = arrayOf(
            "University of Dhaka",
            "BUET",
            "Dhaka Medical College",
            "RUET",
            "KUET",
            "North South University",
            "BRAC University"
        )

        val subjects = arrayOf(
            "Computer Science",
            "Electrical Engineering",
            "Medicine",
            "Business Administration",
            "Economics",
            "Architecture"
        )

        universitiesDropdown.setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_item, universities))
        subjectsDropdown.setAdapter(ArrayAdapter(requireContext(), R.layout.dropdown_item, subjects))

        setupChipGroups()
    }

    private fun setupChipGroups() {
        universitiesDropdown.setOnItemClickListener { _, _, _, _ ->
            val university = universitiesDropdown.text.toString()
            if (!isChipExists(selectedUniversitiesChipGroup, university)) {
                addChip(selectedUniversitiesChipGroup, university)
                universitiesDropdown.setText("")
            }
        }

        subjectsDropdown.setOnItemClickListener { _, _, _, _ ->
            val subject = subjectsDropdown.text.toString()
            if (!isChipExists(selectedSubjectsChipGroup, subject)) {
                addChip(selectedSubjectsChipGroup, subject)
                subjectsDropdown.setText("")
            }
        }
    }

    private fun addChip(chipGroup: ChipGroup, text: String) {
        val chip = Chip(requireContext()).apply {
            this.text = text
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                chipGroup.removeView(this)
            }
        }
        chipGroup.addView(chip)
    }

    private fun isChipExists(chipGroup: ChipGroup, text: String): Boolean {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as Chip
            if (chip.text == text) return true
        }
        return false
    }

    override fun isValid(): Boolean {
        var isValid = true

        if (selectedUniversitiesChipGroup.childCount == 0) {
            universitiesDropdown.error = "Please select at least one university"
            isValid = false
        }

        if (selectedSubjectsChipGroup.childCount == 0) {
            subjectsDropdown.error = "Please select at least one subject"
            isValid = false
        }

        if (!publicUniversityCheck.isChecked &&
            !medicalCollegeCheck.isChecked &&
            !privateUniversityCheck.isChecked) {
            isValid = false
        }

        val selectedUniversities = (0 until selectedUniversitiesChipGroup.childCount).map {
            (selectedUniversitiesChipGroup.getChildAt(it) as Chip).text.toString()
        }
        val selectedSubjects = (0 until selectedSubjectsChipGroup.childCount).map {
            (selectedSubjectsChipGroup.getChildAt(it) as Chip).text.toString()
        }

        println("Selected Universities: $selectedUniversities")
        println("Selected Subjects: $selectedSubjects")
        println("Public University Check: ${publicUniversityCheck.isChecked}")
        println("Medical College Check: ${medicalCollegeCheck.isChecked}")
        println("Private University Check: ${privateUniversityCheck.isChecked}")

        return isValid
    }

    override fun saveData() {
        val preferences = AdmissionPreferences(
            preferredUniversities = getSelectedChipTexts(selectedUniversitiesChipGroup),
            preferredSubjects = getSelectedChipTexts(selectedSubjectsChipGroup),
            examCategories = getSelectedExamCategories()
        )
        (activity as? SignupActivity)?.userData?.put("preferences", preferences)
    }

    fun getPreferences(): AdmissionPreferences {
        return AdmissionPreferences(
            preferredUniversities = getSelectedChipTexts(selectedUniversitiesChipGroup),
            preferredSubjects = getSelectedChipTexts(selectedSubjectsChipGroup),
            examCategories = getSelectedExamCategories()
        )
    }

    private fun getSelectedChipTexts(chipGroup: ChipGroup): List<String> {
        val texts = mutableListOf<String>()
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? Chip
            chip?.text?.toString()?.let { texts.add(it) }
        }
        return texts
    }

    private fun getSelectedExamCategories(): List<String> {
        val categories = mutableListOf<String>()
        if (publicUniversityCheck.isChecked) categories.add("Public University Admission")
        if (medicalCollegeCheck.isChecked) categories.add("Medical Colleges")
        if (privateUniversityCheck.isChecked) categories.add("Private Universities")
        view?.findViewById<MaterialCheckBox>(R.id.othersCheck)?.let {
            if (it.isChecked) {
                categories.add("Others")
            }
        }
        return categories
    }
} 