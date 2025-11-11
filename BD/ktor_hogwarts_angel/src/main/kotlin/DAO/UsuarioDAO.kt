package com.example.DAO

import com.example.models.Usuario
import com.example.models.UsuarioLogin
import com.example.models.UsuarioRol

interface  UsuarioDAO {
    fun insertar(usuario: Usuario): Boolean
    fun obtener(id: Int): Usuario?
    fun actualizar(usuario: Usuario): Boolean
    fun eliminar(id: Int): Boolean
    fun obtenerTodos(): List<Usuario>
    fun login(p: UsuarioLogin):Usuario?
    fun obtenerRolUsuario(id: Int): UsuarioRol?
}