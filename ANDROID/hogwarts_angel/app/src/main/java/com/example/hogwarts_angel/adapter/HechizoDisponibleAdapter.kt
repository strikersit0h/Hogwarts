package com.example.hogwarts_angel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hogwarts_angel.databinding.ItemHechizoDisponibleBinding
import com.example.hogwarts_angel.model.Hechizo

class HechizoDisponibleViewHolder(private val binding: ItemHechizoDisponibleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(hechizo: Hechizo) {
        binding.tvNombreHechizo.text = hechizo.nombre
        binding.tvDescripcionHechizo.text = hechizo.descripcion
        binding.tvPuntosExperiencia.text = "${hechizo.puntos_experiencia} EXP"
    }
}

class HechizoDisponibleAdapter : ListAdapter<Hechizo, HechizoDisponibleViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HechizoDisponibleViewHolder {
        val binding = ItemHechizoDisponibleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HechizoDisponibleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HechizoDisponibleViewHolder, position: Int) {
        val hechizo = getItem(position)
        holder.bind(hechizo)
    }

    class DiffCallback : DiffUtil.ItemCallback<Hechizo>() {
        override fun areItemsTheSame(oldItem: Hechizo, newItem: Hechizo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Hechizo, newItem: Hechizo): Boolean {
            return oldItem == newItem
        }
    }
}
