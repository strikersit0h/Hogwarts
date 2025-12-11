package com.example.hogwarts_angel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hogwarts_angel.databinding.ItemHechizoAValidarBinding
import com.example.hogwarts_angel.model.Hechizo

class ProfesorHechizoViewHolder(private val binding: ItemHechizoAValidarBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(hechizo: Hechizo, onDeleteClick: (Hechizo) -> Unit) {
        binding.tvNombreHechizo.text = hechizo.nombre
        binding.tvPuntosExperiencia.text = "EXP: ${hechizo.puntos_experiencia}"

        binding.btnEliminar.setOnClickListener { onDeleteClick(hechizo) }
    }
}

class ProfesorHechizoAdapter(
    private val onDeleteClick: (Hechizo) -> Unit
) : ListAdapter<Hechizo, ProfesorHechizoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfesorHechizoViewHolder {
        val binding = ItemHechizoAValidarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfesorHechizoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfesorHechizoViewHolder, position: Int) {
        val hechizo = getItem(position)
        holder.bind(hechizo, onDeleteClick)
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
