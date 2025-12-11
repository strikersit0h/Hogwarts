package com.example.hogwarts_angel

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hogwarts_angel.adapter.GestionUsuarioAdapter
import com.example.hogwarts_angel.databinding.ActivityDumbledoreBinding
import com.example.hogwarts_angel.viewmodels.DumbledoreActivityViewModel

class DumbledoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDumbledoreBinding
    private val viewModel: DumbledoreActivityViewModel by viewModels()
    private lateinit var usuarioAdapter: GestionUsuarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDumbledoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        usuarioAdapter = GestionUsuarioAdapter(viewModel)

        binding.rvListaUsuarios.apply {
            layoutManager = LinearLayoutManager(this@DumbledoreActivity)
            adapter = usuarioAdapter
        }

        binding.btnAgregarUsuario.setOnClickListener {
            viewModel.agregarNuevoUsuario()
        }

        viewModel.usuariosGestion.observe(this) { listaUsuarios ->
            usuarioAdapter.submitList(listaUsuarios)
        }

        viewModel.error.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this, "ERROR: $errorMessage", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.usuarioParaEditar.observe(this) { usuario ->
            usuario?.let {
                Toast.makeText(this, "Navegar a Editar: ${it.nombre}", Toast.LENGTH_SHORT).show()
                viewModel.onUsuarioParaEditarConsumido()
            }
        }

        viewModel.navegarAInsertar.observe(this) { navegar ->
            if (navegar) {
                Toast.makeText(this, "Navegar a Registro de Nuevo Usuario", Toast.LENGTH_SHORT).show()
                viewModel.onNavegarAInsertarConsumido()
            }
        }

        viewModel.cargarUsuariosParaGestion()
    }
}