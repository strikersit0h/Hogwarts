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
import com.example.hogwarts_angel.adapter.ValidarPocimaAdapter
import com.example.hogwarts_angel.databinding.FragmentValidarPocimasBinding
import com.example.hogwarts_angel.model.Pocima
import com.example.hogwarts_angel.viewmodels.ValidarPocimasViewModel

class ValidarPocimasFragment : Fragment() {

    private var _binding: FragmentValidarPocimasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ValidarPocimasViewModel by viewModels()
    private lateinit var adapter: ValidarPocimaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentValidarPocimasBinding.inflate(inflater, container, false)
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
                Toast.makeText(context, "Estado de validación actualizado", Toast.LENGTH_SHORT).show()
                viewModel.fetchPocimas()
            }
        })

        viewModel.fetchPocimas()
    }

    private fun setupRecyclerView() {
        adapter = ValidarPocimaAdapter(
            onApproveClick = { pocima ->
                viewModel.validarPocima(pocima.id!!, true)
            },
            onRejectClick = { pocima ->
                mostrarDialogoRechazo(pocima)
            }
        )
        binding.rvPocimasAValidar.adapter = adapter
        binding.rvPocimasAValidar.layoutManager = LinearLayoutManager(context)
    }

    private fun mostrarDialogoRechazo(pocima: Pocima) {
        AlertDialog.Builder(requireContext())
            .setTitle("Rechazar Pócima")
            .setMessage("¿Estás seguro de que quieres marcar '${pocima.nombre}' como no validada?")
            .setPositiveButton("Sí, rechazar") { _, _ ->
                viewModel.validarPocima(pocima.id!!, false)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
