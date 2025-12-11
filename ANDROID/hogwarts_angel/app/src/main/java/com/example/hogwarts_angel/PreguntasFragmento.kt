package com.example.hogwarts_angel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.hogwarts_angel.databinding.FragmentPreguntasFragmentoBinding
import com.example.hogwarts_angel.viewmodels.RegistroViewModel

class PreguntasFragmento : Fragment() {

    private var _binding: FragmentPreguntasFragmentoBinding? = null
    private val binding get() = _binding!!

    private val registroViewModel: RegistroViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreguntasFragmentoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gruposDeRespuestas = listOf(
            binding.rgPregunta1 to 1,
            binding.rgPregunta2 to 2,
            binding.rgPregunta3 to 3,
            binding.rgPregunta4 to 4
        )

        gruposDeRespuestas.forEach { (radioGroup, idPregunta) ->
            radioGroup.setOnCheckedChangeListener { group, checkedId ->

                val respuestaLetra = when (checkedId) {
                    binding.rbP1A.id, binding.rbP2A.id, binding.rbP3A.id, binding.rbP4A.id -> "A"
                    binding.rbP1B.id, binding.rbP2B.id, binding.rbP3B.id, binding.rbP4B.id -> "B"
                    binding.rbP1C.id, binding.rbP2C.id, binding.rbP3C.id, binding.rbP4C.id -> "C"
                    binding.rbP1D.id, binding.rbP2D.id, binding.rbP3D.id, binding.rbP4D.id -> "D"
                    else -> ""
                }

                if (respuestaLetra.isNotEmpty()) {
                    registroViewModel.guardarRespuesta(idPregunta, respuestaLetra)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}