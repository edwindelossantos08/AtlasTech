package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.dao.PagoDAO;
import com.atlastech.gestionclubdeportivos.models.Pago;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;


 // Interfaz de Gestión de Pagos
 // @author AtlasTech
 
public class GestionPagos extends JFrame {
    
    private PagoDAO pagoDAO;
    private JTable tablePagos;
    private DefaultTableModel modeloTabla;
    private JTextField txtConcepto, txtMonto, txtBuscar;
    private JComboBox<String> cmbMetodoPago, cmbEstado;
    private JButton btnNuevo, btnGuardar, btnMarcarPagado, btnMarcarRechazado, btnCancelar;
    private Pago pagoSeleccionado;
    
    public GestionPagos(JFrame parent) {
        pagoDAO = new PagoDAO();
        initComponents();
        cargarTabla();
        setLocationRelativeTo(parent);
    }

    GestionPagos() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    private void initComponents() {
        setTitle("Gestión de Pagos");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // ========== PANEL SUPERIOR ==========
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(236, 240, 241));
        
        JLabel lblTitulo = new JLabel("💰 GESTIÓN DE PAGOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(52, 73, 94));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JLabel lblBuscar = new JLabel("Buscar:");
        txtBuscar = new JTextField(20);
        JButton btnBuscar = crearBoton("🔍 Buscar", new Color(52, 152, 219));
        btnBuscar.addActionListener(e -> buscarPagos());
        
        JButton btnMostrarTodos = crearBoton("Todos", new Color(149, 165, 166));
        btnMostrarTodos.addActionListener(e -> cargarTabla());
        
        JButton btnPendientes = crearBoton("Pendientes", new Color(241, 196, 15));
        btnPendientes.addActionListener(e -> mostrarPagosPendientes());
        
        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnPendientes);
        searchPanel.add(btnMostrarTodos);
        
        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        
        // ========== TABLA ==========
        String[] columnas = {"ID", "Concepto", "Monto", "Método", "Estado", "ID Suscripción", "ID Reserva", "ID Torneo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablePagos = new JTable(modeloTabla);
        tablePagos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablePagos.setRowHeight(25);
        tablePagos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tablePagos.getTableHeader().setBackground(new Color(52, 73, 94));
        tablePagos.getTableHeader().setForeground(Color.WHITE);
        
        tablePagos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablePagos.getSelectedRow() != -1) {
                cargarPagoSeleccionado();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablePagos);
        
        // ========== FORMULARIO ==========
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setPreferredSize(new Dimension(350, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel lblFormTitulo = new JLabel("DATOS DEL PAGO");
        lblFormTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblFormTitulo, gbc);
        
        gbc.gridwidth = 1;
        int row = 1;
        
        // Concepto
        agregarCampo(formPanel, gbc, row++, "Concepto:");
        txtConcepto = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtConcepto, gbc);
        gbc.gridwidth = 1;
        
