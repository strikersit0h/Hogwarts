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
import androidx.lifecycle.Observer
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
    private lateinit var adapter: PocimaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGestionarPocimasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.pocimas.observe(viewLifecycleOwner, Observer { pocimas ->
            adapter.submitList(pocimas)
        })

        viewModel.operacionExitosa.observe(viewLifecycleOwner, Observer { exito ->
            if (exito) {
                Toast.makeText(context, "Operación completada", Toast.LENGTH_SHORT).show()
                viewModel.fetchPocimas()
            }
        })

        binding.fabAgregarPocima.setOnClickListener {
            mostrarDialogoCrearEditar(null)
        }

        viewModel.fetchPocimas()
    }

    private fun setupRecyclerView() {
        adapter = PocimaAdapter(
            onEditClick = { pocima ->
                mostrarDialogoCrearEditar(pocima)
            },
            onDeleteClick = { pocima ->
                mostrarDialogoEliminar(pocima)
            }
        )
        binding.rvPocimas.adapter = adapter
        binding.rvPocimas.layoutManager = LinearLayoutManager(context)
    }

    private fun mostrarDialogoCrearEditar(pocima: Pocima?) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_crear_editar_pocima, null)
        val etNombre = dialogView.findViewById<EditText>(R.id.etNombrePocima)
        val etIdCreador = dialogView.findViewById<EditText>(R.id.etIdCreador)
        val switchValidada = dialogView.findViewById<SwitchMaterial>(R.id.switchValidada)

        val dialogTitle = if (pocima == null) "Crear Pócima" else "Editar Pócima"
        etNombre.setText(pocima?.nombre ?: "")
        etIdCreador.setText(pocima?.id_creador?.toString() ?: "")
        switchValidada.isChecked = pocima?.validada ?: false

        AlertDialog.Builder(requireContext())
            .setTitle(dialogTitle)
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val idCreadorStr = etIdCreador.text.toString()
                val validada = switchValidada.isChecked

                if (nombre.isNotBlank() && idCreadorStr.isNotBlank()) {
                    val idCreador = idCreadorStr.toInt()
                    if (pocima == null) {
                        viewModel.crearPocima(Pocima(nombre = nombre, id_creador = idCreador, validada = validada))
                    } else {
                        val pocimaActualizada = pocima.copy(nombre = nombre, id_creador = idCreador, validada = validada)
                        viewModel.actualizarPocima(pocima.id!!, pocimaActualizada)
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
