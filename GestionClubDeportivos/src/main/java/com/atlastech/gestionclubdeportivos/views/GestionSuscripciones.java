package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.dao.*;
import com.atlastech.gestionclubdeportivos.models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


 // Interfaz de Gestión de Suscripciones
 // @author AtlasTech
 
public class GestionSuscripciones extends JFrame {
    
    private SuscripcionDAO suscripcionDAO;
    private SocioDAO socioDAO;
    private MembresiaDAO membresiaDAO;
    private JTable tableSuscripciones;
    private DefaultTableModel modeloTabla;
    private JComboBox<Socio> cmbSocio;
    private JComboBox<Membresia> cmbMembresia;
    private JCheckBox chkRenovacionAuto;
    private JButton btnNuevo, btnGuardar, btnEliminar, btnCancelar;
    private Suscripcion suscripcionSeleccionada;
    
    public GestionSuscripciones(JFrame parent) {
        suscripcionDAO = new SuscripcionDAO();
        socioDAO = new SocioDAO();
        membresiaDAO = new MembresiaDAO();
        initComponents();
        cargarTabla();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setTitle("Gestión de Suscripciones");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // ========== PANEL SUPERIOR ==========
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(236, 240, 241));
        
        JLabel lblTitulo = new JLabel("📝 GESTIÓN DE SUSCRIPCIONES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(52, 73, 94));
        
        JButton btnRefrescar = crearBoton("🔄 Refrescar", new Color(52, 152, 219));
        btnRefrescar.addActionListener(e -> cargarTabla());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnRefrescar);
        
        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // ========== TABLA ==========
        String[] columnas = {"ID", "Socio", "Membresía", "Renovación Auto", "Fecha Inicio", "Duración (Meses)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableSuscripciones = new JTable(modeloTabla);
        tableSuscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableSuscripciones.setRowHeight(25);
        tableSuscripciones.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableSuscripciones.getTableHeader().setBackground(new Color(52, 73, 94));
        tableSuscripciones.getTableHeader().setForeground(Color.WHITE);
        
        tableSuscripciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableSuscripciones.getSelectedRow() != -1) {
                cargarSuscripcionSeleccionada();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableSuscripciones);
        
        // ========== FORMULARIO ==========
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setPreferredSize(new Dimension(400, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel lblFormTitulo = new JLabel("NUEVA SUSCRIPCIÓN");
        lblFormTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblFormTitulo, gbc);
        
        gbc.gridwidth = 1;
        int row = 1;
        
        // Socio
        agregarCampo(formPanel, gbc, row++, "Seleccionar Socio:");
        cmbSocio = new JComboBox<>();
        cargarSocios();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(cmbSocio, gbc);
        gbc.gridwidth = 1;
        
        // Membresía
        agregarCampo(formPanel, gbc, row++, "Seleccionar Membresía:");
        cmbMembresia = new JComboBox<>();
        cargarMembresias();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(cmbMembresia, gbc);
        gbc.gridwidth = 1;
        
        // Renovación automática
        chkRenovacionAuto = new JCheckBox("Renovación Automática");
        chkRenovacionAuto.setOpaque(false);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(chkRenovacionAuto, gbc);
        
        // Info
        JLabel lblInfo = new JLabel("<html><p style='font-size:10px; color:gray;'>" +
            "La fecha de inicio se establecerá automáticamente al crear la suscripción.</p></html>");
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(lblInfo, gbc);
        
        // Botones
        JPanel botonesPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        botonesPanel.setOpaque(false);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        
        btnNuevo = crearBoton("➕ Nueva Suscripción", new Color(52, 152, 219));
        btnGuardar = crearBoton("💾 Guardar", new Color(46, 204, 113));
        btnEliminar = crearBoton("🗑️ Eliminar", new Color(231, 76, 60));
        btnCancelar = crearBoton("❌ Cancelar", new Color(149, 165, 166));
        
        btnNuevo.addActionListener(e -> nuevaSuscripcion());
        btnGuardar.addActionListener(e -> guardarSuscripcion());
        btnEliminar.addActionListener(e -> eliminarSuscripcion());
        btnCancelar.addActionListener(e -> cancelar());
        
        botonesPanel.add(btnNuevo);
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnEliminar);
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
    
