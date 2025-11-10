package com.example.DAO

import com.example.models.Usuario
import com.example.models.UsuarioLogin
import helpers.Database
import kotlin.io.use

class UsuarioDAOImpl : UsuarioDAO  {
    override fun insertar(usuario: Usuario): Boolean {
        val sql = "INSERT INTO usuarios (nombre, email, contraseña, experiencia, nivel, id_casa) VALUES (?, ?, ?, ?, ?, ?)"
        val connection = Database.getConnection()
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, usuario.nombre)
            statement.setString(2, usuario.email)
            statement.setString(3, usuario.contrasenya)
            statement.setInt(4, usuario.experiencia)
            statement.setInt(5, usuario.nivel)
            statement.setInt(6, usuario.id_casa)
            try {
                return statement.executeUpdate() > 0
            } catch (e: Exception) {
                return false
            }

        }
        return false
    }

    override fun obtener(id: Int): Usuario? {
        val sql = "SELECT * FROM usuarios WHERE id = ?"
        val connection = Database.getConnection()
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, id)
            val resultSet = statement.executeQuery()

            if (resultSet.next()) {
                return Usuario(
                    id = resultSet.getInt("id"),
                    nombre = resultSet.getString("nombre"),
                    email = resultSet.getString("email"),
                    contrasenya = resultSet.getString("contraseña"),
                    experiencia = resultSet.getInt("experiencia"),
                    nivel = resultSet.getInt("nivel"),
                    id_casa = resultSet.getInt("id_casa")

                )
            }
        }
        return null
    }

    override fun actualizar(usuario: Usuario): Boolean {
        TODO("Not yet implemented")
    }

    override fun eliminar(id: Int): Boolean {
        TODO("Not yet implemented")
    }

    override fun obtenerTodos(): List<Usuario> {
        TODO("Not yet implemented")
    }

    override fun login(p: UsuarioLogin): Usuario? {
        TODO("Not yet implemented")
    }

    override fun obtenerRolUsuario(id: Int): Int? {
        TODO("Not yet implemented")
    }

}