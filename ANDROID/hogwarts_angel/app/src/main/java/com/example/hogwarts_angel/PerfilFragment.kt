package com.example.hogwarts_angel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.hogwarts_angel.databinding.FragmentPerfilBinding
import com.example.hogwarts_angel.viewmodels.PerfilViewModel

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PerfilViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        viewModel.personajeAleatorio.observe(viewLifecycleOwner) { personaje ->
            binding.tvNombrePersonaje.text = personaje
        }

        viewModel.nombreUsuario.observe(viewLifecycleOwner) { nombreReal ->
            binding.tvNombreUsuarioReal.text = nombreReal
        }

        viewModel.cargarDatosPerfil()
    }

    private fun cerrarSesion() {
        UserSession.usuarioLogueado = null

        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