    private void cargarSocios() {
        cmbSocio.removeAllItems();
        List<Socio> socios = socioDAO.obtenerSociosActivos();
        for (Socio s : socios) {
            cmbSocio.addItem(s);
        }
        cmbSocio.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Socio) {
                    Socio s = (Socio) value;
                    setText(s.getId() + " - " + s.getNombreCompleto());
                }
                return this;
            }
        });
    }
    
    private void cargarMembresias() {
        cmbMembresia.removeAllItems();
        List<Membresia> membresias = membresiaDAO.obtenerTodasMembresias();
        for (Membresia m : membresias) {
            cmbMembresia.addItem(m);
        }
        cmbMembresia.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Membresia) {
                    Membresia m = (Membresia) value;
                    setText(m.getTipo() + " - $" + m.getPrecio());
                }
                return this;
            }
        });
    }
    
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Suscripcion> suscripciones = suscripcionDAO.obtenerTodas();
        
        for (Suscripcion s : suscripciones) {
            Socio socio = socioDAO.obtenerSocioPorId(s.getIdSocio());
            Membresia membresia = membresiaDAO.obtenerMembresiaPorId(s.getIdMembresia());
            
            String nombreSocio = socio != null ? socio.getNombreCompleto() : "N/A";
            String tipoMembresia = membresia != null ? membresia.getTipo() : "N/A";
            
            modeloTabla.addRow(new Object[]{
                s.getId(),
                nombreSocio,
                tipoMembresia,
                s.isRenovacionAutomatica() ? "Sí" : "No",
                s.getFechaInicio() != null ? s.getFechaInicio() : "N/A",
                s.getDuracionMeses() != null ? s.getDuracionMeses() : "N/A"
            });
        }
    }
    
    private void cargarSuscripcionSeleccionada() {
        int row = tableSuscripciones.getSelectedRow();
        if (row == -1) return;
        
        int id = (int) modeloTabla.getValueAt(row, 0);
        suscripcionSeleccionada = suscripcionDAO.obtenerPorId(id);
        
        if (suscripcionSeleccionada != null) {
            // Seleccionar socio
            for (int i = 0; i < cmbSocio.getItemCount(); i++) {
                Socio s = cmbSocio.getItemAt(i);
                if (s.getId() == suscripcionSeleccionada.getIdSocio()) {
                    cmbSocio.setSelectedIndex(i);
                    break;
                }
            }
            
            // Seleccionar membresía
            for (int i = 0; i < cmbMembresia.getItemCount(); i++) {
                Membresia m = cmbMembresia.getItemAt(i);
                if (m.getId() == suscripcionSeleccionada.getIdMembresia()) {
                    cmbMembresia.setSelectedIndex(i);
                    break;
                }
            }
            
            chkRenovacionAuto.setSelected(suscripcionSeleccionada.isRenovacionAutomatica());
            configurarEstadoBotones(false);
        }
    }
    
    private void nuevaSuscripcion() {
        limpiarCampos();
        suscripcionSeleccionada = null;
        configurarEstadoBotones(true);
    }
    
    private void guardarSuscripcion() {
        if (!validarCampos()) return;
        
        try {
            Socio socio = (Socio) cmbSocio.getSelectedItem();
            Membresia membresia = (Membresia) cmbMembresia.getSelectedItem();
            
            Suscripcion suscripcion = new Suscripcion();
            suscripcion.setIdSocio(socio.getId());
            suscripcion.setIdMembresia(membresia.getId());
            suscripcion.setRenovacionAutomatica(chkRenovacionAuto.isSelected());
            
            if (suscripcionDAO.registrarSuscripcion(suscripcion)) {
                JOptionPane.showMessageDialog(this, 
                    "Suscripción registrada exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
                configurarEstadoBotones(false);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al registrar suscripción", 
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
    
    private void eliminarSuscripcion() {
        if (suscripcionSeleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una suscripción de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar esta suscripción?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (suscripcionDAO.eliminar(suscripcionSeleccionada.getId())) {
                JOptionPane.showMessageDialog(this, 
                    "Suscripción eliminada exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
            }
        }
    }
    
    private void cancelar() {
        limpiarCampos();
        suscripcionSeleccionada = null;
        configurarEstadoBotones(false);
        tableSuscripciones.clearSelection();
    }
    
    private void limpiarCampos() {
        if (cmbSocio.getItemCount() > 0) cmbSocio.setSelectedIndex(0);
        if (cmbMembresia.getItemCount() > 0) cmbMembresia.setSelectedIndex(0);
        chkRenovacionAuto.setSelected(false);
    }
    
    private boolean validarCampos() {
        if (cmbSocio.getSelectedItem() == null || cmbMembresia.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un socio y una membresía", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void configurarEstadoBotones(boolean modoGuardar) {
        btnGuardar.setEnabled(modoGuardar);
        btnNuevo.setEnabled(!modoGuardar);
        btnEliminar.setEnabled(!modoGuardar && suscripcionSeleccionada != null);
        btnCancelar.setEnabled(modoGuardar);
    }
}