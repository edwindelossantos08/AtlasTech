/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.atlastech.gestionclubdeportivos;

/**
 *
 * @author bryanpeguerocamilo
 */
import com.atlastech.gestionclubdeportivos.databases.Databases;
import java.sql.Connection;
public class GestionClubDeportivos {

    public static void main(String[] args) {
        System.out.println("Hello Atlas Tech!");
        Connection cn = Databases.getConection();


        // Verificar conexión
        if (cn != null) {
            System.out.println("Connected from main successfully :)");
        } else {
            System.out.println("Connection failed from main :(");
        }
    }
}
