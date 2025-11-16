package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.Usuario
import kotlinx.coroutines.launch

class RegistroViewModel : ViewModel() {

    private val apiService = HogwartsNetwork().retrofit

    // LiveData para el resultado del Registro (true si éxito, false si fallo)
    val registroExitoso = MutableLiveData<Boolean?>()

    // LiveData para cualquier mensaje de error de la API o la red
    val error = MutableLiveData<String?>()

    fun registrar(nuevoUsuario: Usuario) {
        error.value = null // Limpiamos errores
        registroExitoso.value = null // Limpiamos el resultado anterior para garantizar notificación

        viewModelScope.launch {
            try {
                // Llama a la API directamente
                val respuesta = apiService.crearUsuario(nuevoUsuario)

                if (respuesta.isSuccessful && respuesta.code() == 201) {
                    registroExitoso.postValue(true)
                } else {
                    registroExitoso.postValue(false)
                    // Intentamos obtener un mensaje de error si es un 4xx (ej: email duplicado)
                    val errorBody = respuesta.errorBody()?.string()
                    val mensaje = if (errorBody?.contains("duplicate key") == true) {
                        "El email ya está registrado."
                    } else {
                        "Error ${respuesta.code()} en el servidor."
                    }
                    error.postValue(mensaje)
                }
            } catch (e: Exception) {
                // Error de red (sin conexión, timeout)
                error.postValue(e.message ?: "Error de conexión/red desconocido.")
                registroExitoso.postValue(false)
            }
        }
    }
}