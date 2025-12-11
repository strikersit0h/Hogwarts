package com.example.DAO

import com.example.models.Usuario
import com.example.models.UsuarioLogin
import com.example.models.UsuarioModificar
import com.example.models.UsuarioRol
import com.example.models.UsuarioDetallado
import helpers.Database
import java.sql.Statement

class UsuarioDAOImpl : UsuarioDAO  {

    override fun insertarUsuario(usuario: Usuario): Int? {
        val sql = "INSERT INTO usuarios (nombre, email, contraseña, experiencia, nivel, id_casa) VALUES (?, ?, ?, ?, ?, ?)"
        val connection = Database.getConnection()

        connection.use {
            val statement = it?.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) // Necesito recoger el ID del usuario para asignarle el rol de usuario al crearlo.
                ?: return null

            statement.setString(1, usuario.nombre)
            statement.setString(2, usuario.email)
            statement.setString(3, usuario.contrasenya)
            statement.setInt(4, usuario.experiencia)
            statement.setInt(5, usuario.nivel)
            statement.setInt(6, usuario.id_casa)

            try {
                val filasAfectadas = statement.executeUpdate()

                if (filasAfectadas > 0) {
                    val generatedKeys = statement.generatedKeys

                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
        return null
    }

    override fun obtenerTodosLosUsuariosDetallados(): List<UsuarioDetallado> {
        val usuarios = mutableListOf<UsuarioDetallado>()
        // Esta consulta usa GROUP_CONCAT para juntar todos los IDs de rol en una sola cadena
        val sql = """
            SELECT u.id, u.nombre, u.nivel, u.experiencia, u.id_casa, GROUP_CONCAT(ur.id_rol) as roles
            FROM usuarios u
            LEFT JOIN usuario_rol ur ON u.id = ur.id_usuario
            GROUP BY u.id
        """.trimIndent()

        val connection = Database.getConnection()
        connection?.use {
            val statement = it.prepareStatement(sql)
            val resultSet = statement.executeQuery()
            while (resultSet.next()) {
                val rolesString = resultSet.getString("roles")
                // Convertimos la cadena "1,2,3" en una lista de enteros
                val rolesList = rolesString?.split(',')?.mapNotNull { it.toIntOrNull() } ?: emptyList()

                usuarios.add(
                    UsuarioDetallado(
                        id = resultSet.getInt("id"),
                        nombre = resultSet.getString("nombre"),
                        nivel = resultSet.getInt("nivel"),
                        experiencia = resultSet.getInt("experiencia"),
                        id_casa = resultSet.getInt("id_casa"),
                        id_roles = rolesList
                    )
                )
            }
        }
        return usuarios
    }

    override fun actualizarRolesUsuario(idUsuario: Int, nuevosRoles: List<Int>): Boolean {
        val deleteSql = "DELETE FROM usuario_rol WHERE id_usuario = ?"
        val insertSql = "INSERT INTO usuario_rol (id_usuario, id_rol) VALUES (?, ?)"
        val connection = Database.getConnection()

        return try {
            // 1. Borrar roles antiguos
            val deleteStatement = connection?.prepareStatement(deleteSql)
            deleteStatement?.setInt(1, idUsuario)
            deleteStatement?.executeUpdate()

            // 2. Insertar roles nuevos
            val insertStatement = connection?.prepareStatement(insertSql)
            for (idRol in nuevosRoles) {
                insertStatement?.setInt(1, idUsuario)
                insertStatement?.setInt(2, idRol)
                insertStatement?.addBatch()
            }
            insertStatement?.executeBatch()

            connection?.commit()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {

            connection?.close()
        }
    }

    override fun insertarUsuarioRol(id: Int): Boolean {
        val sql = "INSERT INTO usuario_rol (id_usuario, id_rol) values (?, 3)"
        val connection = Database.getConnection()
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, id)
            try {
                return statement.executeUpdate() > 0
            }catch (e: Exception){
                e.printStackTrace()
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

    override fun actualizarUsuario(usuario: UsuarioModificar): Boolean {
        val sql = "UPDATE usuarios SET nombre = ?, nivel = ?, experiencia = ? WHERE id = ?"
        var filasAfectadas = 0
        val connection = Database.getConnection()

        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, usuario.nombre)
            statement.setInt(2, usuario.nivel)
            statement.setInt(3, usuario.experiencia)
            statement.setInt(4, usuario.id)

            filasAfectadas = statement.executeUpdate()
        }

        return filasAfectadas > 0
    }

    override fun eliminarUsuario(id: Int): Boolean {
        val sql = "DELETE FROM usuarios WHERE id = ?"
        var filasAfectadas = 0

        val connection = Database.getConnection()
        connection?.use {
            val statement = it.prepareStatement(sql)

            statement.setInt(1, id)

            filasAfectadas = statement.executeUpdate()
        }

        return filasAfectadas > 0
    }

    override fun obtenerTodos(): List<Usuario> {
        val listaUsuarios: MutableList<Usuario> = mutableListOf()
        val sql = "SELECT * FROM usuarios"
        val connection = Database.getConnection()

        connection.use {
            val statement = it!!.prepareStatement(sql)
            val resultSet = statement.executeQuery()

            while (resultSet.next()){
                listaUsuarios.add(
                    Usuario(
                        id = resultSet.getInt("id"),
                        nombre = resultSet.getString("nombre"),
                        email = resultSet.getString("email"),
                        contrasenya = resultSet.getString("contraseña"),
                        experiencia = resultSet.getInt("experiencia"),
                        nivel = resultSet.getInt("nivel"),
                        id_casa = resultSet.getInt("id_casa")
                    )
                )
            }
        }
        return listaUsuarios
    }

    override fun obtenerPoblacionesActuales(): Map<Int, Int> {
        val poblaciones: MutableMap<Int, Int> = mutableMapOf()

        val sql = "SELECT id_casa, COUNT(id) AS conteo FROM usuarios GROUP BY id_casa"

        val connection = Database.getConnection()

        connection.use {
            val statement = it!!.prepareStatement(sql)
            val resultSet = statement.executeQuery()

            while (resultSet.next()) {
                val idCasa = resultSet.getInt("id_casa")
                val conteo = resultSet.getInt("conteo")

                poblaciones[idCasa] = conteo
            }
        }

        return poblaciones
    }

    override fun login(p: UsuarioLogin): Usuario? {
        val sql = "SELECT * FROM usuarios WHERE email = ? AND contraseña = ?"
        val connection = Database.getConnection()
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, p.email)
            statement.setString(2, p.password)
            val resultSet = statement.executeQuery()

            if (resultSet.next()){
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

    override fun obtenerRolUsuario(id: Int): List<UsuarioRol> {
        val roles = mutableListOf<UsuarioRol>()
        val sql = "SELECT * FROM usuario_rol WHERE id_usuario = ?"
        val connection = Database.getConnection()
        connection?.use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, id)
            val resultSet = statement.executeQuery()

            while (resultSet.next()){
                val rol = UsuarioRol(
                    id_usuario = resultSet.getInt("id_usuario"),
                    id_rol = resultSet.getInt("id_rol")
                )
                roles.add(rol)
            }
        }
        return roles
    }
}