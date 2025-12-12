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
import com.example.hogwarts_angel.adapter.AsignaturaAdapter
import com.example.hogwarts_angel.databinding.FragmentGestionarAsignaturasBinding
import com.example.hogwarts_angel.model.Asignatura
import com.example.hogwarts_angel.viewmodels.GestionarAsignaturasViewModel

class GestionarAsignaturasFragment : Fragment() {

    private var _binding: FragmentGestionarAsignaturasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GestionarAsignaturasViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentGestionarAsignaturasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val asignaturasAdapter = AsignaturaAdapter(
            onEditClick = ::mostrarDialogoCrearEditar,
            onDeleteClick = ::mostrarDialogoEliminar
        )

        binding.rvAsignaturas.adapter = asignaturasAdapter
        binding.rvAsignaturas.layoutManager = LinearLayoutManager(requireContext())

        binding.fabAgregarAsignatura.setOnClickListener {
            mostrarDialogoCrearEditar(null)
        }

        // viewLifecycleOwner me permite interactuar con el ciclo de vida del fragmento porque el fragmento y su vista no son lo mismo.
        viewModel.asignaturas.observe(viewLifecycleOwner) { asignaturas ->
            asignaturasAdapter.submitList(asignaturas)
        }

        viewModel.operacionExitosa.observe(viewLifecycleOwner) { exito ->
            if (exito) {
                Toast.makeText(context, "Operación completada", Toast.LENGTH_SHORT).show()
                viewModel.fetchAsignaturas()
            }
        }

        viewModel.fetchAsignaturas()
    }

    private fun mostrarDialogoCrearEditar(asignatura: Asignatura?) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_crear_editar_asignatura, null)
        val etNombre = dialogView.findViewById<EditText>(R.id.etNombreAsignatura)
        val etDescripcion = dialogView.findViewById<EditText>(R.id.etDescripcionAsignatura)

        val esParaCrear = asignatura == null

        if (!esParaCrear) {
            etNombre.setText(asignatura.nombre)
            etDescripcion.setText(asignatura.descripcion)
        }

        AlertDialog.Builder(requireContext())
            .setTitle(if (esParaCrear) "Crear Asignatura" else "Editar Asignatura")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val descripcion = etDescripcion.text.toString()

                if (nombre.isNotBlank() && descripcion.isNotBlank()) {
                    if (esParaCrear) {
                        viewModel.crearAsignatura(Asignatura(0, nombre, descripcion))
                    } else {
                        viewModel.actualizarAsignatura(asignatura.id, asignatura.copy(nombre = nombre, descripcion = descripcion))
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEliminar(asignatura: Asignatura) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Asignatura")
            .setMessage("¿Estás seguro de que quieres eliminar '${asignatura.nombre}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminarAsignatura(asignatura.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
