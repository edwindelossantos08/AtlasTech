package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.dao.DeporteDAO;
import com.atlastech.gestionclubdeportivos.models.Deporte;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


 // Interfaz de Gestión de Deportes
 // @author AtlasTech
 
public class GestionDeportes extends JFrame {
    
    private DeporteDAO deporteDAO;
    private JTable tableDeportes;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtDescripcion, txtBuscar;
    private JButton btnNuevo, btnGuardar, btnEditar, btnCancelar;
    private Deporte deporteSeleccionado;
    
    public GestionDeportes(JFrame parent) {
        deporteDAO = new DeporteDAO();
        initComponents();
        cargarTabla();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setTitle("Gestión de Deportes");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // ========== PANEL SUPERIOR ==========
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(236, 240, 241));
        
        JLabel lblTitulo = new JLabel("⚽ GESTIÓN DE DEPORTES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(52, 73, 94));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JLabel lblBuscar = new JLabel("Buscar:");
        txtBuscar = new JTextField(20);
        JButton btnBuscar = crearBoton("🔍 Buscar", new Color(52, 152, 219));
        btnBuscar.addActionListener(e -> buscarDeporte());
        
        JButton btnMostrarTodos = crearBoton("Mostrar Todos", new Color(149, 165, 166));
        btnMostrarTodos.addActionListener(e -> cargarTabla());
        
        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnMostrarTodos);
        
        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        
        // ========== TABLA ==========
        String[] columnas = {"ID", "Nombre del Deporte", "Descripción"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableDeportes = new JTable(modeloTabla);
        tableDeportes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableDeportes.setRowHeight(30);
        tableDeportes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tableDeportes.getTableHeader().setBackground(new Color(52, 73, 94));
        tableDeportes.getTableHeader().setForeground(Color.WHITE);
        tableDeportes.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Ajustar ancho de columnas
        tableDeportes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableDeportes.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableDeportes.getColumnModel().getColumn(2).setPreferredWidth(400);
        
        tableDeportes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableDeportes.getSelectedRow() != -1) {
                cargarDeporteSeleccionado();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableDeportes);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        
        // ========== FORMULARIO ==========
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        formPanel.setPreferredSize(new Dimension(400, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        JLabel lblFormTitulo = new JLabel("DATOS DEL DEPORTE");
        lblFormTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblFormTitulo.setForeground(new Color(52, 73, 94));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblFormTitulo, gbc);
        
        // Separador
        JSeparator separator = new JSeparator();
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 0, 15, 0);
        formPanel.add(separator, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
        int row = 2;
        
        // Nombre
        JLabel lblNombre = new JLabel("Nombre del Deporte:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(lblNombre, gbc);
        
        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 13));
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtNombre, gbc);
        
        // Descripción
        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(lblDescripcion, gbc);
        
        txtDescripcion = new JTextField();
        txtDescripcion.setFont(new Font("Arial", Font.PLAIN, 13));
        txtDescripcion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtDescripcion, gbc);
        
        // Info adicional
        JLabel lblInfo = new JLabel("<html><p style='font-size:11px; color:gray; margin-top:10px;'>" +
            "Los deportes registrados aquí estarán disponibles para asociar con torneos, " +
            "instalaciones y entrenadores especializados.</p></html>");
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(lblInfo, gbc);
        
        // Botones
        JPanel botonesPanel = new JPanel(new GridLayout(4, 1, 8, 8));
        botonesPanel.setOpaque(false);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 8, 8, 8);
        
        btnNuevo = crearBoton("➕ Nuevo Deporte", new Color(52, 152, 219));
        btnGuardar = crearBoton("💾 Guardar", new Color(46, 204, 113));
        btnEditar = crearBoton("✏️ Editar", new Color(241, 196, 15));
        btnCancelar = crearBoton("❌ Cancelar", new Color(149, 165, 166));
        
        btnNuevo.addActionListener(e -> nuevoDeporte());
        btnGuardar.addActionListener(e -> guardarDeporte());
        btnEditar.addActionListener(e -> editarDeporte());
        btnCancelar.addActionListener(e -> cancelar());
        
        botonesPanel.add(btnNuevo);
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnEditar);
        botonesPanel.add(btnCancelar);
        
        formPanel.add(botonesPanel, gbc);
        
        // Ensamblar
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(formPanel, BorderLayout.EAST);
        
        add(mainPanel);
        configurarEstadoBotones(false);
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(0, 45));
        
        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = btn.getBackground();
            
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(originalColor.darker());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(originalColor);
            }
        });
        
        return btn;
    }
    
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Deporte> deportes = deporteDAO.obtenerTodosDeportes();
        
        for (Deporte d : deportes) {
            modeloTabla.addRow(new Object[]{
                d.getId(),
                d.getNombre(),
                d.getDescripcion()
            });
        }
        
        // Mostrar cantidad
        if (deportes.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No hay deportes registrados", 
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void buscarDeporte() {
        String busqueda = txtBuscar.getText().trim();
        if (busqueda.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Ingrese un nombre para buscar", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Deporte deporte = deporteDAO.buscarDeportePorNombre(busqueda);
        
        if (deporte != null) {
            modeloTabla.setRowCount(0);
            modeloTabla.addRow(new Object[]{
                deporte.getId(),
                deporte.getNombre(),
                deporte.getDescripcion()
            });
        } else {
            JOptionPane.showMessageDialog(this, 
                "No se encontró ningún deporte con ese nombre", 
                "Sin Resultados", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void cargarDeporteSeleccionado() {
        int row = tableDeportes.getSelectedRow();
        if (row == -1) return;
        
        int id = (int) modeloTabla.getValueAt(row, 0);
        deporteSeleccionado = deporteDAO.obtenerDeportePorId(id);
        
        if (deporteSeleccionado != null) {
            txtNombre.setText(deporteSeleccionado.getNombre());
            txtDescripcion.setText(deporteSeleccionado.getDescripcion());
            
            configurarEstadoBotones(false);
        }
    }
    
    private void nuevoDeporte() {
        limpiarCampos();
        deporteSeleccionado = null;
        configurarEstadoBotones(true);
        txtNombre.requestFocus();
    }
    
    private void guardarDeporte() {
        if (!validarCampos()) return;
        
        try {
            Deporte deporte = new Deporte();
            deporte.setNombre(txtNombre.getText().trim());
            deporte.setDescripcion(txtDescripcion.getText().trim());
            
            if (deporteDAO.insertarDeporte(deporte)) {
                JOptionPane.showMessageDialog(this, 
                    "Deporte registrado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
                configurarEstadoBotones(false);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al registrar deporte", 
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
    
    private void editarDeporte() {
        if (deporteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un deporte de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validarCampos()) return;
        
        try {
            deporteSeleccionado.setNombre(txtNombre.getText().trim());
            deporteSeleccionado.setDescripcion(txtDescripcion.getText().trim());
            
            if (deporteDAO.actualizarDeporte(deporteSeleccionado)) {
                JOptionPane.showMessageDialog(this, 
                    "Deporte actualizado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al actualizar deporte", 
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
    
    private void cancelar() {
        limpiarCampos();
        deporteSeleccionado = null;
        configurarEstadoBotones(false);
        tableDeportes.clearSelection();
    }
    
    private void limpiarCampos() {
        txtNombre.setText("");
        txtDescripcion.setText("");
    }
    
    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "El nombre del deporte es obligatorio", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return false;
        }
        
        if (txtDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "La descripción es obligatoria", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            txtDescripcion.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void configurarEstadoBotones(boolean modoGuardar) {
        btnGuardar.setEnabled(modoGuardar);
        btnNuevo.setEnabled(!modoGuardar);
        btnEditar.setEnabled(!modoGuardar && deporteSeleccionado != null);
        btnCancelar.setEnabled(modoGuardar);
        
        // Deshabilitar campos cuando no está en modo edición
        txtNombre.setEnabled(modoGuardar);
        txtDescripcion.setEnabled(modoGuardar);
    }
}
