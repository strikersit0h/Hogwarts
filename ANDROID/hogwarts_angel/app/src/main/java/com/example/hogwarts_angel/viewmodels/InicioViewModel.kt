package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hogwarts_angel.UserSession

class InicioViewModel : ViewModel() {

    private val _nombreUsuario = MutableLiveData<String>()
    val nombreUsuario: LiveData<String> = _nombreUsuario

    private val _casaUsuario = MutableLiveData<String>()
    val casaUsuario: LiveData<String> = _casaUsuario

    // Mapa para traducir el ID de la casa a su nombre
    private val mapaCasas = mapOf(
        1 to "Gryffindor",
        2 to "Slytherin",
        3 to "Hufflepuff",
        4 to "Ravenclaw"
    )

    fun cargarDatosUsuario() {
        val usuario = UserSession.usuarioLogueado
        if (usuario != null) {
            _nombreUsuario.value = usuario.nombre
            _casaUsuario.value = mapaCasas[usuario.id_casa] ?: "Casa Desconocida"
        } else {
            _nombreUsuario.value = "Mago Desconocido"
            _casaUsuario.value = "Sin casa"
        }
    }
}
