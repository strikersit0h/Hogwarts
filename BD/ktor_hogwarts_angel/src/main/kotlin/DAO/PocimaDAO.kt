package com.example.DAO

import com.example.models.Pocima

interface PocimaDAO {
    fun obtenerTodas(): List<Pocima>
    fun crear(pocima: Pocima): Pocima?
    fun actualizar(id: Int, pocima: Pocima): Boolean
    fun eliminar(id: Int): Boolean // Ahora hará un soft-delete
    fun validar(id: Int, estado: Boolean): Boolean // Nueva función para el Profesor
}