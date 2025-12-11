package com.example.DAO

import com.example.models.Hechizo

interface HechizoDAO {
    fun obtenerTodos(): List<Hechizo>
    fun crear(hechizo: Hechizo): Hechizo?
    fun actualizar(id: Int, hechizo: Hechizo): Boolean
    fun eliminar(id: Int): Boolean
    fun obtenerHechizosPorNivel(nivelRequerido: Int): List<Hechizo>
}