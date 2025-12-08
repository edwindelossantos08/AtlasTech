/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.atlastech.gestionclubdeportivos;
import javax.swing.SwingUtilities;
import com.atlastech.gestionclubdeportivos.ui.menus.Bienvenida;

/**
 *
 * @author bryanpeguerocamilo
 */

public class GestionClubDeportivos {
    public static void main(String[] args) {

        // Iniciar interfaz gráfica de forma segura
        SwingUtilities.invokeLater(() -> {
            Bienvenida login = new Bienvenida();
            login.setLocationRelativeTo(null); // Centrar ventana
            login.setVisible(true);
        });

    }
}
