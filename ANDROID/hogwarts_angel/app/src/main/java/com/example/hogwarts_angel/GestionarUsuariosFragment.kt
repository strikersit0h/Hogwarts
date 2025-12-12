package com.example.hogwarts_angel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hogwarts_angel.adapter.AdminGestionUsuarioAdapter
import com.example.hogwarts_angel.databinding.FragmentGestionarUsuariosBinding
import com.example.hogwarts_angel.model.UsuarioDetallado
import com.example.hogwarts_angel.model.UsuarioModificar
import com.example.hogwarts_angel.viewmodels.GestionarUsuariosViewModel

class GestionarUsuariosFragment : Fragment() {

    private var _binding: FragmentGestionarUsuariosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GestionarUsuariosViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGestionarUsuariosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usuariosAdapter = AdminGestionUsuarioAdapter(
            onUsuarioClick = ::mostrarDialogoRoles,
            onUsuarioLongClick = ::mostrarDialogoNivelExp
        )

        binding.rvUsuarios.adapter = usuariosAdapter
        binding.rvUsuarios.layoutManager = LinearLayoutManager(requireContext())

        viewModel.usuarios.observe(viewLifecycleOwner) { usuarios ->
            usuariosAdapter.submitList(usuarios)
        }

        viewModel.actualizacionExitosa.observe(viewLifecycleOwner) { exito ->
            if (exito) {
                Toast.makeText(context, "Usuario actualizado correctamente", Toast.LENGTH_SHORT).show()
                viewModel.fetchUsuarios()
            }
        }

        viewModel.fetchUsuarios()
    }

    private fun mostrarDialogoRoles(usuario: UsuarioDetallado) {
        val rolesDisponibles = arrayOf("Administrador", "Profesor", "Usuario")
        val idRolesDisponibles = arrayOf(2, 4, 3)

        val rolesActualesCheck = BooleanArray(rolesDisponibles.size) {
            usuario.id_roles.contains(idRolesDisponibles[it])
        }

        val rolesSeleccionados = usuario.id_roles.toMutableList()

        AlertDialog.Builder(requireContext())
            .setTitle("Gestionar roles de ${usuario.nombre}")
            .setMultiChoiceItems(rolesDisponibles, rolesActualesCheck) { _, which, isChecked ->
                val idRol = idRolesDisponibles[which]
                if (isChecked) rolesSeleccionados.add(idRol) else rolesSeleccionados.remove(idRol)
            }
            .setPositiveButton("Guardar") { _, _ ->
                usuario.id?.let { viewModel.actualizarRoles(it, rolesSeleccionados.distinct()) }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoNivelExp(usuario: UsuarioDetallado) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_modificar_nivel_exp, null)
        val etNivel = dialogView.findViewById<EditText>(R.id.etNivel)
        val etExperiencia = dialogView.findViewById<EditText>(R.id.etExperiencia)

        etNivel.setText(usuario.nivel.toString())
        etExperiencia.setText(usuario.experiencia.toString())

        AlertDialog.Builder(requireContext())
            .setTitle("Modificar Nivel/EXP de ${usuario.nombre}")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoNivel = etNivel.text.toString().toIntOrNull() ?: usuario.nivel
                val nuevaExp = etExperiencia.text.toString().toIntOrNull() ?: usuario.experiencia

                usuario.id?.let {
                    val usuarioModificado = UsuarioModificar(it, usuario.nombre, nuevoNivel, nuevaExp)
                    viewModel.modificarUsuario(usuarioModificado)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
