package com.example.hogwarts_angel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hogwarts_angel.adapter.IngredienteAdapter
import com.example.hogwarts_angel.databinding.FragmentPocionesBinding
import com.example.hogwarts_angel.viewmodels.PocionesViewModel

class PocionesFragment : Fragment() {

    private var _binding: FragmentPocionesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PocionesViewModel by viewModels()
    private lateinit var adapter: IngredienteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPocionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = IngredienteAdapter { ingrediente ->
            viewModel.onIngredienteClicked(ingrediente.id)
        }
        binding.rvIngredientes.adapter = adapter
        binding.rvIngredientes.layoutManager = LinearLayoutManager(context)

        // Observador para la lista de ingredientes
        viewModel.ingredientes.observe(viewLifecycleOwner, Observer { ingredientes ->
            adapter.submitList(ingredientes)
        })

        // Observador para los ingredientes seleccionados
        viewModel.ingredientesSeleccionados.observe(viewLifecycleOwner, Observer { seleccionados ->
            adapter.setSeleccionados(seleccionados)
        })

        // Observador para el resultado de la creación de la pócima
        viewModel.creacionExitosa.observe(viewLifecycleOwner, Observer { exito ->
            if (exito) {
                Toast.makeText(context, "Pócima creada con éxito", Toast.LENGTH_SHORT).show()
                binding.etNombrePocima.text.clear()
            } else {
                Toast.makeText(context, "Error al crear la pócima", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnCrearPocima.setOnClickListener {
            val nombrePocima = binding.etNombrePocima.text.toString()
            if (nombrePocima.isNotBlank()) {
                viewModel.crearPocima(nombrePocima)
            } else {
                Toast.makeText(context, "Dale un nombre a tu pócima", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.fetchIngredientes()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
