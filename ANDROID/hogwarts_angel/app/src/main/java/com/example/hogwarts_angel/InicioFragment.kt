package com.example.hogwarts_angel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.hogwarts_angel.databinding.FragmentInicioBinding
import com.example.hogwarts_angel.viewmodels.InicioViewModel

class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InicioViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observamos los datos del ViewModel
        viewModel.nombreUsuario.observe(viewLifecycleOwner, Observer { nombre ->
            binding.tvNombreUsuario.text = nombre
        })

        viewModel.casaUsuario.observe(viewLifecycleOwner, Observer { casa ->
            binding.tvCasaUsuario.text = casa
        })

        // Le decimos al ViewModel que cargue los datos
        viewModel.cargarDatosUsuario()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
