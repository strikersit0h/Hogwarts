package com.example.DAO

import com.example.models.Pocima
import helpers.Database
import java.sql.Statement

class PocimaDAOImpl : PocimaDAO {

    override fun obtenerTodas(): List<Pocima> {val pocimas = mutableListOf<Pocima>()
        val sql = "SELECT * FROM pociones WHERE fecha_borrado IS NULL"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    pocimas.add(Pocima(
                        id = rs.getInt("id"),
                        nombre = rs.getString("nombre"),
                        fecha_creacion = rs.getString("fecha_creacion"),
                        fecha_modificacion = rs.getString("fecha_modificacion"),
                        fecha_borrado = rs.getString("fecha_borrado"),
                        indicador_bien_mal = rs.getBoolean("indicador_bien_mal"),
                        id_creador = rs.getInt("id_creador"),
                        validada = rs.getBoolean("validada")
                    ))
                }
            }
        }
        return pocimas
    }

    override fun crear(pocima: Pocima): Pocima? {
        val sql = "INSERT INTO pociones (nombre, id_creador) VALUES (?, ?)"
        var generatedId: Int? = null
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { stmt ->
                stmt.setString(1, pocima.nombre)
                stmt.setInt(2, pocima.id_creador)
                if (stmt.executeUpdate() > 0) {
                    val keys = stmt.generatedKeys
                    if (keys.next()) {
                        generatedId = keys.getInt(1)
                    }
                }
            }
        }
        return generatedId?.let { pocima.copy(id = it) }
    }

    override fun actualizar(id: Int, pocima: Pocima): Boolean {
        val sql = "UPDATE pociones SET nombre = ?, fecha_modificacion = CURRENT_TIMESTAMP WHERE id = ?"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, pocima.nombre)
                stmt.setInt(2, id)
                return stmt.executeUpdate() > 0
            }
        }
        return false
    }

    override fun eliminar(id: Int): Boolean {
        val sql = "UPDATE pociones SET fecha_borrado = CURRENT_TIMESTAMP WHERE id = ?"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                return stmt.executeUpdate() > 0
            }
        }
        return false
    }

    override fun validar(id: Int, estado: Boolean): Boolean {
        // función para que el profesor valide o desvalide una pócima
        val sql = "UPDATE pociones SET validada = ?, fecha_modificacion = CURRENT_TIMESTAMP WHERE id = ?"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setBoolean(1, estado)
                stmt.setInt(2, id)
                return stmt.executeUpdate() > 0
            }
        }
        return false
    }
}