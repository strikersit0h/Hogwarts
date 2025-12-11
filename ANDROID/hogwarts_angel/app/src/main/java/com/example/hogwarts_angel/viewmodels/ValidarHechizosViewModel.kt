package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.api.HogwartsAPI
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.Hechizo
import kotlinx.coroutines.launch

class ProfesorHechizoViewModel : ViewModel() {

    private val api: HogwartsAPI = HogwartsNetwork().retrofit

    private val _hechizos = MutableLiveData<List<Hechizo>>()
    val hechizos: LiveData<List<Hechizo>> = _hechizos

    private val _operacionExitosa = MutableLiveData<Boolean>()
    val operacionExitosa: LiveData<Boolean> = _operacionExitosa

    fun fetchHechizos() {
        viewModelScope.launch {
            try {
                val response = api.getHechizos()
                if (response.isSuccessful) {
                    _hechizos.postValue(response.body())
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun eliminarHechizo(id: Int) {
        viewModelScope.launch {
            try {
                val response = api.eliminarHechizo(id)
                _operacionExitosa.postValue(response.isSuccessful)
            } catch (e: Exception) {
                _operacionExitosa.postValue(false)
            }
        }
    }
}
