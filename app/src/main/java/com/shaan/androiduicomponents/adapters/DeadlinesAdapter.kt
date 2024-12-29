package com.shaan.androiduicomponents
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.shaan.androiduicomponents.models.Deadline
import java.text.SimpleDateFormat
import java.util.Locale
class DeadlineAdapter : RecyclerView.Adapter<DeadlineAdapter.ViewHolder>() {
    private var deadlines: List<Deadline> = emptyList()

    companion object {
        private const val TAG = "DeadlineAdapter"
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val universityNameText: TextView = itemView.findViewById(R.id.universityNameText)
        private val deadlineTypeText: TextView = itemView.findViewById(R.id.deadlineTypeText)
        private val dateText: TextView = itemView.findViewById(R.id.dateText)
        private val timeText: TextView = itemView.findViewById(R.id.applicationDeadlineTime)

        fun bind(deadline: Deadline) {
            try {
                universityNameText.text = deadline.universityName
                deadlineTypeText.text = deadline.eventType

                val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .parse(deadline.date)

                if (date != null) {
                    val day = SimpleDateFormat("dd", Locale.getDefault()).format(date)
                    val month = SimpleDateFormat("MMM", Locale.getDefault()).format(date)
                    dateText.text = "$day\n${month.uppercase()}"
                }

                timeText.text = deadline.time

                val cardColor = when {
                    deadline.isOngoing() -> ContextCompat.getColor(itemView.context, R.color.status_pending)
                    deadline.isUpcoming() -> ContextCompat.getColor(itemView.context, R.color.status_upcoming)
                    else -> ContextCompat.getColor(itemView.context, R.color.status_completed)
                }
                itemView.findViewById<MaterialCardView>(R.id.dateCard).setCardBackgroundColor(cardColor)

                if (deadline.isOngoing()) {
                    dateText.startAnimation(
                        AnimationUtils.loadAnimation(itemView.context, R.anim.blink)
                    )
                } else {
                    dateText.clearAnimation()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error binding deadline data", e)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return try {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_deadline, parent, false)
            ViewHolder(view)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating ViewHolder", e)
            throw e
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.bind(deadlines[position])
        } catch (e: Exception) {
            Log.e(TAG, "Error binding ViewHolder", e)
        }
    }

    override fun getItemCount() = deadlines.size

    fun updateDeadlines(newDeadlines: List<Deadline>) {
        Log.d(TAG, "Updating deadlines list with ${newDeadlines.size} items")
        deadlines = newDeadlines
        notifyDataSetChanged()
    }
}