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
import com.example.hogwarts_angel.adapter.PocimaAdapter
import com.example.hogwarts_angel.databinding.FragmentGestionarPocimasBinding
import com.example.hogwarts_angel.model.Pocima
import com.example.hogwarts_angel.viewmodels.GestionarPocimasViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class GestionarPocimasFragment : Fragment() {

    private var _binding: FragmentGestionarPocimasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GestionarPocimasViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGestionarPocimasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pocimasAdapter = PocimaAdapter(
            onEditClick = ::mostrarDialogoCrearEditar,
            onDeleteClick = ::mostrarDialogoEliminar
        )

        binding.rvPocimas.adapter = pocimasAdapter
        binding.rvPocimas.layoutManager = LinearLayoutManager(requireContext())

        binding.fabAgregarPocima.setOnClickListener {
            mostrarDialogoCrearEditar(null)
        }

        viewModel.pocimas.observe(viewLifecycleOwner) { pocimas ->
            pocimasAdapter.submitList(pocimas)
        }

        viewModel.operacionExitosa.observe(viewLifecycleOwner) { exito ->
            if (exito) {
                Toast.makeText(context, "Operación completada", Toast.LENGTH_SHORT).show()
                viewModel.fetchPocimas()
            }
        }

        viewModel.fetchPocimas()
    }

    private fun mostrarDialogoCrearEditar(pocima: Pocima?) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_crear_editar_pocima, null)
        val etNombre = dialogView.findViewById<EditText>(R.id.etNombrePocima)
        val etIdCreador = dialogView.findViewById<EditText>(R.id.etIdCreador)
        val switchValidada = dialogView.findViewById<SwitchMaterial>(R.id.switchValidada)

        val esParaCrear = pocima == null

        if (!esParaCrear) {
            etNombre.setText(pocima.nombre)
            etIdCreador.setText(pocima.id_creador.toString())
            switchValidada.isChecked = pocima.validada
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (esParaCrear) "Crear Pócima" else "Editar Pócima")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val idCreador = etIdCreador.text.toString().toIntOrNull() ?: 0
                val validada = switchValidada.isChecked

                if (nombre.isNotBlank() && idCreador > 0) {
                    if (esParaCrear) {
                        viewModel.crearPocima(Pocima(nombre = nombre, id_creador = idCreador, validada = validada))
                    } else {
                        pocima.id?.let {
                            val pocimaActualizada = pocima.copy(nombre = nombre, id_creador = idCreador, validada = validada)
                            viewModel.actualizarPocima(it, pocimaActualizada)
                        }
                    }
                } else {
                    Toast.makeText(context, "Nombre e ID del creador son obligatorios", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEliminar(pocima: Pocima) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Pócima")
            .setMessage("¿Estás seguro de que quieres eliminar '${pocima.nombre}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                pocima.id?.let { viewModel.eliminarPocima(it) }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
