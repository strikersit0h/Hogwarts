package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.UsuarioRegistro
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {

    private val apiService = HogwartsNetwork().retrofit
    // LiveData para el resultado del Registro (true si éxito, false si fallo)
    val registroExitoso = MutableLiveData<Boolean?>()
    // LiveData para cualquier mensaje de error de la API o la red
    val error = MutableLiveData<String?>()
    val respuestasMapa = MutableLiveData<Map<Int, String>>(emptyMap())

    fun guardarRespuesta(idPregunta: Int, opcion: String) {
        val currentMap = respuestasMapa.value.orEmpty().toMutableMap()
        currentMap[idPregunta] = opcion
        respuestasMapa.value = currentMap
    }

    fun registrar(nuevoUsuario: UsuarioRegistro) {
        error.value = null // Limpiamos errores
        registroExitoso.value = null // Limpiamos el resultado anterior para garantizar notificación

        viewModelScope.launch {
            try {
                val respuesta = apiService.crearUsuario(nuevoUsuario)

                if (respuesta.isSuccessful && respuesta.code() == 201) {
                    registroExitoso.postValue(true)
                } else {
                    registroExitoso.postValue(false)

                    val mensaje = "Error ${respuesta.code()} en el servidor."
                    error.postValue(mensaje)
                }
            } catch (e: Exception) {

                error.postValue(e.message ?: "Error de conexión/red desconocido.")
                registroExitoso.postValue(false)
            }
        }
    }
}
