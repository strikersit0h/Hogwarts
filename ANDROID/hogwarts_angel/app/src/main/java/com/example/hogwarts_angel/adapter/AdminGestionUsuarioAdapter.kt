package com.example.hogwarts_angel.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hogwarts_angel.R
import com.example.hogwarts_angel.databinding.ItemUsuarioBinding
import com.example.hogwarts_angel.model.UsuarioDetallado

class AdminUsuarioViewHolder(private val binding: ItemUsuarioBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        usuario: UsuarioDetallado, 
        nombresRoles: String, 
        colorRes: Int, 
        onUsuarioClick: (UsuarioDetallado) -> Unit,
        onUsuarioLongClick: (UsuarioDetallado) -> Unit
    ) {
        binding.tvNombreUsuario.text = usuario.nombre
        binding.tvExperiencia.text = "Nivel: ${usuario.nivel} | EXP: ${usuario.experiencia}"
        binding.tvRol.text = nombresRoles
        binding.viewHouseColor.setBackgroundResource(colorRes)

        binding.root.setOnClickListener {
            onUsuarioClick(usuario)
        }

        binding.root.setOnLongClickListener {
            onUsuarioLongClick(usuario)
            true
        }
    }
}

class AdminGestionUsuarioAdapter(
    private val onUsuarioClick: (UsuarioDetallado) -> Unit,
    private val onUsuarioLongClick: (UsuarioDetallado) -> Unit
) : ListAdapter<UsuarioDetallado, AdminUsuarioViewHolder>(DiffCallback()) {

    private val mapaRoles = mapOf(
        1 to "Dumbledore", 2 to "Administrador", 3 to "Usuario", 4 to "Profesor"
    )
    private val mapaColoresCasa = mapOf(
        1 to R.color.colorGryffindor, 2 to R.color.colorSlytherin,
        3 to R.color.colorHufflepuff, 4 to R.color.colorRavenclaw
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminUsuarioViewHolder {
        val binding = ItemUsuarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminUsuarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminUsuarioViewHolder, position: Int) {
        val usuario = getItem(position)
        val nombresRoles = usuario.id_roles.joinToString(", ") { id -> mapaRoles[id] ?: "Desconocido" }
        val colorRes = mapaColoresCasa[usuario.id_casa] ?: R.color.colorDefault
        holder.bind(usuario, nombresRoles, colorRes, onUsuarioClick, onUsuarioLongClick)
    }

    class DiffCallback : DiffUtil.ItemCallback<UsuarioDetallado>() {
        override fun areItemsTheSame(oldItem: UsuarioDetallado, newItem: UsuarioDetallado): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: UsuarioDetallado, newItem: UsuarioDetallado): Boolean {
            return oldItem == newItem
        }
    }
}
