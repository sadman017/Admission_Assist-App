package com.shaan.androiduicomponents.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.checkbox.MaterialCheckBox
import com.shaan.androiduicomponents.R
import com.shaan.androiduicomponents.fragments.base.BaseSignupFragment

class SignupFinalFragment : BaseSignupFragment() {
    private lateinit var profilePhotoPreview: ImageView
    private lateinit var signaturePreview: ImageView
    private lateinit var uploadPhotoButton: MaterialButton
    private lateinit var uploadSignatureButton: MaterialButton
    private lateinit var termsCheck: MaterialCheckBox
    private lateinit var dataSharingCheck: MaterialCheckBox

//    var profilePhotoUri: Uri? = null
//    var signatureUri: Uri? = null

    companion object {
        private const val PICK_PROFILE_PHOTO = 1
        private const val PICK_SIGNATURE = 2
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup_final, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        initializeViews(view)
        setupClickListeners()
    }

    private fun initializeViews(view: View) {
        profilePhotoPreview = view.findViewById(R.id.profilePhotoPreview)
        signaturePreview = view.findViewById(R.id.signaturePreview)
        uploadPhotoButton = view.findViewById(R.id.uploadPhotoButton)
        uploadSignatureButton = view.findViewById(R.id.uploadSignatureButton)
        termsCheck = view.findViewById(R.id.termsCheck)
        dataSharingCheck = view.findViewById(R.id.dataSharingCheck)
    }

    private fun setupClickListeners() {
        uploadPhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_PROFILE_PHOTO)
        }

        uploadSignatureButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_SIGNATURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                PICK_PROFILE_PHOTO -> {
                    _profilePhotoUri = data.data
                    profilePhotoPreview.setImageURI(_profilePhotoUri)
                }
                PICK_SIGNATURE -> {
                    _signatureUri = data.data
                    signaturePreview.setImageURI(_signatureUri)
                }
            }
        }
    }

    override fun isValid(): Boolean {
        if (!termsCheck.isChecked) {
            Toast.makeText(context, "Please accept the terms and conditions", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!dataSharingCheck.isChecked) {
            Toast.makeText(context, "Please accept the data sharing consent", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun saveData() {

    }

    val profilePhotoUri: Uri?
        get() = _profilePhotoUri

    val signatureUri: Uri?
        get() = _signatureUri

    private var _profilePhotoUri: Uri? = null
    private var _signatureUri: Uri? = null
} 