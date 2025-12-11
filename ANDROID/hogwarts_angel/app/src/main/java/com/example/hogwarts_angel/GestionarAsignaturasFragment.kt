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
import com.example.hogwarts_angel.adapter.AsignaturaAdapter
import com.example.hogwarts_angel.databinding.FragmentGestionarAsignaturasBinding
import com.example.hogwarts_angel.model.Asignatura
import com.example.hogwarts_angel.viewmodels.GestionarAsignaturasViewModel

class GestionarAsignaturasFragment : Fragment() {

    private var _binding: FragmentGestionarAsignaturasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GestionarAsignaturasViewModel by viewModels()
    private lateinit var adapter: AsignaturaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGestionarAsignaturasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.asignaturas.observe(viewLifecycleOwner, Observer { asignaturas ->
            adapter.submitList(asignaturas)
        })

        viewModel.operacionExitosa.observe(viewLifecycleOwner, Observer { exito ->
            if (exito) {
                Toast.makeText(context, "Operación completada", Toast.LENGTH_SHORT).show()
                viewModel.fetchAsignaturas()
            }
        })

        binding.fabAgregarAsignatura.setOnClickListener {
            mostrarDialogoCrearEditar(null)
        }

        viewModel.fetchAsignaturas()
    }

    private fun setupRecyclerView() {
        adapter = AsignaturaAdapter(
            onEditClick = { asignatura ->
                mostrarDialogoCrearEditar(asignatura)
            },
            onDeleteClick = { asignatura ->
                mostrarDialogoEliminar(asignatura)
            }
        )
        binding.rvAsignaturas.adapter = adapter
        binding.rvAsignaturas.layoutManager = LinearLayoutManager(context)
    }

    private fun mostrarDialogoCrearEditar(asignatura: Asignatura?) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_crear_editar_asignatura, null)
        val etNombre = dialogView.findViewById<EditText>(R.id.etNombreAsignatura)
        val etDescripcion = dialogView.findViewById<EditText>(R.id.etDescripcionAsignatura)

        val dialogTitle = if (asignatura == null) "Crear Asignatura" else "Editar Asignatura"
        etNombre.setText(asignatura?.nombre ?: "")
        etDescripcion.setText(asignatura?.descripcion ?: "")

        AlertDialog.Builder(requireContext())
            .setTitle(dialogTitle)
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val descripcion = etDescripcion.text.toString()
                if (nombre.isNotBlank() && descripcion.isNotBlank()) {
                    if (asignatura == null) {
                        viewModel.crearAsignatura(Asignatura(0, nombre, descripcion))
                    } else {
                        viewModel.actualizarAsignatura(asignatura.id, Asignatura(asignatura.id, nombre, descripcion))
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
