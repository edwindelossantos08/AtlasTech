
package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.dao.MembresiaDAO;
import com.atlastech.gestionclubdeportivos.models.Membresia;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;


 // Interfaz de Gestión de Membresías
 // @author AtlasTech
 
public class GestionMembresias extends JFrame {
    
    private MembresiaDAO membresiaDAO;
    private JTable tableMembresias;
    private DefaultTableModel modeloTabla;
    private JTextField txtTipo, txtPrecio, txtDuracionDias, txtDescripcion, txtBuscar;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnCancelar;
    private Membresia membresiaSeleccionada;
    
    public GestionMembresias(JFrame parent) {
        membresiaDAO = new MembresiaDAO();
        initComponents();
        cargarTabla();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setTitle("Gestión de Membresías");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // ========== PANEL SUPERIOR ==========
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(236, 240, 241));
        
        JLabel lblTitulo = new JLabel("💳 GESTIÓN DE MEMBRESÍAS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(52, 73, 94));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JLabel lblBuscar = new JLabel("Buscar:");
        txtBuscar = new JTextField(20);
        JButton btnBuscar = crearBoton("🔍 Buscar", new Color(52, 152, 219));
        btnBuscar.addActionListener(e -> buscarMembresias());
        
        JButton btnMostrarTodos = crearBoton("Mostrar Todos", new Color(149, 165, 166));
        btnMostrarTodos.addActionListener(e -> cargarTabla());
        
        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnMostrarTodos);
        
        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        
        // ========== TABLA ==========
        String[] columnas = {"ID", "Tipo", "Precio", "Duración (Días)", "Descripción"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableMembresias = new JTable(modeloTabla);
        tableMembresias.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableMembresias.setRowHeight(25);
        tableMembresias.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableMembresias.getTableHeader().setBackground(new Color(52, 73, 94));
        tableMembresias.getTableHeader().setForeground(Color.WHITE);
        
        tableMembresias.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableMembresias.getSelectedRow() != -1) {
                cargarMembresiaSeleccionada();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableMembresias);
        
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
        
