package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.UserSession
import com.example.hogwarts_angel.api.HogwartsAPI
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.Hechizo
import com.example.hogwarts_angel.model.Usuario
import kotlinx.coroutines.launch

class LogeadoSharedViewModel : ViewModel() {

    private val api: HogwartsAPI = HogwartsNetwork().retrofit

    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    private val _hechizosDisponibles = MutableLiveData<List<Hechizo>>()
    val hechizosDisponibles: LiveData<List<Hechizo>> = _hechizosDisponibles

    fun cargarDatosDeSesion() {
        if (_usuario.value != null) return // Evita recargar si ya tenemos los datos
        
        val usuarioActual = UserSession.usuarioLogueado
        _usuario.value = usuarioActual

        // Una vez tenemos el usuario, cargamos los datos
        fetchHechizosDisponibles()
    }

    /**
     * Obtiene la lista de TODOS los hechizos.
     */
    private fun fetchHechizosDisponibles() {
        viewModelScope.launch {
            try {
                val response = api.getHechizos()
                if (response.isSuccessful) {
                    _hechizosDisponibles.postValue(response.body())
                } else {
                    _hechizosDisponibles.postValue(emptyList()) // Lista vacía en caso de error
                }
            } catch (e: Exception) {
                _hechizosDisponibles.postValue(emptyList()) // Lista vacía en caso de excepción
            }
        }
    }
}
