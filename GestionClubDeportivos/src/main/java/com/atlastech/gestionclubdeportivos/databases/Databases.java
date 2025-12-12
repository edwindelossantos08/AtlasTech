/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.atlastech.gestionclubdeportivos.databases;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Databases {

    private static Connection cn = null;
    private static Databases instance;

    // Configuración de la base de datos
    private final String URL = "jdbc:mysql://localhost:3306/atlas_tech";
    private final String USER = "root";
    private final String PASSWORD = "14162021jj";

    // ❌ Este método lanza error, así que lo quitamos o lo corregimos
    private static void conectar() {
        getInstance(); // Esto probará la conexión correctamente
    }

    private Databases() {
        try {
            cn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Great, you're connected");
        } catch (Exception e) {
            System.out.println("❌ Connection error: " + e.getMessage());
        }
    }

    public static Databases getInstance() {
        if (instance == null) {
            instance = new Databases();
        }
        return instance;
    }

    public static Connection getConection() {
        if (cn == null) {
            new Databases();
        }
        return cn;
    }

    public static void main(String[] args) {
        conectar(); // Ejecuta conexión y muestra si se logró
    }
}
