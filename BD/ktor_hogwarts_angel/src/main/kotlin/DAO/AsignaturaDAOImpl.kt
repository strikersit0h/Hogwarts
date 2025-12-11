package com.example.DAO

import com.example.models.Asignatura
import com.example.models.Usuario
import helpers.Database
import java.sql.Statement

class AsignaturaDAOImpl : AsignaturaDAO {

    override fun obtenerTodas(): List<Asignatura> {
        val asignaturas = mutableListOf<Asignatura>()
        val sql = "SELECT * FROM asignaturas"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    asignaturas.add(Asignatura(
                        id = rs.getInt("id"),
                        nombre = rs.getString("nombre"),
                        descripcion = rs.getString("descripcion")
                    ))
                }
            }
        }
        return asignaturas
    }

    override fun crear(asignatura: Asignatura): Asignatura? {
        val sql = "INSERT INTO asignaturas (nombre, descripcion) VALUES (?, ?)"
        var generatedId: Int? = null
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { stmt ->
                stmt.setString(1, asignatura.nombre)
                stmt.setString(2, asignatura.descripcion)
                if (stmt.executeUpdate() > 0) {
                    val keys = stmt.generatedKeys
                    if (keys.next()) {
                        generatedId = keys.getInt(1)
                    }
                }
            }
        }
        return generatedId?.let { asignatura.copy(id = it) }
    }

    override fun actualizar(id: Int, asignatura: Asignatura): Boolean {
        val sql = "UPDATE asignaturas SET nombre = ?, descripcion = ? WHERE id = ?"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, asignatura.nombre)
                stmt.setString(2, asignatura.descripcion)
                stmt.setInt(3, id)
                return stmt.executeUpdate() > 0
            }
        }
        return false
    }

    override fun eliminar(id: Int): Boolean {
        val sql = "DELETE FROM asignaturas WHERE id = ?"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                return stmt.executeUpdate() > 0
            }
        }
        return false
    }

    override fun asignarUsuario(idAsignatura: Int, idUsuario: Int): Boolean {
        val sql = "INSERT INTO usuario_asignatura (id_asignatura, id_usuario) VALUES (?, ?)"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, idAsignatura)
                stmt.setInt(2, idUsuario)
                // Usamos try-catch para evitar errores si el usuario ya está asignado (clave primaria duplicada)
                return try { stmt.executeUpdate() > 0 } catch (e: Exception) { false }
            }
        }
        return false
    }

    override fun obtenerUsuariosDeAsignatura(idAsignatura: Int): List<Usuario> {
        val usuarios = mutableListOf<Usuario>()
        // Hacemos un JOIN para obtener los datos completos del usuario
        val sql = """
            SELECT u.* FROM usuarios u
            INNER JOIN usuario_asignatura ua ON u.id = ua.id_usuario
            WHERE ua.id_asignatura = ?
        """.trimIndent()
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, idAsignatura)
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    usuarios.add(Usuario(
                        id = rs.getInt("id"),
                        nombre = rs.getString("nombre"),
                        email = rs.getString("email"),
                        contrasenya = rs.getString("contraseña"),
                        experiencia = rs.getInt("experiencia"),
                        nivel = rs.getInt("nivel"),
                        id_casa = rs.getInt("id_casa")
                    ))
                }
            }
        }
        return usuarios
    }

    override fun desasignarUsuario(idAsignatura: Int, idUsuario: Int): Boolean {
        val sql = "DELETE FROM usuario_asignatura WHERE id_asignatura = ? AND id_usuario = ?"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, idAsignatura)
                stmt.setInt(2, idUsuario)
                return stmt.executeUpdate() > 0
            }
        }
        return false
    }
}