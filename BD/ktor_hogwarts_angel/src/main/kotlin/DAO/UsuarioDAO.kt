package com.example.DAO

import com.example.models.UsuarioDetallado
import com.example.models.Usuario
import com.example.models.UsuarioLogin
import com.example.models.UsuarioModificar
import com.example.models.UsuarioRol

interface UsuarioDAO {
    fun insertarUsuario(usuario: Usuario): Int?
    fun obtener(id: Int): Usuario?
    fun actualizarUsuario(usuario: UsuarioModificar): Boolean
    fun eliminarUsuario(id: Int): Boolean
    fun obtenerTodos(): List<Usuario>
    fun login(p: UsuarioLogin): Usuario?
    fun obtenerRolUsuario(id: Int): List<UsuarioRol>
    fun insertarUsuarioRol(id: Int): Boolean
    fun obtenerPoblacionesActuales(): Map<Int, Int>
    fun obtenerTodosLosUsuariosDetallados(): List<UsuarioDetallado>
    fun actualizarRolesUsuario(idUsuario: Int, nuevosRoles: List<Int>): Boolean
}
