package com.shaan.androiduicomponents

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shaan.androiduicomponents.R
import com.shaan.androiduicomponents.helpers.FirebaseHelper
import kotlinx.coroutines.launch

class ProfileDashboardSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ProfileDashboardSheet"
    }

    private var onViewProfileClickListener: (() -> Unit)? = null
    private var onSettingsClickListener: (() -> Unit)? = null
    private var onLogoutClickListener: (() -> Unit)? = null
    var onLogoutClick: (() -> Unit)? = null

    private lateinit var userNameText: TextView
    private lateinit var userEmailText: TextView
//    private lateinit var userIdText: TextView
    private lateinit var profileImage: ImageView
    private lateinit var editProfileOption: TextView
    private lateinit var viewProfileOption: TextView
    private lateinit var settingsOption: TextView
    private lateinit var helpOption: TextView
    private lateinit var logoutOption: TextView
//    private lateinit var loadingView: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: Creating profile dashboard view")
        return try {
            inflater.inflate(R.layout.profile_dashboard_sheet, container, false)
        } catch (e: Exception) {
            Log.e(TAG, "onCreateView: Failed to inflate view", e)
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Setting up profile dashboard")
        
        try {
            setupViews(view)
        } catch (e: Exception) {
            Log.e(TAG, "onViewCreated: Failed to setup views", e)
            Toast.makeText(context, "Error setting up profile", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    private fun setupViews(view: View) {
        Log.d(TAG, "setupViews: Setting up dashboard views")
        try {
            initializeViews(view)
            loadUserInfo()
            setupClickListeners()
            view.findViewById<MaterialTextView>(R.id.settingsOption).setOnClickListener {
                Log.d(TAG, "Settings button clicked")
                try {
                    showSettingsDialog()
                } catch (e: Exception) {
                    Log.e(TAG, "Error showing settings dialog", e)
                    Toast.makeText(context, "Error opening settings", Toast.LENGTH_SHORT).show()
                }
            }
            view.findViewById<MaterialTextView>(R.id.helpOption).setOnClickListener {
                Log.d(TAG, "Help button clicked")
                try {
                    showHelpDialog()
                } catch (e: Exception) {
                    Log.e(TAG, "Error showing help dialog", e)
                    Toast.makeText(context, "Error opening help", Toast.LENGTH_SHORT).show()
                }
            }

            view.findViewById<MaterialTextView>(R.id.logoutOption).setOnClickListener {
                Log.d(TAG, "Logout button clicked")
                try {
                    onLogoutClick?.invoke()
                } catch (e: Exception) {
                    Log.e(TAG, "Error during logout callback", e)
                    Toast.makeText(context, "Error logging out", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "setupViews: Failed to setup views", e)
            throw e
        }
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView: Cleaning up profile dashboard")
        try {
            super.onDestroyView()
        } catch (e: Exception) {
            Log.e(TAG, "onDestroyView: Error during cleanup", e)
        }
    }

    private fun initializeViews(view: View) {
        userNameText = view.findViewById(R.id.userNameText)
        userEmailText = view.findViewById(R.id.userEmailText)
//        userIdText = view.findViewById(R.id.userIdText)
        profileImage = view.findViewById(R.id.profileImage)
        editProfileOption = view.findViewById(R.id.editProfileOption)
        viewProfileOption = view.findViewById(R.id.viewProfileOption)
        settingsOption = view.findViewById(R.id.settingsOption)
        helpOption = view.findViewById(R.id.helpOption)
        logoutOption = view.findViewById(R.id.logoutOption)
//        loadingView = view.findViewById(R.id.loadingView)
    }

    private fun loadUserInfo() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
//            showLoading()
            lifecycleScope.launch {
                try {
                    val userData = FirebaseHelper.getUserData(currentUser.uid).getOrThrow()

                    userNameText.text = userData.personalInfo.fullName
                    userEmailText.text = userData.email
//                    userIdText.text = "ID: ${currentUser.uid.take(8)}..."

                    if (userData.profilePhotoUrl.isNotEmpty()) {
                        Glide.with(this@ProfileDashboardSheet)
                            .load(userData.profilePhotoUrl)
                            .placeholder(R.drawable.ic_profile_placeholder)
                            .error(R.drawable.ic_profile_placeholder)
                            .circleCrop()
                            .into(profileImage)
                    }

//                    hideLoading()
                } catch (e: Exception) {
//                    hideLoading()
                    Log.e(TAG, "Error loading profile: ${e.message}", e)
                    Toast.makeText(context, "Error loading profile: ${e.message}", 
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupClickListeners() {
        editProfileOption.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
            dismiss()
        }

        viewProfileOption.setOnClickListener {
            startActivity(Intent(requireContext(), ProfileViewActivity::class.java))
            dismiss()
        }

        settingsOption.setOnClickListener {
            onSettingsClickListener?.invoke()
            dismiss()
        }

        helpOption.setOnClickListener {
            showHelpDialog()
        }

        logoutOption.setOnClickListener {
            onLogoutClickListener?.invoke()
            dismiss()
        }

        profileImage.setOnClickListener {
            showProfilePictureDialog()
        }
    }

    private fun showSettingsDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Settings")
            .setItems(arrayOf(
                "Notification Settings",
                "Privacy Settings",
                "Language Settings",
                "Theme Settings"
            )) { _, which ->
                when (which) {
                    0 -> handleNotificationSettings()
                    1 -> handlePrivacySettings()
                    2 -> handleLanguageSettings()
                    3 -> handleThemeSettings()
                }
            }
            .show()
    }

    private fun showHelpDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Help & Support")
            .setItems(arrayOf(
                "FAQ",
                "Contact Support",
                "Report a Problem",
                "About App"
            )) { _, which ->
                when (which) {
                    0 -> openFAQ()
                    1 -> contactSupport()
                    2 -> reportProblem()
                    3 -> showAboutApp()
                }
            }
            .show()
    }

    private fun showLogoutConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun performLogout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(requireContext(), Login::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        requireActivity().finish()
    }

    private fun showProfilePictureDialog() {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        val imageView = ImageView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        Glide.with(this)
            .load(FirebaseAuth.getInstance().currentUser?.photoUrl)
            .placeholder(R.drawable.ic_profile_placeholder)
            .into(imageView)

        dialog.setContentView(imageView)
        imageView.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

//    private fun showLoading() {
//        loadingView.visibility = View.VISIBLE
//    }
//
//    private fun hideLoading() {
//        loadingView.visibility = View.GONE
//    }

    // Helper methods for settings
    private fun handleNotificationSettings() {
        // Implement notification settings
    }

    private fun handlePrivacySettings() {
        // Implement privacy settings
    }

    private fun handleLanguageSettings() {
        // Implement language settings
    }

    private fun handleThemeSettings() {
        // Implement theme settings
    }

    // Helper methods for help & support
    private fun openFAQ() {
        // Open FAQ page
    }

    private fun contactSupport() {
        // Open support contact options
    }

    private fun reportProblem() {
        // Open problem reporting form
    }

    private fun showAboutApp() {
        // Show app information
    }

    fun setOnViewProfileClickListener(listener: () -> Unit) {
        onViewProfileClickListener = listener
    }

    fun setOnSettingsClickListener(listener: () -> Unit) {
        onSettingsClickListener = listener
    }

    fun setOnLogoutClickListener(listener: () -> Unit) {
        onLogoutClickListener = listener
    }
}