package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.api.HogwartsAPI
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.Usuario
import com.example.hogwarts_angel.model.UsuarioModificar
import kotlinx.coroutines.launch

class ModificarUsuarioViewModel : ViewModel() {

    private val api: HogwartsAPI = HogwartsNetwork().retrofit

    private val _usuario = MutableLiveData<Usuario?>()
    val usuario: LiveData<Usuario?> = _usuario

    fun fetchUsuario(id: Int) {
        viewModelScope.launch {
            try {
                val response = api.getUsuario(id)
                if (response.isSuccessful) {
                    _usuario.postValue(response.body())
                } else {
                    _usuario.postValue(null)
                }
            } catch (e: Exception) {
                _usuario.postValue(null)
            }
        }
    }

    fun modificarUsuario(usuario: UsuarioModificar, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.modificarUsuario(usuario)
                if (response.isSuccessful) {
                    onResult(true, "Usuario actualizado con éxito.")
                } else {
                    onResult(false, "Error al actualizar: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                onResult(false, "Excepción al actualizar: ${e.message}")
            }
        }
    }
}
