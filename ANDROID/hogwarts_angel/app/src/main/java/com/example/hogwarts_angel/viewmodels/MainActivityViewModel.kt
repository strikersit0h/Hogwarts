package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.Usuario
import com.example.hogwarts_angel.model.UsuarioLogin
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    // Inicializamos la instancia de Retrofit
    private val apiService = HogwartsNetwork().retrofit

    // LiveData para el resultado del Login (Contiene el Usuario si funciona, null si falla)
    val usuarioAutenticado = MutableLiveData<Usuario?>()

    // LiveData para cualquier mensaje de error de la API o la red
    val error = MutableLiveData<String?>()

    /**
     * Intenta iniciar sesión con email y contraseña.
     */
    fun iniciarSesion(email: String, clave: String) {
        error.value = null // Limpiamos errores

        viewModelScope.launch {
            try {
                val datosLogin = UsuarioLogin(email = email, password = clave)
                // 💡 Llama a la API directamente
                val respuesta = apiService.login(datosLogin)

                if (respuesta.isSuccessful) {
                    // Login exitoso. postValue actualizará el LiveData
                    usuarioAutenticado.postValue(respuesta.body())
                } else {
                    // Fallo. Enviamos null al LiveData y gestionamos el error específico
                    usuarioAutenticado.postValue(null)
                    if (respuesta.code() == 401 || respuesta.code() == 400) {
                        error.postValue("Email o Contraseña incorrectos.")
                    } else {
                        error.postValue("Error ${respuesta.code()} en el servidor.")
                    }
                }
            } catch (e: Exception) {
                // Error de red (sin conexión, timeout)
                error.postValue(e.message ?: "Error de conexión/red desconocido.")
                usuarioAutenticado.postValue(null)
            }
        }
    }
}