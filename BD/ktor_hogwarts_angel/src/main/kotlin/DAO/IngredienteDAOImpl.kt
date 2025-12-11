package com.example.DAO

import com.example.models.Ingrediente
import helpers.Database

class IngredienteDAOImpl : IngredienteDAO {

    override fun obtenerTodos(): List<Ingrediente> {
        val ingredientes = mutableListOf<Ingrediente>()
        val sql = "SELECT * FROM ingredientes"
        Database.getConnection()?.use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    ingredientes.add(Ingrediente(
                        id = rs.getInt("id"),
                        nombre = rs.getString("nombre"),
                        sanacion = rs.getInt("sanacion"),
                        analgesia = rs.getInt("analgesia"),
                        curativo = rs.getInt("curativo"),
                        inflamatorio = rs.getInt("inflamatorio")
                    ))
                }
            }
        }
        return ingredientes
    }
}