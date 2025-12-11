package com.example.hogwarts_angel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hogwarts_angel.UserSession
import com.example.hogwarts_angel.api.HogwartsAPI
import com.example.hogwarts_angel.api.HogwartsNetwork
import com.example.hogwarts_angel.model.Ingrediente
import com.example.hogwarts_angel.model.Pocima
import kotlinx.coroutines.launch

class PocionesViewModel : ViewModel() {

    private val api: HogwartsAPI = HogwartsNetwork().retrofit

    // Lista de todos los ingredientes disponibles
    private val _ingredientes = MutableLiveData<List<Ingrediente>>()
    val ingredientes: LiveData<List<Ingrediente>> = _ingredientes

    // IDs de los ingredientes que el usuario ha seleccionado
    private val _ingredientesSeleccionados = MutableLiveData<Set<Int>>(emptySet())
    val ingredientesSeleccionados: LiveData<Set<Int>> = _ingredientesSeleccionados

    // LiveData para notificar el resultado de la creación
    private val _creacionExitosa = MutableLiveData<Boolean>()
    val creacionExitosa: LiveData<Boolean> = _creacionExitosa

    fun fetchIngredientes() {
        viewModelScope.launch {
            try {
                val response = api.getIngredientes()
                if (response.isSuccessful) {
                    _ingredientes.postValue(response.body())
                }
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun onIngredienteClicked(idIngrediente: Int) {
        val setActual = _ingredientesSeleccionados.value.orEmpty().toMutableSet()
        if (setActual.contains(idIngrediente)) {
            setActual.remove(idIngrediente)
        } else {
            setActual.add(idIngrediente)
        }
        _ingredientesSeleccionados.value = setActual
    }

    fun crearPocima(nombre: String) {
        val idCreador = UserSession.usuarioLogueado?.id
        if (idCreador == null) {
            _creacionExitosa.postValue(false)
            return
        }

        val nuevaPocima = Pocima(nombre = nombre, id_creador = idCreador)

        viewModelScope.launch {
            try {
                val response = api.crearPocima(nuevaPocima)
                _creacionExitosa.postValue(response.isSuccessful)
                if (response.isSuccessful) {
                    _ingredientesSeleccionados.postValue(emptySet()) // Limpiar selección
                }
            } catch (e: Exception) {
                _creacionExitosa.postValue(false)
            }
        }
    }
}
