package com.example.hogwarts_angel

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hogwarts_angel.databinding.ActivityRegistroBinding
import com.example.hogwarts_angel.model.SombreroRespuestas
import com.example.hogwarts_angel.model.UsuarioRegistro
import com.example.hogwarts_angel.viewmodels.RegistroViewModel

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val viewModel: RegistroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.registroExitoso.observe(this) { exito ->
            if (exito == true) {
                Toast.makeText(this, "¡Registro Exitoso! Bienvenido a Hogwarts.", Toast.LENGTH_LONG).show()
                finish()
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, "Error de registro: $it", Toast.LENGTH_LONG).show()
            }
        }

        binding.btRegistrar.setOnClickListener {
            val nombre = binding.txtNombre.text
            val email = binding.txtEmail.text
            val contrasenya = binding.txtContraseA.text
            val respuestasMapa = viewModel.respuestasMapa.value

            if (nombre.isNullOrBlank() || email.isNullOrBlank() || contrasenya.isNullOrBlank()) {
                Toast.makeText(this, "Por favor, rellena todos los campos de información personal.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (respuestasMapa.isNullOrEmpty() || respuestasMapa.size < 4) {
                Toast.makeText(this, "Debes responder las 4 preguntas del Sombrero Seleccionador.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val respuestasSombrero = SombreroRespuestas(respuestas = respuestasMapa)

            val usuarioRegistro = UsuarioRegistro(
                nombre = nombre.toString(),
                email = email.toString(),
                contrasenya = contrasenya.toString(),
                respuestasSombrero = respuestasSombrero
            )

            viewModel.registrar(usuarioRegistro)
        }

    }
}