package com.example.hogwarts_angel.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hogwarts_angel.ModificarUsuarioActivity
import com.example.hogwarts_angel.R
import com.example.hogwarts_angel.UsuarioDataHolder
import com.example.hogwarts_angel.databinding.ItemUsuarioBinding
import com.example.hogwarts_angel.model.UsuarioDetallado
import com.example.hogwarts_angel.viewmodels.DumbledoreActivityViewModel

class UsuarioViewHolder(private val binding: ItemUsuarioBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        usuario: UsuarioDetallado,
        nombresRoles: String,
        colorRes: Int,
        viewModel: DumbledoreActivityViewModel
    ) {

        binding.tvNombreUsuario.text = usuario.nombre
        binding.tvExperiencia.text = "Nivel: ${usuario.nivel} | EXP: ${usuario.experiencia}"

        binding.tvRol.text = nombresRoles
        binding.viewHouseColor.setBackgroundResource(colorRes)

        binding.root.setOnClickListener {
            val context = binding.root.context
            UsuarioDataHolder.usuarioId = usuario.id // Guardamos solo el ID
            val intent = Intent(context, ModificarUsuarioActivity::class.java)
            context.startActivity(intent)
        }

        binding.root.setOnLongClickListener {
            try {
                viewModel.deleteUsuario(usuario.id)
                Toast.makeText(binding.root.context, "Usuario ${usuario.nombre} eliminado.", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(binding.root.context, e.message, Toast.LENGTH_SHORT).show()
            }
            true
        }
    }
}

class GestionUsuarioAdapter(
    private val viewModel: DumbledoreActivityViewModel // Recibe el ViewModel
) : ListAdapter<UsuarioDetallado, UsuarioViewHolder>(DiffCallback()) {

    private val mapaRoles = mapOf(
        1 to "Dumbledore", 2 to "Administrador", 3 to "Usuario", 4 to "Profesor"
    )
    private val mapaColoresCasa = mapOf(
        1 to R.color.colorGryffindor, 2 to R.color.colorSlytherin,
        3 to R.color.colorHufflepuff, 4 to R.color.colorRavenclaw
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val binding = ItemUsuarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsuarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val usuario = getItem(position)

        val nombresRoles = usuario.id_roles.joinToString(separator = ", ") { id -> mapaRoles[id] ?: "Desconocido" }

        val colorRes = mapaColoresCasa[usuario.id_casa] ?: R.color.colorDefault

        holder.bind(usuario, nombresRoles, colorRes, viewModel) // Pasa el ViewModel
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
