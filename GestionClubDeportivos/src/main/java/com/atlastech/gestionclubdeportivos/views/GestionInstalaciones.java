package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.dao.InstalacionDAO;
import com.atlastech.gestionclubdeportivos.models.Instalacion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;


 // Interfaz de Gestión de Instalaciones
 // @author AtlasTech
 
public class GestionInstalaciones extends JFrame {
    
    private InstalacionDAO instalacionDAO;
    private JTable tableInstalaciones;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtCapacidad, txtCosto, txtBuscar;
    private JComboBox<String> cmbEstado;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnCancelar;
    private Instalacion instalacionSeleccionada;
    
    public GestionInstalaciones(JFrame parent) {
        instalacionDAO = new InstalacionDAO();
        initComponents();
        cargarTabla();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setTitle("Gestión de Instalaciones");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // ========== PANEL SUPERIOR ==========
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(236, 240, 241));
        
        JLabel lblTitulo = new JLabel("🏟️ GESTIÓN DE INSTALACIONES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(52, 73, 94));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JLabel lblBuscar = new JLabel("Buscar:");
        txtBuscar = new JTextField(20);
        JButton btnBuscar = crearBoton("🔍 Buscar", new Color(52, 152, 219));
        btnBuscar.addActionListener(e -> buscarInstalaciones());
        
        JButton btnMostrarTodos = crearBoton("Mostrar Todos", new Color(149, 165, 166));
        btnMostrarTodos.addActionListener(e -> cargarTabla());
        
        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnMostrarTodos);
        
        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        
        // ========== TABLA ==========
        String[] columnas = {"ID", "Nombre", "Capacidad", "Estado", "Costo Reserva"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableInstalaciones = new JTable(modeloTabla);
        tableInstalaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableInstalaciones.setRowHeight(25);
        tableInstalaciones.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableInstalaciones.getTableHeader().setBackground(new Color(52, 73, 94));
        tableInstalaciones.getTableHeader().setForeground(Color.WHITE);
        
        tableInstalaciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableInstalaciones.getSelectedRow() != -1) {
                cargarInstalacionSeleccionada();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableInstalaciones);
        
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
        
