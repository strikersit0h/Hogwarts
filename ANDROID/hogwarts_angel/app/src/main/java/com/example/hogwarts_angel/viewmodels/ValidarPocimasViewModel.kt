package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.api.HogwartsAPI
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.Pocima
import kotlinx.coroutines.launch

class ValidarPocimasViewModel : ViewModel() {

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

    fun validarPocima(id: Int, estado: Boolean) {
        viewModelScope.launch {
            try {
                val body = mapOf("validada" to estado)
                val response = api.validarPocima(id, body)
                _operacionExitosa.postValue(response.isSuccessful)
            } catch (e: Exception) {
                _operacionExitosa.postValue(false)
            }
        }
    }
}
