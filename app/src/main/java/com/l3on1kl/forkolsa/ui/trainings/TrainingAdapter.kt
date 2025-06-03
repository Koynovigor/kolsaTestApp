package com.l3on1kl.forkolsa.ui.trainings

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.l3on1kl.forkolsa.R
import com.l3on1kl.forkolsa.domain.model.Training

class TrainingAdapter : RecyclerView.Adapter<TrainingAdapter.TrainingViewHolder>() {

    private val items = mutableListOf<Training>()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: List<Training>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_training, parent, false)
        return TrainingViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class TrainingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title = view.findViewById<TextView>(R.id.title)
        private val type = view.findViewById<TextView>(R.id.type)
        private val duration = view.findViewById<TextView>(R.id.duration)
        private val description = view.findViewById<TextView>(R.id.description)

        fun bind(training: Training) {
            title.text = training.title
            type.text = when (training.type) {
                1 -> "Тренировка"
                2 -> "Эфир"
                3 -> "Комплекс"
                else -> "Неизвестно"
            }
            duration.text = "Длительность: ${training.duration} мин"
            description.text = training.description
        }
    }
}