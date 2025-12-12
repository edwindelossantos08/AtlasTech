
package com.atlastech.gestionclubdeportivos;
import com.atlastech.gestionclubdeportivos.views.Login;
import javax.swing.SwingUtilities;

/**
 *
 * @author bryanpeguerocamilo
 */

public class GestionClubDeportivos {
   public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setLocationRelativeTo(null); // Centrar ventana
            login.setVisible(true);
        });
    }
}

