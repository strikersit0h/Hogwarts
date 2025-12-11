package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.api.HogwartsAPI
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.Hechizo
import kotlinx.coroutines.launch

class HechizosViewModel : ViewModel() {

    private val api: HogwartsAPI = HogwartsNetwork().retrofit

    private val _hechizosDisponibles = MutableLiveData<List<Hechizo>>()
    val hechizosDisponibles: LiveData<List<Hechizo>> = _hechizosDisponibles

    fun fetchHechizosDisponibles() {
        viewModelScope.launch {
            try {

                val response = api.getHechizos()
                if (response.isSuccessful) {
                    _hechizosDisponibles.postValue(response.body())
                }
            } catch (e: Exception) {
                _hechizosDisponibles.postValue(emptyList())
            }
        }
    }
}
