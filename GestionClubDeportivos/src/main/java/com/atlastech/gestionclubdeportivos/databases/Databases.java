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
public class Databases {


 final static String url = "jdbc:mysql://localhost:3306/ATLAS_TECH";
    final static String user = "lore";
    final static String password = "root18";
    public static Connection getConection(){
       Connection conectado=null;
        try {
    conectado = DriverManager.getConnection(url, user, password);
    System.out.println("Great, you're connected");}
        catch (Exception e) {
    System.out.println("Opps!, There's been a connection error :/");
    e.printStackTrace(); // PARA VER EL ERROR REAL
}
   return conectado; }
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
