package com.shaan.androiduicomponents

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shaan.androiduicomponents.databinding.ActivityUniversityListBinding
import com.shaan.androiduicomponents.helpers.FirebaseHelper
import com.shaan.androiduicomponents.helpers.NotificationHelper
import com.shaan.androiduicomponents.models.University
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Job

class UniversityListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUniversityListBinding
    private lateinit var adapter: UniversityAdapter
    private lateinit var notificationHelper: NotificationHelper
    private var universities = mutableListOf<University>()
    private var fetchJob: Job? = null

    companion object {
        private const val TAG = "UniversityListActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUniversityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationHelper = NotificationHelper(this)
        setupToolbar()
        setupRecyclerView()
        setupSearch()
        setupClearButton()
        checkNotificationPermission()
        loadUniversities()
    }

    private fun setupRecyclerView() {
        adapter = UniversityAdapter(
            universities = universities,
            onItemClick = { university ->
                val intent = Intent(this, UniversityDetailsActivity::class.java)
                intent.putExtra("university", university)
                startActivity(intent)
            },
            onShortlistClick = { university, isShortlisted ->
                handleShortlistAction(university, isShortlisted)
            }
        )
        binding.universitiesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@UniversityListActivity)
            adapter = this@UniversityListActivity.adapter
        }
    }

    private fun handleShortlistAction(university: University, isShortlisted: Boolean) {
        try {
            if (isShortlisted) {
                ShortlistManager.addToShortlist(this, university.generalInfo.name)
                Toast.makeText(this, 
                    "${university.generalInfo.name} added to shortlist", 
                    Toast.LENGTH_SHORT).show()
            } else {
                ShortlistManager.removeFromShortlist(this, university.generalInfo.name)
                Toast.makeText(this, 
                    "${university.generalInfo.name} removed from shortlist", 
                    Toast.LENGTH_SHORT).show()
            }

            adapter.updateShortlistedUniversities(
                ShortlistManager.getShortlistedUniversities(this)
            )

            notificationHelper.showShortlistNotification(university, isShortlisted)

        } catch (e: Exception) {
            Log.e(TAG, "Error handling shortlist action", e)
            Toast.makeText(this, "Error updating shortlist", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Universities"
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun loadUniversities() {
        showLoading()
        fetchJob?.cancel()
        fetchJob = lifecycleScope.launch(Dispatchers.Main) {
            try {
                val result = withContext(Dispatchers.IO) {
                    FirebaseHelper.fetchUniversities()
                }
                result.fold(
                    onSuccess = { universityList ->
                        if (universityList.isEmpty()) {
                            Log.d(TAG, "No universities found")
                            Toast.makeText(this@UniversityListActivity, 
                                "No universities found", 
                                Toast.LENGTH_LONG).show()
                        } else {
                            Log.d(TAG, "Loaded ${universityList.size} universities")
                            universities.clear()
                            universities.addAll(universityList)
                            adapter.updateList(universities)
                        }
                        hideLoading()
                    },
                    onFailure = { exception ->
                        Log.e(TAG, "Error loading universities", exception)
                        Toast.makeText(this@UniversityListActivity, 
                            "Error: ${exception.localizedMessage}", 
                            Toast.LENGTH_LONG).show()
                        hideLoading()
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error in coroutine", e)
                Toast.makeText(this@UniversityListActivity, 
                    "Error: ${e.localizedMessage}", 
                    Toast.LENGTH_LONG).show()
                hideLoading()
            }
        }
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterUniversities(s?.toString() ?: "")
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupClearButton() {
        binding.clearButton.setOnClickListener {
            binding.searchEditText.text?.clear()
            adapter.updateList(universities)
        }
    }

    private fun filterUniversities(query: String) {
        val filteredList = if (query.isEmpty()) {
            universities
        } else {
            universities.filter { it.generalInfo.name.contains(query, ignoreCase = true) }
        }
        adapter.updateList(filteredList)
    }

    private fun showLoading() {
        binding.loadingProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingProgressBar.visibility = View.GONE
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.updateShortlistedUniversities(
            ShortlistManager.getShortlistedUniversities(this)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        fetchJob?.cancel()
        universities.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notifications -> {
                startActivity(Intent(this, NotificationListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}