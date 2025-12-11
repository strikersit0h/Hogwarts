package com.example.DAO

import com.example.models.Hechizo
import helpers.Database
import java.sql.Statement

class HechizoDAOImpl : HechizoDAO {

    override fun obtenerTodos(): List<Hechizo> {
        val hechizos = mutableListOf<Hechizo>()
        val sql = "SELECT * FROM hechizos"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    hechizos.add(Hechizo(
                        id = rs.getInt("id"),
                        nombre = rs.getString("nombre"),
                        descripcion = rs.getString("descripcion"),
                        puntos_experiencia = rs.getInt("puntos_experiencia")
                    ))
                }
            }
        }
        return hechizos
    }

    override fun obtenerHechizosPorNivel(nivelRequerido: Int): List<Hechizo> {
        val hechizos = mutableListOf<Hechizo>()
        val sql = "SELECT * FROM hechizos WHERE puntos_experiencia <= ?"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, nivelRequerido)
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    hechizos.add(Hechizo(
                        id = rs.getInt("id"),
                        nombre = rs.getString("nombre"),
                        descripcion = rs.getString("descripcion"),
                        puntos_experiencia = rs.getInt("puntos_experiencia")
                    ))
                }
            }
        }
        return hechizos
    }

    override fun crear(hechizo: Hechizo): Hechizo? {
        val sql = "INSERT INTO hechizos (nombre, descripcion, puntos_experiencia) VALUES (?, ?, ?)"
        var generatedId: Int? = null
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { stmt ->
                stmt.setString(1, hechizo.nombre)
                stmt.setString(2, hechizo.descripcion)
                stmt.setInt(3, hechizo.puntos_experiencia)
                if (stmt.executeUpdate() > 0) {
                    val keys = stmt.generatedKeys
                    if (keys.next()) {
                        generatedId = keys.getInt(1)
                    }
                }
            }
        }
        return generatedId?.let { hechizo.copy(id = it) }
    }

    override fun actualizar(id: Int, hechizo: Hechizo): Boolean {
        val sql = "UPDATE hechizos SET nombre = ?, descripcion = ? WHERE id = ?"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, hechizo.nombre)
                stmt.setString(2, hechizo.descripcion)
                stmt.setInt(3, id)
                return stmt.executeUpdate() > 0
            }
        }
        return false
    }

    override fun eliminar(id: Int): Boolean {
        val sql = "DELETE FROM hechizos WHERE id = ?"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                return stmt.executeUpdate() > 0
            }
        }
        return false
    }
}