package com.example.DAO

import com.example.models.Asignatura
import com.example.models.Usuario

interface AsignaturaDAO {
    fun obtenerTodas(): List<Asignatura>
    fun crear(asignatura: Asignatura): Asignatura?
    fun actualizar(id: Int, asignatura: Asignatura): Boolean
    fun eliminar(id: Int): Boolean
    fun asignarUsuario(idAsignatura: Int, idUsuario: Int): Boolean
    fun obtenerUsuariosDeAsignatura(idAsignatura: Int): List<Usuario>
    fun desasignarUsuario(idAsignatura: Int, idUsuario: Int): Boolean
}