package com.example.fitness_app_kp.exercise

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fitness_app_kp.R
import com.example.fitness_app_kp.databinding.ItemExerciseStatusBinding

class ExerciseAdapter(
    private val items: ArrayList<ExerciseModel>,
    private val context: Context
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemExerciseStatusBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = ItemExerciseStatusBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = items[position]

        holder.binding.itemTv.text = model.getId().toString()

        if (model.getIsSelected()) {

            holder.binding.itemTv.background =
                ContextCompat.getDrawable(
                    context,
                    R.drawable.item_circular_thin_color_border
                )

            holder.binding.itemTv.setTextColor(Color.parseColor("#212121"))

        } else if (model.getIsCompleted()) {

            holder.binding.itemTv.background =
                ContextCompat.getDrawable(
                    context,
                    R.drawable.circular_color_background
                )

            holder.binding.itemTv.setTextColor(Color.WHITE)

        } else {

            holder.binding.itemTv.background =
                ContextCompat.getDrawable(
                    context,
                    R.drawable.item_circular_color_gray_bg
                )

            holder.binding.itemTv.setTextColor(Color.parseColor("#212121"))
        }
    }
}