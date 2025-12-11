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
import com.example.hogwarts_angel.adapter.HechizoAdapter
import com.example.hogwarts_angel.databinding.FragmentGestionarHechizosBinding
import com.example.hogwarts_angel.model.Hechizo
import com.example.hogwarts_angel.viewmodels.GestionarHechizosViewModel

class GestionarHechizosFragment : Fragment() {

    private var _binding: FragmentGestionarHechizosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GestionarHechizosViewModel by viewModels()
    private lateinit var adapter: HechizoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGestionarHechizosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.hechizos.observe(viewLifecycleOwner, Observer { hechizos ->
            adapter.submitList(hechizos)
        })

        viewModel.operacionExitosa.observe(viewLifecycleOwner, Observer { exito ->
            if (exito) {
                Toast.makeText(context, "Operación completada", Toast.LENGTH_SHORT).show()
                viewModel.fetchHechizos()
            }
        })

        binding.fabAgregarHechizo.setOnClickListener {
            mostrarDialogoCrearEditar(null)
        }

        viewModel.fetchHechizos()
    }

    private fun setupRecyclerView() {
        adapter = HechizoAdapter(
            onEditClick = { hechizo ->
                mostrarDialogoCrearEditar(hechizo)
            },
            onDeleteClick = { hechizo ->
                mostrarDialogoEliminar(hechizo)
            }
        )
        binding.rvHechizos.adapter = adapter
        binding.rvHechizos.layoutManager = LinearLayoutManager(context)
    }

    private fun mostrarDialogoCrearEditar(hechizo: Hechizo?) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_crear_editar_hechizo, null)
        val etNombre = dialogView.findViewById<EditText>(R.id.etNombreHechizo)
        val etDescripcion = dialogView.findViewById<EditText>(R.id.etDescripcionHechizo)
        val etPuntosExperiencia = dialogView.findViewById<EditText>(R.id.etPuntosExperiencia)

        val dialogTitle = if (hechizo == null) "Crear Hechizo" else "Editar Hechizo"
        etNombre.setText(hechizo?.nombre ?: "")
        etDescripcion.setText(hechizo?.descripcion ?: "")
        etPuntosExperiencia.setText(hechizo?.puntos_experiencia?.toString() ?: "0")

        AlertDialog.Builder(requireContext())
            .setTitle(dialogTitle)
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = etNombre.text.toString()
                val descripcion = etDescripcion.text.toString()
                val puntos = etPuntosExperiencia.text.toString().toIntOrNull() ?: 0

                if (nombre.isNotBlank() && descripcion.isNotBlank()) {
                    if (hechizo == null) {
                        viewModel.crearHechizo(Hechizo(0, nombre, descripcion, puntos))
                    } else {
                        viewModel.actualizarHechizo(hechizo.id, Hechizo(hechizo.id, nombre, descripcion, puntos))
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoEliminar(hechizo: Hechizo) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Hechizo")
            .setMessage("¿Estás seguro de que quieres eliminar '${hechizo.nombre}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminarHechizo(hechizo.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
