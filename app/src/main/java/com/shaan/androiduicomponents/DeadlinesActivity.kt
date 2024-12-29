package com.shaan.androiduicomponents
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.shaan.androiduicomponents.databinding.ActivityDeadlinesBinding
import com.shaan.androiduicomponents.models.Deadline
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DeadlinesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeadlinesBinding
    private lateinit var adapter: DeadlineAdapter
    private var fetchJob: Job? = null

    companion object {
        private const val TAG = "DeadlinesActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Starting DeadlinesActivity")
        
        try {
            binding = ActivityDeadlinesBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setupToolbar()
            setupRecyclerView()
            loadDeadlines()
        } catch (e: Exception) {
            Log.e(TAG, "onCreate: Error initializing activity", e)
            Toast.makeText(this, "Error initializing deadlines", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupToolbar() {
        Log.d(TAG, "setupToolbar: Setting up toolbar")
        try {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
                title = "Admission Deadlines"
            }
            binding.toolbar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
        } catch (e: Exception) {
            Log.e(TAG, "setupToolbar: Failed to setup toolbar", e)
            throw e
        }
    }

    private fun setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView: Setting up RecyclerView")
        try {
            adapter = DeadlineAdapter()
            binding.deadlinesRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@DeadlinesActivity)
                adapter = this@DeadlinesActivity.adapter
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        } catch (e: Exception) {
            Log.e(TAG, "setupRecyclerView: Failed to setup RecyclerView", e)
            throw e
        }
    }

    private fun loadDeadlines() {
        Log.d(TAG, "loadDeadlines: Starting to fetch deadlines from Firebase")
        showLoading()
        
        fetchJob?.cancel()
        fetchJob = CoroutineScope(Dispatchers.Main).launch {
            try {
                val db = FirebaseFirestore.getInstance()
                val universities = db.collection("universitylist")
                    .get()
                    .await()
                    .documents
                    .mapNotNull { doc ->
                        try {
                            val admissionInfo = doc.get("admissionInfo") as? Map<*, *>
                            val generalInfo = doc.get("generalInfo") as? Map<*, *>
                            val testDetails = (admissionInfo?.get("admissionTestDetails") as? Map<*, *>)

                            if (testDetails != null && generalInfo != null) {
                                Deadline(
                                    universityName = generalInfo["name"]?.toString() ?: "",
                                    eventType = "Admission Test",
                                    date = testDetails["date"]?.toString() ?: "",
                                    time = testDetails["time"]?.toString() ?: ""
                                )
                            } else null
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing document", e)
                            null
                        }
                    }

                if (universities.isEmpty()) {
                    showEmptyState()
                } else {
                    hideEmptyState()
                    val sortedDeadlines = universities.sortedWith(
                        compareBy<Deadline> {
                            when {
                                it.isOngoing() -> 0
                                it.isUpcoming() -> 1
                                else -> 2
                            }
                        }.thenBy { it.date }
                    )
                    adapter.updateDeadlines(sortedDeadlines)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error loading deadlines", e)
                Toast.makeText(
                    this@DeadlinesActivity,
                    "Error loading deadlines: ${e.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
                showEmptyState()
            } finally {
                hideLoading()
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.deadlinesRecyclerView.visibility = View.GONE
        binding.emptyStateView.visibility = View.GONE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        binding.deadlinesRecyclerView.visibility = View.VISIBLE
    }

    private fun showEmptyState() {
        binding.emptyStateView.visibility = View.VISIBLE
        binding.deadlinesRecyclerView.visibility = View.GONE
    }

    private fun hideEmptyState() {
        binding.emptyStateView.visibility = View.GONE
        binding.deadlinesRecyclerView.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        fetchJob?.cancel()
    }
}
