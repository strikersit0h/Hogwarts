package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.Usuario
import kotlinx.coroutines.launch

class AgregarUsuarioViewModel : ViewModel() {

    private val api = HogwartsNetwork().retrofit

    fun agregarUsuario(usuario: Usuario, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.crearUsuarioDirecto(usuario)
                if (response.isSuccessful) {
                    onResult(true, "Usuario añadido con éxito.")
                } else {
                    onResult(false, "Error al añadir: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                onResult(false, "Excepción al añadir: ${e.message}")
            }
        }
    }
}
