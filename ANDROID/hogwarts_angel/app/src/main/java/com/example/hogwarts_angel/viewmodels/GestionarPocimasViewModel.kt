package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.api.HogwartsAPI
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.Pocima
import kotlinx.coroutines.launch

class GestionarPocimasViewModel : ViewModel() {

    private val api: HogwartsAPI = HogwartsNetwork().retrofit

    private val _pocimas = MutableLiveData<List<Pocima>>()
    val pocimas: LiveData<List<Pocima>> = _pocimas

    private val _operacionExitosa = MutableLiveData<Boolean>()
    val operacionExitosa: LiveData<Boolean> = _operacionExitosa

    fun fetchPocimas() {
        viewModelScope.launch {
            try {
                val response = api.getPocimas()
                if (response.isSuccessful) {
                    _pocimas.postValue(response.body())
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun crearPocima(pocima: Pocima) {
        viewModelScope.launch {
            try {
                val response = api.crearPocima(pocima)
                _operacionExitosa.postValue(response.isSuccessful)
            } catch (e: Exception) {
                _operacionExitosa.postValue(false)
            }
        }
    }

    fun actualizarPocima(id: Int, pocima: Pocima) {
        viewModelScope.launch {
            try {
                val response = api.actualizarPocima(id, pocima)
                _operacionExitosa.postValue(response.isSuccessful)
            } catch (e: Exception) {
                _operacionExitosa.postValue(false)
            }
        }
    }

    fun eliminarPocima(id: Int) {
        viewModelScope.launch {
            try {
                val response = api.eliminarPocima(id)
                _operacionExitosa.postValue(response.isSuccessful)
            } catch (e: Exception) {
                _operacionExitosa.postValue(false)
            }
        }
    }
}
