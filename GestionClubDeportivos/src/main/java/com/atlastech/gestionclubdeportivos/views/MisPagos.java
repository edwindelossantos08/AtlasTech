package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.models.Pago;
import com.atlastech.gestionclubdeportivos.models.Socio;
import com.atlastech.gestionclubdeportivos.dao.PagoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MisPagos extends JFrame {

    public MisPagos(Socio socio) {
        setTitle("💳 MIS PAGOS");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        PagoDAO pagoDAO = new PagoDAO();
        List<Pago> pagos = pagoDAO.obtenerPagosPorSocio(socio.getId());

        String[] columnas = {"ID", "Monto", "Fecha", "Método", "Descripción"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        for (Pago p : pagos) {
            model.addRow(new Object[]{
                p.getId(),
                "$" + p.getMonto(),
                p.getFechaPago(),
                p.getMetodoPago(),
                p.getDescripcion()
            });
        }

        JTable tabla = new JTable(model);
        JScrollPane scroll = new JScrollPane(tabla);

        add(scroll, BorderLayout.CENTER);
    }
}
