package com.example.hogwarts_angel

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hogwarts_angel.databinding.ActivityAgregarUsuarioBinding
import com.example.hogwarts_angel.model.Usuario
import com.example.hogwarts_angel.viewmodels.AgregarUsuarioViewModel

class AgregarUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAgregarUsuarioBinding
    private val viewModel: AgregarUsuarioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgregarUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAgregarUsuario.setOnClickListener {
            val nombre = binding.etNombre.text.toString()
            val email = binding.etEmail.text.toString()
            val contrasenya = binding.etContrasenya.text.toString()
            val experiencia = binding.etExperiencia.text.toString().toIntOrNull() ?: 0
            val nivel = binding.etNivel.text.toString().toIntOrNull() ?: 0
            val idCasa = binding.etIdCasa.text.toString().toIntOrNull() ?: 0

            if (nombre.isNotBlank() && email.isNotBlank() && contrasenya.isNotBlank()) {
                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    email = email,
                    contrasenya = contrasenya,
                    experiencia = experiencia,
                    nivel = nivel,
                    id_casa = idCasa
                )

                viewModel.agregarUsuario(nuevoUsuario) { isSuccess, message ->
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    if (isSuccess) {
                        finish() // Cierra la actividad si el usuario se creó con éxito
                    }
                }
            } else {
                Toast.makeText(this, "Nombre, email y contraseña son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
