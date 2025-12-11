package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.api.HogwartsAPI
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.Asignatura
import kotlinx.coroutines.launch

class GestionarAsignaturasViewModel : ViewModel() {

    private val api: HogwartsAPI = HogwartsNetwork().retrofit

    private val _asignaturas = MutableLiveData<List<Asignatura>>()
    val asignaturas: LiveData<List<Asignatura>> = _asignaturas

    private val _operacionExitosa = MutableLiveData<Boolean>()
    val operacionExitosa: LiveData<Boolean> = _operacionExitosa

    fun fetchAsignaturas() {
        viewModelScope.launch {
            try {
                val response = api.getAsignaturas()
                if (response.isSuccessful) {
                    _asignaturas.postValue(response.body())
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun crearAsignatura(asignatura: Asignatura) {
        viewModelScope.launch {
            try {
                val response = api.crearAsignatura(asignatura)
                _operacionExitosa.postValue(response.isSuccessful)
            } catch (e: Exception) {
                _operacionExitosa.postValue(false)
            }
        }
    }

    fun actualizarAsignatura(id: Int, asignatura: Asignatura) {
        viewModelScope.launch {
            try {
                val response = api.actualizarAsignatura(id, asignatura)
                _operacionExitosa.postValue(response.isSuccessful)
            } catch (e: Exception) {
                _operacionExitosa.postValue(false)
            }
        }
    }

    fun eliminarAsignatura(id: Int) {
        viewModelScope.launch {
            try {
                val response = api.eliminarAsignatura(id)
                _operacionExitosa.postValue(response.isSuccessful)
            } catch (e: Exception) {
                _operacionExitosa.postValue(false)
            }
        }
    }
}
