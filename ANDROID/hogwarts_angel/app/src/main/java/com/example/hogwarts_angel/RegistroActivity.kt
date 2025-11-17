package com.example.hogwarts_angel

import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hogwarts_angel.databinding.ActivityRegistroBinding
import com.example.hogwarts_angel.model.Casa
import com.example.hogwarts_angel.model.Usuario
import com.example.hogwarts_angel.viewmodels.RegistroViewModel

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val viewModel: RegistroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSliders()
        setupListeners()
        observeViewModel()
    }

    private fun setupSliders() {
        // La SeekBar tiene max="4" para ir de 0 a 4, que sumado a 1 es 1 a 5.
        // Hacemos que la etiqueta de texto muestre el valor real (1 a 5).

        val sliders = listOf(
            binding.sliderValentia to binding.tvValorValentia,
            binding.sliderInteligencia to binding.tvValorInteligencia,
            binding.sliderAmbicion to binding.tvValorAmbicion,
            binding.sliderLealtad to binding.tvValorLealtad
        )

        sliders.forEach { (seekBar, textView) ->
            // Inicializa el texto con el valor actual (por defecto 3, ya que progress es 2 + 1)
            textView.text = (seekBar.progress + 1).toString()

            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    // Muestra el valor real (1 al 5)
                    textView.text = (progress + 1).toString()
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun setupListeners() {
        binding.btSeleccionarCasa.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val contrasenya = binding.etContrasenya.text.toString()

            if (nombre.isEmpty() || email.isEmpty() || contrasenya.isEmpty()) {
                Toast.makeText(this, "Debes completar todos los campos de usuario.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 1. Obtener los valores de los sliders (sumamos 1 para ir de 1 a 5)
            val valorValentia = binding.sliderValentia.progress + 1
            val valorInteligencia = binding.sliderInteligencia.progress + 1
            val valorAmbicion = binding.sliderAmbicion.progress + 1
            val valorLealtad = binding.sliderLealtad.progress + 1

            // 2. Ejecutar el Algoritmo de Selección
            val idCasaAsignada = asignarCasa(valorValentia, valorInteligencia, valorAmbicion, valorLealtad)

            // 3. Crear el objeto Usuario para registrar
            val nuevoUsuario = Usuario(
                nombre = nombre,
                email = email,
                contrasenya = contrasenya,
                experiencia = 0, // Valores fijos al registrar
                nivel = 1,       // Valores fijos al registrar
                id_casa = idCasaAsignada
            )

            // 4. Iniciar el registro a través del ViewModel
            viewModel.registrar(nuevoUsuario)
        }
    }

    private fun asignarCasa(valentia: Int, inteligencia: Int, ambicion: Int, lealtad: Int): Int {

        // 1. Inicializar con la primera casa (Gryffindor)
        var casaGanadora = Casa.GRYFFINDOR
        var maximoPuntaje = valentia

        // 2. Comparar con Ravenclaw
        if (inteligencia > maximoPuntaje) {
            maximoPuntaje = inteligencia
            casaGanadora = Casa.RAVENCLAW
        }

        // 3. Comparar con Slytherin
        // Nota: Si la puntuacion es igual (ambicion == maximoPuntaje),
        // mantenemos la casa anterior para resolver empates a favor de las casas anteriores.
        if (ambicion > maximoPuntaje) {
            maximoPuntaje = ambicion
            casaGanadora = Casa.SLYTHERIN
        }

        // 4. Comparar con Hufflepuff
        if (lealtad > maximoPuntaje) {
            // maximoPuntaje = lealtad
            casaGanadora = Casa.HUFFLEPUFF
        }

        // La Casa Ganadora ahora contiene el ID de la casa con la puntuacion más alta.
        return casaGanadora
    }
    private fun observeViewModel() {
        viewModel.registroExitoso.observe(this) { exito ->
            if (exito == true) {
                // En el siguiente Sprint se modificará y se hará que lance la nueva activity que contendrá todo lo demás.
                Toast.makeText(this, "¡Registro Exitoso! Bienvenido a Hogwarts.", Toast.LENGTH_LONG).show()
                finish() // Cierra la activity y vuelve a MainActivity (Login)
            }
        }

        viewModel.error.observe(this) { errorMsg ->
            errorMsg?.let {
                Toast.makeText(this, "Error de registro: $it", Toast.LENGTH_LONG).show()
            }
        }
    }
}