        JLabel lblFormTitulo = new JLabel("DATOS DE INSTALACIÓN");
        lblFormTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblFormTitulo, gbc);
        
        gbc.gridwidth = 1;
        int row = 1;
        
        // Nombre
        agregarCampo(formPanel, gbc, row++, "Nombre:");
        txtNombre = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtNombre, gbc);
        gbc.gridwidth = 1;
        
        // Capacidad
        agregarCampo(formPanel, gbc, row++, "Capacidad (personas):");
        txtCapacidad = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtCapacidad, gbc);
        gbc.gridwidth = 1;
        
        // Estado
        agregarCampo(formPanel, gbc, row++, "Estado:");
        cmbEstado = new JComboBox<>(new String[]{"Disponible", "Mantenimiento", "Ocupada", "Cerrada"});
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(cmbEstado, gbc);
        gbc.gridwidth = 1;
        
        // Costo
        agregarCampo(formPanel, gbc, row++, "Costo de Reserva:");
        txtCosto = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtCosto, gbc);
        gbc.gridwidth = 1;
        
        // Botones
        JPanel botonesPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        botonesPanel.setOpaque(false);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        
        btnNuevo = crearBoton("➕ Nuevo", new Color(52, 152, 219));
        btnGuardar = crearBoton("💾 Guardar", new Color(46, 204, 113));
        btnEditar = crearBoton("✏️ Editar", new Color(241, 196, 15));
        btnEliminar = crearBoton("🗑️ Eliminar", new Color(231, 76, 60));
        btnCancelar = crearBoton("❌ Cancelar", new Color(149, 165, 166));
        
        btnNuevo.addActionListener(e -> nuevaInstalacion());
        btnGuardar.addActionListener(e -> guardarInstalacion());
        btnEditar.addActionListener(e -> editarInstalacion());
        btnEliminar.addActionListener(e -> eliminarInstalacion());
        btnCancelar.addActionListener(e -> cancelar());
        
        botonesPanel.add(btnNuevo);
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnEditar);
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
    
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Instalacion> instalaciones = instalacionDAO.obtenerTodasInstalaciones();
        
        for (Instalacion i : instalaciones) {
            modeloTabla.addRow(new Object[]{
                i.getId(),
                i.getNombre(),
                i.getCapacidadPersonas(),
                i.getEstado(),
                "$" + i.getCostoReserva()
            });
        }
    }
    
    private void buscarInstalaciones() {
        String busqueda = txtBuscar.getText().trim();
        if (busqueda.isEmpty()) {
            cargarTabla();
            return;
        }
        
        modeloTabla.setRowCount(0);
        Instalacion instalacion = instalacionDAO.obtenerInstalacionPorNombre(busqueda);
        
        if (instalacion != null) {
            modeloTabla.addRow(new Object[]{
                instalacion.getId(),
                instalacion.getNombre(),
                instalacion.getCapacidadPersonas(),
                instalacion.getEstado(),
                "$" + instalacion.getCostoReserva()
            });
        }
    }
    
    private void cargarInstalacionSeleccionada() {
        int row = tableInstalaciones.getSelectedRow();
        if (row == -1) return;
        
        int id = (int) modeloTabla.getValueAt(row, 0);
        instalacionSeleccionada = instalacionDAO.obtenerInstalacionPorId(id);
        
        if (instalacionSeleccionada != null) {
            txtNombre.setText(instalacionSeleccionada.getNombre());
            txtCapacidad.setText(String.valueOf(instalacionSeleccionada.getCapacidadPersonas()));
            cmbEstado.setSelectedItem(instalacionSeleccionada.getEstado());
            txtCosto.setText(instalacionSeleccionada.getCostoReserva().toString());
            
            configurarEstadoBotones(false);
        }
    }
    
    private void nuevaInstalacion() {
        limpiarCampos();
        instalacionSeleccionada = null;
        configurarEstadoBotones(true);
        txtNombre.requestFocus();
    }
    
    private void guardarInstalacion() {
        if (!validarCampos()) return;
        
        try {
            Instalacion instalacion = new Instalacion();
            instalacion.setNombre(txtNombre.getText().trim());
            instalacion.setCapacidadPersonas(Integer.parseInt(txtCapacidad.getText().trim()));
            instalacion.setEstado((String) cmbEstado.getSelectedItem());
            instalacion.setCostoReserva(new BigDecimal(txtCosto.getText().trim()));
            
            if (instalacionDAO.insertarInstalacion(instalacion)) {
                JOptionPane.showMessageDialog(this, 
                    "Instalación registrada exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
                configurarEstadoBotones(false);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al registrar instalación", 
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
    
    private void editarInstalacion() {
        if (instalacionSeleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una instalación de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validarCampos()) return;
        
        try {
            instalacionSeleccionada.setNombre(txtNombre.getText().trim());
            instalacionSeleccionada.setCapacidadPersonas(Integer.parseInt(txtCapacidad.getText().trim()));
            instalacionSeleccionada.setEstado((String) cmbEstado.getSelectedItem());
            instalacionSeleccionada.setCostoReserva(new BigDecimal(txtCosto.getText().trim()));
            
            if (instalacionDAO.actualizarInstalacion(instalacionSeleccionada)) {
                JOptionPane.showMessageDialog(this, 
                    "Instalación actualizada exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarInstalacion() {
        if (instalacionSeleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una instalación de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de cambiar el estado de esta instalación a 'Cerrada'?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (instalacionDAO.cambiarEstado(instalacionSeleccionada.getId(), "Cerrada")) {
                JOptionPane.showMessageDialog(this, 
                    "Estado actualizado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
            }
        }
    }
    
    private void cancelar() {
        limpiarCampos();
        instalacionSeleccionada = null;
        configurarEstadoBotones(false);
        tableInstalaciones.clearSelection();
    }
    
    private void limpiarCampos() {
        txtNombre.setText("");
        txtCapacidad.setText("");
        cmbEstado.setSelectedIndex(0);
        txtCosto.setText("");
    }
    
    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty() ||
            txtCapacidad.getText().trim().isEmpty() ||
            txtCosto.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Complete todos los campos obligatorios", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            Integer.parseInt(txtCapacidad.getText().trim());
            new BigDecimal(txtCosto.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Capacidad y costo deben ser valores numéricos válidos", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void configurarEstadoBotones(boolean modoGuardar) {
        btnGuardar.setEnabled(modoGuardar);
        btnNuevo.setEnabled(!modoGuardar);
        btnEditar.setEnabled(!modoGuardar && instalacionSeleccionada != null);
        btnEliminar.setEnabled(!modoGuardar && instalacionSeleccionada != null);
        btnCancelar.setEnabled(modoGuardar);
    }
}