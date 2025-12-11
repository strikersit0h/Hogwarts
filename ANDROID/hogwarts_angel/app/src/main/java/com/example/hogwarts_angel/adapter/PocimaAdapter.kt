package com.example.hogwarts_angel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hogwarts_angel.databinding.ItemPocimaBinding
import com.example.hogwarts_angel.model.Pocima

class PocimaViewHolder(private val binding: ItemPocimaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        pocima: Pocima,
        onEditClick: (Pocima) -> Unit,
        onDeleteClick: (Pocima) -> Unit
    ) {
        binding.tvNombrePocima.text = pocima.nombre

        binding.btnEditar.setOnClickListener { onEditClick(pocima) }
        binding.btnEliminar.setOnClickListener { onDeleteClick(pocima) }
    }
}

class PocimaAdapter(
    private val onEditClick: (Pocima) -> Unit,
    private val onDeleteClick: (Pocima) -> Unit
) : ListAdapter<Pocima, PocimaViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PocimaViewHolder {
        val binding = ItemPocimaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PocimaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PocimaViewHolder, position: Int) {
        val pocima = getItem(position)
        holder.bind(pocima, onEditClick, onDeleteClick)
    }

    class DiffCallback : DiffUtil.ItemCallback<Pocima>() {
        override fun areItemsTheSame(oldItem: Pocima, newItem: Pocima): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Pocima, newItem: Pocima): Boolean {
            return oldItem == newItem
        }
    }
}
