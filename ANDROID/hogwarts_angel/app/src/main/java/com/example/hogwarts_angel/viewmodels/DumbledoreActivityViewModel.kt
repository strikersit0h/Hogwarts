package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.Usuario
import com.example.hogwarts_angel.model.UsuarioDetallado
import kotlinx.coroutines.launch

class DumbledoreActivityViewModel: ViewModel()  {
    private val apiService = HogwartsNetwork().retrofit

    private val _usuariosGestion = MutableLiveData<List<UsuarioDetallado>>()
    val usuariosGestion: LiveData<List<UsuarioDetallado>> = _usuariosGestion

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _usuarioParaEditar = MutableLiveData<UsuarioDetallado?>()
    val usuarioParaEditar: LiveData<UsuarioDetallado?> = _usuarioParaEditar

    private val _navegarAInsertar = MutableLiveData<Boolean>()
    val navegarAInsertar: LiveData<Boolean> = _navegarAInsertar

    fun cargarUsuariosParaGestion() {
        viewModelScope.launch {
            try {
                val respuestaBase = apiService.getTodosLosUsuarios()

                if (respuestaBase.isSuccessful) {
                    val listaUsuariosBase = respuestaBase.body() ?: emptyList()

                    val usuariosDetallados = listaUsuariosBase.mapNotNull { usuarioBase ->
                        combinarDetallesUsuario(usuarioBase)
                    }

                    _usuariosGestion.postValue(usuariosDetallados)
                } else {
                    _error.postValue("Error al obtener la lista base de usuarios: ${respuestaBase.code()}")
                }
            } catch (e: Exception) {
                _error.postValue("Error de red al cargar la gestión: ${e.message}")
            }
        }
    }

    private suspend fun combinarDetallesUsuario(usuarioBase: Usuario): UsuarioDetallado? {
        return try {

            val respuestaRol = apiService.getRolUsuario(usuarioBase.id)
            val listaRoles = respuestaRol.body()?.map { it.id_rol } ?: emptyList()

            val idCasa = usuarioBase.id_casa

            UsuarioDetallado(
                id = usuarioBase.id,
                nombre = usuarioBase.nombre,
                nivel = usuarioBase.nivel,
                experiencia = usuarioBase.experiencia,
                id_roles = listaRoles,
                id_casa = idCasa
            )
        } catch (e: Exception) {
            _error.postValue("Fallo al obtener detalles para el usuario ID ${usuarioBase.id}: ${e.message}")
            null
        }
    }

    fun deleteUsuario(idUsuario: Int?) {
        viewModelScope.launch {
            try {
                val respuesta = apiService.deleteUsuario(idUsuario)

                if (respuesta.isSuccessful) {
                    cargarUsuariosParaGestion() // Recargar la lista
                } else {
                    _error.postValue("Error al eliminar usuario: ${respuesta.code()}")
                }
            } catch (e: Exception) {
                _error.postValue("Error de red al eliminar usuario: ${e.message}")
            }
        }
    }

    fun seleccionarUsuarioParaModificar(usuario: UsuarioDetallado) {
        _usuarioParaEditar.value = usuario
    }

    fun agregarNuevoUsuario() {
        _navegarAInsertar.value = true
    }

    // Funciones para limpiar los eventos después de que la Activity actúe
    fun onUsuarioParaEditarConsumido() {
        _usuarioParaEditar.value = null
    }

    fun onNavegarAInsertarConsumido() {
        _navegarAInsertar.value = false
    }
}