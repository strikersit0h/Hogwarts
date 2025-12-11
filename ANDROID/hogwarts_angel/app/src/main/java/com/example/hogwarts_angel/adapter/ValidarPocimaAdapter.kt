package com.example.hogwarts_angel.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hogwarts_angel.databinding.ItemPocimaAValidarBinding
import com.example.hogwarts_angel.model.Pocima

class ValidarPocimaViewHolder(private val binding: ItemPocimaAValidarBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        pocima: Pocima,
        onApproveClick: (Pocima) -> Unit,
        onRejectClick: (Pocima) -> Unit
    ) {
        binding.tvNombrePocima.text = pocima.nombre
        binding.tvCreadorPocima.text = "Creador ID: ${pocima.id_creador}"

        if (pocima.validada) {
            binding.tvEstadoValidacion.text = "Estado: Aprobada"
            binding.tvEstadoValidacion.setTextColor(Color.parseColor("#008000")) // Verde
        } else {
            binding.tvEstadoValidacion.text = "Estado: Pendiente"
            binding.tvEstadoValidacion.setTextColor(Color.parseColor("#FFA500")) // Naranja
        }

        binding.btnAprobar.setOnClickListener { onApproveClick(pocima) }
        binding.btnRechazar.setOnClickListener { onRejectClick(pocima) }
    }
}

class ValidarPocimaAdapter(
    private val onApproveClick: (Pocima) -> Unit,
    private val onRejectClick: (Pocima) -> Unit
) : ListAdapter<Pocima, ValidarPocimaViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValidarPocimaViewHolder {
        val binding = ItemPocimaAValidarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ValidarPocimaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ValidarPocimaViewHolder, position: Int) {
        val pocima = getItem(position)
        holder.bind(pocima, onApproveClick, onRejectClick)
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
