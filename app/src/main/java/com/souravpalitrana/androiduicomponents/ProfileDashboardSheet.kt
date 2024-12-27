package com.souravpalitrana.androiduicomponents
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ProfileDashboardSheet : BottomSheetDialogFragment() {
    
    private var onViewProfileClick: (() -> Unit)? = null
    private var onSettingsClick: (() -> Unit)? = null
    private var onLogoutClick: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_dashboard_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.viewProfileOption).setOnClickListener {
            dismiss()
            onViewProfileClick?.invoke()
        }

        view.findViewById<View>(R.id.settingsOption).setOnClickListener {
            dismiss()
            onSettingsClick?.invoke()
        }

        view.findViewById<View>(R.id.logoutOption).setOnClickListener {
            dismiss()
            onLogoutClick?.invoke()
        }
    }

    fun setOnViewProfileClickListener(listener: () -> Unit) {
        onViewProfileClick = listener
    }

    fun setOnSettingsClickListener(listener: () -> Unit) {
        onSettingsClick = listener
    }

    fun setOnLogoutClickListener(listener: () -> Unit) {
        onLogoutClick = listener
    }

    companion object {
        const val TAG = "ProfileDashboardSheet"
    }
} 