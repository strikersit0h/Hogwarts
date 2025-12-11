package com.example.hogwarts_angel

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.hogwarts_angel.databinding.ActivityModificarUsuarioBinding
import com.example.hogwarts_angel.model.UsuarioModificar
import com.example.hogwarts_angel.viewmodels.ModificarUsuarioViewModel

class ModificarUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModificarUsuarioBinding
    private val viewModel: ModificarUsuarioViewModel by viewModels()
    private var usuarioId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModificarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioId = UsuarioDataHolder.usuarioId

        if (usuarioId == null) {
            Toast.makeText(this, "Error: ID de usuario no encontrado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.usuario.observe(this, Observer { usuario ->
            usuario?.let {
                binding.etNombreUsuario.setText(it.nombre)
                binding.etNivel.setText(it.nivel.toString())
                binding.etExperiencia.setText(it.experiencia.toString())
            }
        })

        usuarioId?.let { viewModel.fetchUsuario(it) }

        binding.btnGuardar.setOnClickListener {
            val nombre = binding.etNombreUsuario.text.toString()
            val nivel = binding.etNivel.text.toString().toIntOrNull() ?: 0
            val experiencia = binding.etExperiencia.text.toString().toIntOrNull() ?: 0

            usuarioId?.let {
                val usuarioModificado = UsuarioModificar(it, nombre, nivel, experiencia)
                viewModel.modificarUsuario(usuarioModificado) { isSuccess, message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    if (isSuccess) {
                        finish()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UsuarioDataHolder.usuarioId = null
    }
}
