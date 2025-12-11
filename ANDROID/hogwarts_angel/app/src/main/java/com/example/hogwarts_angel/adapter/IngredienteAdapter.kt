package com.example.hogwarts_angel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hogwarts_angel.databinding.ItemIngredienteSeleccionableBinding
import com.example.hogwarts_angel.model.Ingrediente

class IngredienteViewHolder(private val binding: ItemIngredienteSeleccionableBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        ingrediente: Ingrediente,
        isSelected: Boolean,
        onItemClick: (Ingrediente) -> Unit
    ) {
        binding.tvNombreIngrediente.text = ingrediente.nombre
        binding.root.isActivated = isSelected

        binding.root.setOnClickListener { onItemClick(ingrediente) }
    }
}

class IngredienteAdapter(
    private val onIngredienteClick: (Ingrediente) -> Unit
) : ListAdapter<Ingrediente, IngredienteViewHolder>(DiffCallback()) {

    private var seleccionados = setOf<Int>()

    fun setSeleccionados(nuevosSeleccionados: Set<Int>) {
        seleccionados = nuevosSeleccionados
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredienteViewHolder {
        val binding = ItemIngredienteSeleccionableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredienteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredienteViewHolder, position: Int) {
        val ingrediente = getItem(position)
        holder.bind(ingrediente, seleccionados.contains(ingrediente.id), onIngredienteClick)
    }

    class DiffCallback : DiffUtil.ItemCallback<Ingrediente>() {
        override fun areItemsTheSame(oldItem: Ingrediente, newItem: Ingrediente): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ingrediente, newItem: Ingrediente): Boolean {
            return oldItem == newItem
        }
    }
}
