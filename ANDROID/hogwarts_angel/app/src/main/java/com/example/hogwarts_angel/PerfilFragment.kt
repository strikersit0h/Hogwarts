package com.example.hogwarts_angel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.hogwarts_angel.databinding.FragmentPerfilBinding
import com.example.hogwarts_angel.viewmodels.PerfilViewModel

class PerfilFragment : Fragment() {

    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PerfilViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.personajeAleatorio.observe(viewLifecycleOwner, Observer { personaje ->
            binding.tvNombrePersonaje.text = personaje
        })

        viewModel.nombreUsuario.observe(viewLifecycleOwner, Observer { nombreReal ->
            binding.tvNombreUsuarioReal.text = nombreReal
        })

        binding.btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        viewModel.cargarDatosPerfil()
    }

    private fun cerrarSesion() {
        UserSession.usuarioLogueado = null

        val intent = Intent(activity, MainActivity::class.java)
        // Limpiar la pila de actividades para que el usuario no pueda volver atrás
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
