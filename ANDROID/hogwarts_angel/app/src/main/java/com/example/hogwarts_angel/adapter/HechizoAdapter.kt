package com.example.hogwarts_angel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hogwarts_angel.databinding.ItemHechizoBinding
import com.example.hogwarts_angel.model.Hechizo

class HechizoViewHolder(private val binding: ItemHechizoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        hechizo: Hechizo,
        onEditClick: (Hechizo) -> Unit,
        onDeleteClick: (Hechizo) -> Unit
    ) {
        binding.tvNombreHechizo.text = hechizo.nombre
        binding.tvDescripcionHechizo.text = hechizo.descripcion

        binding.btnEditar.setOnClickListener { onEditClick(hechizo) }
        binding.btnEliminar.setOnClickListener { onDeleteClick(hechizo) }
    }
}

class HechizoAdapter(
    private val onEditClick: (Hechizo) -> Unit,
    private val onDeleteClick: (Hechizo) -> Unit
) : ListAdapter<Hechizo, HechizoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HechizoViewHolder {
        val binding = ItemHechizoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HechizoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HechizoViewHolder, position: Int) {
        val hechizo = getItem(position)
        holder.bind(hechizo, onEditClick, onDeleteClick)
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
