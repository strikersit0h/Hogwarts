package com.example.hogwarts_angel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hogwarts_angel.databinding.ItemAsignaturaBinding
import com.example.hogwarts_angel.model.Asignatura

class AsignaturaViewHolder(private val binding: ItemAsignaturaBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        asignatura: Asignatura,
        onEditClick: (Asignatura) -> Unit,
        onDeleteClick: (Asignatura) -> Unit
    ) {
        binding.tvNombreAsignatura.text = asignatura.nombre
        binding.tvDescripcionAsignatura.text = asignatura.descripcion

        binding.btnEditar.setOnClickListener { onEditClick(asignatura) }
        binding.btnEliminar.setOnClickListener { onDeleteClick(asignatura) }
    }
}

class AsignaturaAdapter(
    private val onEditClick: (Asignatura) -> Unit,
    private val onDeleteClick: (Asignatura) -> Unit
) : ListAdapter<Asignatura, AsignaturaViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsignaturaViewHolder {
        val binding = ItemAsignaturaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AsignaturaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AsignaturaViewHolder, position: Int) {
        val asignatura = getItem(position)
        holder.bind(asignatura, onEditClick, onDeleteClick)
    }

    class DiffCallback : DiffUtil.ItemCallback<Asignatura>() {
        override fun areItemsTheSame(oldItem: Asignatura, newItem: Asignatura): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asignatura, newItem: Asignatura): Boolean {
            return oldItem == newItem
        }
    }
}
