package com.example.hogwarts_angel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hogwarts_angel.adapter.HechizoDisponibleAdapter
import com.example.hogwarts_angel.databinding.FragmentHechizosBinding
import com.example.hogwarts_angel.viewmodels.LogeadoSharedViewModel

class HechizosFragment : Fragment() {

    private var _binding: FragmentHechizosBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: LogeadoSharedViewModel by activityViewModels()
    private lateinit var adapter: HechizoDisponibleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHechizosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HechizoDisponibleAdapter()
        binding.rvHechizosDisponibles.adapter = adapter
        binding.rvHechizosDisponibles.layoutManager = LinearLayoutManager(context)

        sharedViewModel.hechizosDisponibles.observe(viewLifecycleOwner, Observer { hechizos ->
            adapter.submitList(hechizos)
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
