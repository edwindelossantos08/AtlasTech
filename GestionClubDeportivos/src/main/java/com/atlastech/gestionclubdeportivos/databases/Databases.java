/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.atlastech.gestionclubdeportivos.databases;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author loren
 */
//public class Databases {
//
//
// final static String url = "jdbc:mysql://localhost:3306/atlas_tech";
//    final static String user = "root";
//    final static String password = "14162021jj";
//    public static Connection getConection(){
//       Connection conectado=null;
//        try {
//    conectado = DriverManager.getConnection(url, user, password);
//    System.out.println("Great, you're connected");}
//        catch (Exception e) {
//    System.out.println("Opps!, There's been a connection error :/");
//    e.printStackTrace(); // PARA VER EL ERROR REAL
//}
//   return conectado; }
//
//    public static Object getInstance() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
//}
        



public class Databases {

    private static Connection cn = null;
    private static Databases instance;

    // Configuración de la base de datos
    private final String URL = "jdbc:mysql://localhost:3306/prueba";
    private final String USER = "root";

    private final String PASSWORD = "root18";


    // Constructor privado (Singleton)
    private Databases() {
        try {
            cn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Great, you're connected");
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }

    // Instancia única
    public static Databases getInstance() {
        if (instance == null) {
            instance = new Databases();
        }
        return instance;
    }

    // Obtener conexión
    public static Connection getConection() {
        if (cn == null) {
            getInstance();
        }
        return cn;
    }
}

    
    


/*Statement query = conectado.createStatement();

    // UPDATE
    int filas = query.executeUpdate("UPDATE SOCIO SET Nombres='Carlos' WHERE Id = 1");
    if (filas > 0) {
        System.out.println("Cambio aplicado correctamente.");
    }

    // SELECT
    ResultSet resultado = query.executeQuery("SELECT Id, Nombres FROM SOCIO WHERE Id = 1");
    while (resultado.next()) {
        System.out.println("Nuevo nombre: " + resultado.getString("Nombres"));
    }

    // Cerrar recursos
    resultado.close();
    query.close();
    conectado.close();*/
