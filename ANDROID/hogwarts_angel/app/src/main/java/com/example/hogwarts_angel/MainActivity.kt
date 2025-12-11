package com.example.hogwarts_angel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hogwarts_angel.databinding.ActivityMainBinding
import com.example.hogwarts_angel.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // --- Lógica de los Botones ---

        binding.btLogin.setOnClickListener {
            val email = binding.editTextText.text.toString().trim()
            val clave = binding.editTextTextPassword.text.toString()

            if (email.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Debe introducir el email y la contraseña.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.iniciarSesion(email, clave)
        }

        binding.btRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        // --- Observación del Login ---

        viewModel.usuarioAutenticado.observe(this) { usuario ->
            if (usuario != null) {
                // Guardamos el usuario en el objeto de sesión
                UserSession.usuarioLogueado = usuario
                Toast.makeText(this, "Login exitoso. Obteniendo roles...", Toast.LENGTH_SHORT).show()
                viewModel.obtenerRoles(usuario.id!!)
            }
        }

        viewModel.rolesUsuario.observe(this) { roles ->
            roles?.let {
                viewModel.rolesUsuario.value = null

                val usuario = UserSession.usuarioLogueado
                if (usuario == null) {
                    Toast.makeText(this, "Error: No se pudo recuperar la sesión del usuario.", Toast.LENGTH_SHORT).show()
                    return@observe
                }

                val mapaRoles = mapOf(
                    1 to "Dumbledore", 2 to "Administrador", 3 to "Usuario", 4 to "Profesor"
                )

                if (it.size > 1) {
                    val rolesConNombre = it.map { id -> mapaRoles[id] ?: "Rol Desconocido ($id)" }
                    val rolesArray = rolesConNombre.toTypedArray<CharSequence>()

                    AlertDialog.Builder(this)
                        .setTitle("Selecciona tu rol de acceso")
                        .setItems(rolesArray) { _, which ->
                            val idRolSeleccionado = it[which]
                            val intent = when (idRolSeleccionado) {
                                1 -> Intent(this, DumbledoreActivity::class.java)
                                2 -> Intent(this, AdministradorActivity::class.java)
                                4 -> Intent(this, ProfesorActivity::class.java)
                                3 -> Intent(this, LogeadoActivity::class.java)
                                else -> Intent(this, LogeadoActivity::class.java)
                            }
                            startActivity(intent)
                        }
                        .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
                        .setCancelable(false)
                        .show()

                } else {
                    val idRol = it.firstOrNull() ?: 3
                    val intent = when (idRol) {
                        1 -> Intent(this, DumbledoreActivity::class.java)
                        2 -> Intent(this, AdministradorActivity::class.java)
                        4 -> Intent(this, ProfesorActivity::class.java)
                        3 -> Intent(this, LogeadoActivity::class.java)
                        else -> Intent(this, LogeadoActivity::class.java)
                    }
                    startActivity(intent)
                }
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
                viewModel.error.value = null // Limpiar el LiveData de error
            }
        }
    }
}
