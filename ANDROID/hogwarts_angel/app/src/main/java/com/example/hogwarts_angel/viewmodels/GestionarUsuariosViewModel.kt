package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.api.HogwartsAPI
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.UsuarioDetallado
import com.example.hogwarts_angel.model.UsuarioModificar
import kotlinx.coroutines.launch

class GestionarUsuariosViewModel : ViewModel() {

    private val api: HogwartsAPI = HogwartsNetwork().retrofit

    private val _usuarios = MutableLiveData<List<UsuarioDetallado>>()
    val usuarios: LiveData<List<UsuarioDetallado>> = _usuarios

    private val _actualizacionExitosa = MutableLiveData<Boolean>()
    val actualizacionExitosa: LiveData<Boolean> = _actualizacionExitosa

    fun fetchUsuarios() {
        viewModelScope.launch {
            try {
                val response = api.getTodosLosUsuariosDetallados()
                if (response.isSuccessful) {
                    _usuarios.postValue(response.body())
                }
            } catch (e: Exception) {
                // Manejar el error
            }
        }
    }

    fun actualizarRoles(idUsuario: Int, nuevosRoles: List<Int>) {
        viewModelScope.launch {
            try {
                val response = api.actualizarRolesUsuario(idUsuario, nuevosRoles)
                _actualizacionExitosa.postValue(response.isSuccessful)
            } catch (e: Exception) {
                _actualizacionExitosa.postValue(false)
            }
        }
    }

    fun modificarUsuario(usuario: UsuarioModificar) {
        viewModelScope.launch {
            try {
                val response = api.modificarUsuario(usuario)
                _actualizacionExitosa.postValue(response.isSuccessful)
            } catch (e: Exception) {
                _actualizacionExitosa.postValue(false)
            }
        }
    }
}
