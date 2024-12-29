package com.shaan.androiduicomponents
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.shaan.androiduicomponents.models.University

class UniversityAdapter(
    private var universities: List<University>,
    private val onItemClick: (University) -> Unit,
    private val onShortlistClick: (University, Boolean) -> Unit,
    private val showShortlistButton: Boolean = true
) : RecyclerView.Adapter<UniversityAdapter.ViewHolder>() {

    private val shortlistedUniversities = mutableSetOf<String>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.universityNameText)
        val locationText: TextView = view.findViewById(R.id.locationText)
        val typeText: TextView = view.findViewById(R.id.universityTypeText)
        val shortlistButton: ImageButton = view.findViewById(R.id.shortlistButton)

        init {
            nameText.setTextColor(view.context.getColor(R.color.colorCustomColor1))
            nameText.isClickable = true
            nameText.isFocusable = true
            nameText.background = view.context.getDrawable(R.drawable.ripple_effect)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val university = universities[position]
        holder.nameText.text = university.generalInfo.name
        holder.locationText.text = university.generalInfo.location
        holder.typeText.text = university.generalInfo.universityType

        holder.nameText.setOnClickListener {
            onItemClick(university)
        }

        holder.shortlistButton.visibility = if (showShortlistButton) View.VISIBLE else View.GONE
        if (showShortlistButton) {
            val isShortlisted = shortlistedUniversities.contains(university.generalInfo.name)
            updateShortlistButton(holder.shortlistButton, isShortlisted)

            holder.shortlistButton.setOnClickListener {
                val newState = !isShortlisted
                if (newState) {
                    shortlistedUniversities.add(university.generalInfo.name)
                } else {
                    shortlistedUniversities.remove(university.generalInfo.name)
                }
                updateShortlistButton(holder.shortlistButton, newState)
                onShortlistClick(university, newState)
            }
        }
    }

    private fun updateShortlistButton(button: ImageButton, isShortlisted: Boolean) {
        button.setImageResource(
            if (isShortlisted) R.drawable.ic_bookmark_filled
            else R.drawable.ic_bookmark_border
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_university, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = universities.size

    fun updateList(newList: List<University>) {
        universities = newList
        notifyDataSetChanged()
    }

    fun updateShortlistedUniversities(shortlisted: Set<String>) {
        shortlistedUniversities.clear()
        shortlistedUniversities.addAll(shortlisted)
        notifyDataSetChanged()
    }
}