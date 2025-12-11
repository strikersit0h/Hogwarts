package com.example.DAO

import com.example.models.Ingrediente

interface IngredienteDAO {
    fun obtenerTodos(): List<Ingrediente>
}