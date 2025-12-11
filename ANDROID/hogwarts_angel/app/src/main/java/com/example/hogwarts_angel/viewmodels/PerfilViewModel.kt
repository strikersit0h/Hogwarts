package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hogwarts_angel.UserSession

class PerfilViewModel : ViewModel() {

    private val _nombreUsuario = MutableLiveData<String>()
    val nombreUsuario: LiveData<String> = _nombreUsuario

    private val _personajeAleatorio = MutableLiveData<String>()
    val personajeAleatorio: LiveData<String> = _personajeAleatorio

    private val listaPersonajes = listOf("Harry Potter", "Hermione Granger", "Ron Weasley", "Draco Malfoy")

    fun cargarDatosPerfil() {
        // Cargar el nombre del usuario real desde la sesión
        _nombreUsuario.value = "(${UserSession.usuarioLogueado?.nombre ?: "Usuario Desconocido"})"

        // Seleccionar un personaje aleatorio
        _personajeAleatorio.value = listaPersonajes.random()
    }
}