        // Monto
        agregarCampo(formPanel, gbc, row++, "Monto:");
        txtMonto = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtMonto, gbc);
        gbc.gridwidth = 1;
        
        // Método de Pago
        agregarCampo(formPanel, gbc, row++, "Método de Pago:");
        cmbMetodoPago = new JComboBox<>(new String[]{"Efectivo", "Tarjeta", "Transferencia", "PayPal", "Otro"});
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(cmbMetodoPago, gbc);
        gbc.gridwidth = 1;
        
        // Estado
        agregarCampo(formPanel, gbc, row++, "Estado:");
        cmbEstado = new JComboBox<>(new String[]{"pendiente", "pagado", "rechazado", "cancelado"});
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(cmbEstado, gbc);
        gbc.gridwidth = 1;
        
        // Info
        JLabel lblInfo = new JLabel("<html><p style='font-size:10px; color:gray;'>" +
            "Nota: Los IDs de suscripción, reserva o torneo deben asignarse manualmente en la BD si es necesario.</p></html>");
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(lblInfo, gbc);
        
        // Botones
        JPanel botonesPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        botonesPanel.setOpaque(false);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        
        btnNuevo = crearBoton("➕ Nuevo", new Color(52, 152, 219));
        btnGuardar = crearBoton("💾 Guardar", new Color(46, 204, 113));
        btnMarcarPagado = crearBoton("✓ Marcar Pagado", new Color(46, 204, 113));
        btnMarcarRechazado = crearBoton("✗ Marcar Rechazado", new Color(231, 76, 60));
        btnCancelar = crearBoton("❌ Cancelar", new Color(149, 165, 166));
        
        btnNuevo.addActionListener(e -> nuevoPago());
        btnGuardar.addActionListener(e -> guardarPago());
        btnMarcarPagado.addActionListener(e -> marcarComoPagado());
        btnMarcarRechazado.addActionListener(e -> marcarComoRechazado());
        btnCancelar.addActionListener(e -> cancelar());
        
        botonesPanel.add(btnNuevo);
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnMarcarPagado);
        botonesPanel.add(btnMarcarRechazado);
        botonesPanel.add(btnCancelar);
        
        formPanel.add(botonesPanel, gbc);
        
        // Ensamblar
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.EAST);
        
        add(mainPanel);
        configurarEstadoBotones(false);
    }
    
    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int row, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(lbl, gbc);
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        return btn;
    }
    
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Pago> pagos = pagoDAO.obtenerTodosPagos();
        
        for (Pago p : pagos) {
            modeloTabla.addRow(new Object[]{
                p.getId(),
                p.getConcepto(),
                "$" + p.getMonto(),
                p.getMetodoPago(),
                p.getEstado(),
                p.getIdSuscripcion() != null ? p.getIdSuscripcion() : "N/A",
                p.getIdReserva() != null ? p.getIdReserva() : "N/A",
                p.getIdTorneo() != null ? p.getIdTorneo() : "N/A"
            });
        }
    }
    
    private void mostrarPagosPendientes() {
        modeloTabla.setRowCount(0);
        List<Pago> pagos = pagoDAO.obtenerPagosPendientes();
        
        for (Pago p : pagos) {
            modeloTabla.addRow(new Object[]{
                p.getId(),
                p.getConcepto(),
                "$" + p.getMonto(),
                p.getMetodoPago(),
                p.getEstado(),
                p.getIdSuscripcion() != null ? p.getIdSuscripcion() : "N/A",
                p.getIdReserva() != null ? p.getIdReserva() : "N/A",
                p.getIdTorneo() != null ? p.getIdTorneo() : "N/A"
            });
        }
    }
    
    private void buscarPagos() {
        String busqueda = txtBuscar.getText().trim();
        if (busqueda.isEmpty()) {
            cargarTabla();
            return;
        }
        
        modeloTabla.setRowCount(0);
        List<Pago> pagos = pagoDAO.obtenerPagosPorConcepto(busqueda);
        
        for (Pago p : pagos) {
            modeloTabla.addRow(new Object[]{
                p.getId(),
                p.getConcepto(),
                "$" + p.getMonto(),
                p.getMetodoPago(),
                p.getEstado(),
                p.getIdSuscripcion() != null ? p.getIdSuscripcion() : "N/A",
                p.getIdReserva() != null ? p.getIdReserva() : "N/A",
                p.getIdTorneo() != null ? p.getIdTorneo() : "N/A"
            });
        }
    }
    
    private void cargarPagoSeleccionado() {
        int row = tablePagos.getSelectedRow();
        if (row == -1) return;
        
        int id = (int) modeloTabla.getValueAt(row, 0);
        pagoSeleccionado = pagoDAO.obtenerPagoPorId(id);
        
        if (pagoSeleccionado != null) {
            txtConcepto.setText(pagoSeleccionado.getConcepto());
            txtMonto.setText(pagoSeleccionado.getMonto().toString());
            cmbMetodoPago.setSelectedItem(pagoSeleccionado.getMetodoPago());
            cmbEstado.setSelectedItem(pagoSeleccionado.getEstado());
            
            configurarEstadoBotones(false);
        }
    }
    
    private void nuevoPago() {
        limpiarCampos();
        pagoSeleccionado = null;
        configurarEstadoBotones(true);
        txtConcepto.requestFocus();
    }
    
    private void guardarPago() {
        if (!validarCampos()) return;
        
        try {
            Pago pago = new Pago();
            pago.setConcepto(txtConcepto.getText().trim());
            pago.setMonto(new BigDecimal(txtMonto.getText().trim()));
            pago.setMetodoPago((String) cmbMetodoPago.getSelectedItem());
            pago.setEstado((String) cmbEstado.getSelectedItem());
            
            if (pagoDAO.registrarPago(pago)) {
                JOptionPane.showMessageDialog(this, 
                    "Pago registrado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
                configurarEstadoBotones(false);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al registrar pago", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void marcarComoPagado() {
        if (pagoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un pago de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (pagoDAO.marcarComoPagado(pagoSeleccionado.getId())) {
            JOptionPane.showMessageDialog(this, 
                "Pago marcado como pagado", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            limpiarCampos();
        }
    }
    
    private void marcarComoRechazado() {
        if (pagoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un pago de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (pagoDAO.marcarComoRechazado(pagoSeleccionado.getId())) {
            JOptionPane.showMessageDialog(this, 
                "Pago marcado como rechazado", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            cargarTabla();
            limpiarCampos();
        }
    }
    
    private void cancelar() {
        limpiarCampos();
        pagoSeleccionado = null;
        configurarEstadoBotones(false);
        tablePagos.clearSelection();
    }
    
    private void limpiarCampos() {
        txtConcepto.setText("");
        txtMonto.setText("");
        cmbMetodoPago.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
    }
    
    private boolean validarCampos() {
        if (txtConcepto.getText().trim().isEmpty() ||
            txtMonto.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Complete todos los campos obligatorios", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            new BigDecimal(txtMonto.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "El monto debe ser un valor numérico válido", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void configurarEstadoBotones(boolean modoGuardar) {
        btnGuardar.setEnabled(modoGuardar);
        btnNuevo.setEnabled(!modoGuardar);
        btnMarcarPagado.setEnabled(!modoGuardar && pagoSeleccionado != null);
        btnMarcarRechazado.setEnabled(!modoGuardar && pagoSeleccionado != null);
        btnCancelar.setEnabled(modoGuardar);
    }
}
