package com.souravpalitrana.androiduicomponents
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UniversityAdapter(
    private var universities: List<University>,
    private val onItemClicked: (University) -> Unit
) : RecyclerView.Adapter<UniversityAdapter.UniversityViewHolder>() {

    inner class UniversityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val universityNameTextView: TextView = itemView.findViewById(R.id.universityNameTextView)
        fun bind(university: University) {
            universityNameTextView.text = university.name
            itemView.setOnClickListener {
                onItemClicked(university)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_university, parent, false)
        return UniversityViewHolder(view)
    }

    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {
        holder.bind(universities[position])
    }

    override fun getItemCount(): Int = universities.size

    fun updateList(newList: List<University>) {
        universities = newList
        notifyDataSetChanged()
    }

}