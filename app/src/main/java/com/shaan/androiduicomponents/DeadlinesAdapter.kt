package com.shaan.androiduicomponents
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.shaan.androiduicomponents.models.Deadline
import java.time.LocalDate

class DeadlinesAdapter(
    private var deadlines: List<Deadline>,
    private val onItemClick: (Deadline) -> Unit
) : RecyclerView.Adapter<DeadlinesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateText: TextView = view.findViewById(R.id.dateText)
        val universityNameText: TextView = view.findViewById(R.id.universityNameText)
        val deadlineTypeText: TextView = view.findViewById(R.id.deadlineTypeText)
        val reminderButton: ImageButton = view.findViewById(R.id.reminderButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_deadline, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val deadline = deadlines[position]

        // Parse the date and format it
        val date = LocalDate.parse(deadline.date)
        holder.dateText.text = "${date.dayOfMonth}\n${date.month.name.take(3)}"

        holder.universityNameText.text = deadline.universityName
        holder.deadlineTypeText.text = deadline.type

        holder.itemView.setOnClickListener { onItemClick(deadline) }
        holder.reminderButton.setOnClickListener {
            // TODO: Implement reminder functionality
        }
    }

    override fun getItemCount() = deadlines.size

    fun updateList(newList: List<Deadline>) {
        deadlines = newList
        notifyDataSetChanged()
    }
}