        JLabel lblFormTitulo = new JLabel("DATOS DE MEMBRESÍA");
        lblFormTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblFormTitulo, gbc);
        
        gbc.gridwidth = 1;
        int row = 1;
        
        // Tipo
        agregarCampo(formPanel, gbc, row++, "Tipo de Membresía:");
        txtTipo = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtTipo, gbc);
        gbc.gridwidth = 1;
        
        // Precio
        agregarCampo(formPanel, gbc, row++, "Precio:");
        txtPrecio = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtPrecio, gbc);
        gbc.gridwidth = 1;
        
        // Duración
        agregarCampo(formPanel, gbc, row++, "Duración (Días):");
        txtDuracionDias = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtDuracionDias, gbc);
        gbc.gridwidth = 1;
        
        // Descripción
        agregarCampo(formPanel, gbc, row++, "Descripción:");
        txtDescripcion = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtDescripcion, gbc);
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
        
        btnNuevo.addActionListener(e -> nuevaMembresia());
        btnGuardar.addActionListener(e -> guardarMembresia());
        btnEditar.addActionListener(e -> editarMembresia());
        btnEliminar.addActionListener(e -> eliminarMembresia());
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
        List<Membresia> membresias = membresiaDAO.obtenerTodasMembresias();
        
        for (Membresia m : membresias) {
            modeloTabla.addRow(new Object[]{
                m.getId(),
                m.getTipo(),
                "$" + m.getPrecio(),
                m.getDuracionDias(),
                m.getDescripcion()
            });
        }
    }
    
    private void buscarMembresias() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            cargarTabla();
            return;
        }
        
        modeloTabla.setRowCount(0);
        List<Membresia> membresias = membresiaDAO.obtenerTodasMembresias();
        
        for (Membresia m : membresias) {
            if (m.getTipo().toLowerCase().contains(busqueda) ||
                (m.getDescripcion() != null && m.getDescripcion().toLowerCase().contains(busqueda))) {
                modeloTabla.addRow(new Object[]{
                    m.getId(),
                    m.getTipo(),
                    "$" + m.getPrecio(),
                    m.getDuracionDias(),
                    m.getDescripcion()
                });
            }
        }
    }
    
    private void cargarMembresiaSeleccionada() {
        int row = tableMembresias.getSelectedRow();
        if (row == -1) return;
        
        int id = (int) modeloTabla.getValueAt(row, 0);
        membresiaSeleccionada = membresiaDAO.obtenerMembresiaPorId(id);
        
        if (membresiaSeleccionada != null) {
            txtTipo.setText(membresiaSeleccionada.getTipo());
            txtPrecio.setText(membresiaSeleccionada.getPrecio().toString());
            txtDuracionDias.setText(String.valueOf(membresiaSeleccionada.getDuracionDias()));
            txtDescripcion.setText(membresiaSeleccionada.getDescripcion());
            
            configurarEstadoBotones(false);
        }
    }
    
    private void nuevaMembresia() {
        limpiarCampos();
        membresiaSeleccionada = null;
        configurarEstadoBotones(true);
        txtTipo.requestFocus();
    }
    
    private void guardarMembresia() {
        if (!validarCampos()) return;
        
        try {
            Membresia membresia = new Membresia();
            membresia.setTipo(txtTipo.getText().trim());
            membresia.setPrecio(new BigDecimal(txtPrecio.getText().trim()));
            membresia.setDuracionDias(Integer.parseInt(txtDuracionDias.getText().trim()));
            membresia.setDescripcion(txtDescripcion.getText().trim());
            
            if (membresiaDAO.insertarMembresia(membresia)) {
                JOptionPane.showMessageDialog(this, 
                    "Membresía registrada exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
                configurarEstadoBotones(false);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al registrar membresía", 
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
    
    private void editarMembresia() {
        if (membresiaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una membresía de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validarCampos()) return;
        
        try {
            membresiaSeleccionada.setTipo(txtTipo.getText().trim());
            membresiaSeleccionada.setPrecio(new BigDecimal(txtPrecio.getText().trim()));
            membresiaSeleccionada.setDuracionDias(Integer.parseInt(txtDuracionDias.getText().trim()));
            membresiaSeleccionada.setDescripcion(txtDescripcion.getText().trim());
            
            if (membresiaDAO.actualizarMembresia(membresiaSeleccionada)) {
                JOptionPane.showMessageDialog(this, 
                    "Membresía actualizada exitosamente", 
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
    
    private void eliminarMembresia() {
        if (membresiaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una membresía de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar esta membresía?\nEsto puede afectar suscripciones existentes.",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (membresiaDAO.eliminarMembresia(membresiaSeleccionada.getId())) {
                JOptionPane.showMessageDialog(this, 
                    "Membresía eliminada exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
            }
        }
    }
    
    private void cancelar() {
        limpiarCampos();
        membresiaSeleccionada = null;
        configurarEstadoBotones(false);
        tableMembresias.clearSelection();
    }
    
    private void limpiarCampos() {
        txtTipo.setText("");
        txtPrecio.setText("");
        txtDuracionDias.setText("");
        txtDescripcion.setText("");
    }
    
    private boolean validarCampos() {
        if (txtTipo.getText().trim().isEmpty() ||
            txtPrecio.getText().trim().isEmpty() ||
            txtDuracionDias.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Complete todos los campos obligatorios", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            new BigDecimal(txtPrecio.getText().trim());
            Integer.parseInt(txtDuracionDias.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Precio y duración deben ser valores numéricos válidos", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void configurarEstadoBotones(boolean modoGuardar) {
        btnGuardar.setEnabled(modoGuardar);
        btnNuevo.setEnabled(!modoGuardar);
        btnEditar.setEnabled(!modoGuardar && membresiaSeleccionada != null);
        btnEliminar.setEnabled(!modoGuardar && membresiaSeleccionada != null);
        btnCancelar.setEnabled(modoGuardar);
    }
}