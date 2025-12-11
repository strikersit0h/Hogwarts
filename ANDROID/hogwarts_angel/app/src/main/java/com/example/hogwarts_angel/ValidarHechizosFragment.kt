package com.example.hogwarts_angel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hogwarts_angel.adapter.ProfesorHechizoAdapter
import com.example.hogwarts_angel.databinding.FragmentValidarHechizosBinding
import com.example.hogwarts_angel.model.Hechizo
import com.example.hogwarts_angel.viewmodels.ProfesorHechizoViewModel

class ValidarHechizosFragment : Fragment() {

    private var _binding: FragmentValidarHechizosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfesorHechizoViewModel by viewModels()
    private lateinit var adapter: ProfesorHechizoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentValidarHechizosBinding.inflate(inflater, container, false)
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
                Toast.makeText(context, "Hechizo eliminado correctamente", Toast.LENGTH_SHORT).show()
                viewModel.fetchHechizos()
            }
        })

        viewModel.fetchHechizos()
    }

    private fun setupRecyclerView() {
        adapter = ProfesorHechizoAdapter {
            hechizo -> mostrarDialogoEliminar(hechizo)
        }
        binding.rvHechizosAValidar.adapter = adapter
        binding.rvHechizosAValidar.layoutManager = LinearLayoutManager(context)
    }

    private fun mostrarDialogoEliminar(hechizo: Hechizo) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Hechizo")
            .setMessage("¿Estás seguro de que quieres eliminar '${hechizo.nombre}'? Esta acción no se puede deshacer.")
            .setPositiveButton("Sí, eliminar") { _, _ ->
                viewModel.eliminarHechizo(hechizo.id!!)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
