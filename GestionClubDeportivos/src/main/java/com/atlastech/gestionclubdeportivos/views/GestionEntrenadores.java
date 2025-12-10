package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.dao.EntrenadorDAO;
import com.atlastech.gestionclubdeportivos.models.Entrenador;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;


 // Interfaz de Gestión de Entrenadores
 // @author AtlasTech
 
public class GestionEntrenadores extends JFrame {
    
    private EntrenadorDAO entrenadorDAO;
    private JTable tableEntrenadores;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombres, txtApellidos, txtEmail, txtTelefono, txtEspecialidad, txtBuscar;
    private JCheckBox chkEstado;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnCancelar;
    private Entrenador entrenadorSeleccionado;
    
    public GestionEntrenadores(JFrame parent) {
        entrenadorDAO = new EntrenadorDAO();
        initComponents();
        cargarTabla();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setTitle("Gestión de Entrenadores");
        setSize(1200, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // ========== PANEL SUPERIOR ==========
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(236, 240, 241));
        
        JLabel lblTitulo = new JLabel("👨‍🏫 GESTIÓN DE ENTRENADORES");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(52, 73, 94));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JLabel lblBuscar = new JLabel("Buscar especialidad:");
        txtBuscar = new JTextField(15);
        JButton btnBuscar = crearBoton("🔍 Buscar", new Color(52, 152, 219));
        btnBuscar.addActionListener(e -> buscarPorEspecialidad());
        
        JButton btnActivos = crearBoton("Activos", new Color(46, 204, 113));
        btnActivos.addActionListener(e -> mostrarActivos());
        
        JButton btnMostrarTodos = crearBoton("Mostrar Todos", new Color(149, 165, 166));
        btnMostrarTodos.addActionListener(e -> cargarTabla());
        
        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnActivos);
        searchPanel.add(btnMostrarTodos);
        
        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        
        // ========== TABLA ==========
        String[] columnas = {"ID", "Nombres", "Apellidos", "Email", "Teléfono", "Especialidad", "Fecha Contratación", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableEntrenadores = new JTable(modeloTabla);
        tableEntrenadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableEntrenadores.setRowHeight(25);
        tableEntrenadores.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tableEntrenadores.getTableHeader().setBackground(new Color(52, 73, 94));
        tableEntrenadores.getTableHeader().setForeground(Color.WHITE);
        
        tableEntrenadores.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableEntrenadores.getSelectedRow() != -1) {
                cargarEntrenadorSeleccionado();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableEntrenadores);
        
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
        
        JLabel lblFormTitulo = new JLabel("DATOS DEL ENTRENADOR");
        lblFormTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblFormTitulo, gbc);
        
        gbc.gridwidth = 1;
        int row = 1;
        
        // Nombres
        agregarCampo(formPanel, gbc, row++, "Nombres:");
        txtNombres = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtNombres, gbc);
        gbc.gridwidth = 1;
        
        // Apellidos
        agregarCampo(formPanel, gbc, row++, "Apellidos:");
        txtApellidos = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtApellidos, gbc);
        gbc.gridwidth = 1;
        
        // Email
        agregarCampo(formPanel, gbc, row++, "Email:");
        txtEmail = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtEmail, gbc);
        gbc.gridwidth = 1;
        
        // Teléfono
        agregarCampo(formPanel, gbc, row++, "Teléfono:");
        txtTelefono = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtTelefono, gbc);
        gbc.gridwidth = 1;
        
        // Especialidad
        agregarCampo(formPanel, gbc, row++, "Especialidad:");
        txtEspecialidad = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtEspecialidad, gbc);
        gbc.gridwidth = 1;
        
        // Estado
        chkEstado = new JCheckBox("Activo");
        chkEstado.setSelected(true);
        chkEstado.setOpaque(false);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(chkEstado, gbc);
        
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
        
        btnNuevo.addActionListener(e -> nuevoEntrenador());
        btnGuardar.addActionListener(e -> guardarEntrenador());
        btnEditar.addActionListener(e -> editarEntrenador());
        btnEliminar.addActionListener(e -> eliminarEntrenador());
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
        List<Entrenador> entrenadores = entrenadorDAO.obtenerTodosEntrenadores();
        
        for (Entrenador e : entrenadores) {
            modeloTabla.addRow(new Object[]{
                e.getId(),
                e.getNombres(),
                e.getApellidos(),
                e.getEmail(),
                e.getTelefono(),
                e.getEspecialidad(),
                e.getFechaContratacion(),
                e.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }
    
    private void mostrarActivos() {
        modeloTabla.setRowCount(0);
        List<Entrenador> entrenadores = entrenadorDAO.obtenerEntrenadoresActivos();
        
        for (Entrenador e : entrenadores) {
            modeloTabla.addRow(new Object[]{
                e.getId(),
                e.getNombres(),
                e.getApellidos(),
                e.getEmail(),
                e.getTelefono(),
                e.getEspecialidad(),
                e.getFechaContratacion(),
                "Activo"
            });
        }
    }
    
    private void buscarPorEspecialidad() {
        String especialidad = txtBuscar.getText().trim();
        if (especialidad.isEmpty()) {
            cargarTabla();
            return;
        }
        
        modeloTabla.setRowCount(0);
        List<Entrenador> entrenadores = entrenadorDAO.obtenerEntrenadoresPorEspecialidad(especialidad);
        
        for (Entrenador e : entrenadores) {
            modeloTabla.addRow(new Object[]{
                e.getId(),
                e.getNombres(),
                e.getApellidos(),
                e.getEmail(),
                e.getTelefono(),
                e.getEspecialidad(),
                e.getFechaContratacion(),
                e.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }
    
    private void cargarEntrenadorSeleccionado() {
        int row = tableEntrenadores.getSelectedRow();
        if (row == -1) return;
        
        int id = (int) modeloTabla.getValueAt(row, 0);
        entrenadorSeleccionado = entrenadorDAO.obtenerEntrenadorPorId(id);
        
        if (entrenadorSeleccionado != null) {
            txtNombres.setText(entrenadorSeleccionado.getNombres());
            txtApellidos.setText(entrenadorSeleccionado.getApellidos());
            txtEmail.setText(entrenadorSeleccionado.getEmail());
            txtTelefono.setText(entrenadorSeleccionado.getTelefono());
            txtEspecialidad.setText(entrenadorSeleccionado.getEspecialidad());
            chkEstado.setSelected(entrenadorSeleccionado.isEstado());
            
            configurarEstadoBotones(false);
        }
    }
    
    private void nuevoEntrenador() {
        limpiarCampos();
        entrenadorSeleccionado = null;
        configurarEstadoBotones(true);
        txtNombres.requestFocus();
    }
    
    private void guardarEntrenador() {
        if (!validarCampos()) return;
        
        try {
            Entrenador entrenador = new Entrenador();
            entrenador.setNombres(txtNombres.getText().trim());
            entrenador.setApellidos(txtApellidos.getText().trim());
            entrenador.setEmail(txtEmail.getText().trim());
            entrenador.setTelefono(txtTelefono.getText().trim());
            entrenador.setEspecialidad(txtEspecialidad.getText().trim());
            entrenador.setFechaContratacion(LocalDate.now());
            entrenador.setEstado(chkEstado.isSelected());
            
            if (entrenadorDAO.insertarEntrenador(entrenador)) {
                JOptionPane.showMessageDialog(this, 
                    "Entrenador registrado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
                configurarEstadoBotones(false);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al registrar entrenador", 
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
    
    private void editarEntrenador() {
        if (entrenadorSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un entrenador de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validarCampos()) return;
        
        try {
            entrenadorSeleccionado.setNombres(txtNombres.getText().trim());
            entrenadorSeleccionado.setApellidos(txtApellidos.getText().trim());
            entrenadorSeleccionado.setEmail(txtEmail.getText().trim());
            entrenadorSeleccionado.setTelefono(txtTelefono.getText().trim());
            entrenadorSeleccionado.setEspecialidad(txtEspecialidad.getText().trim());
            entrenadorSeleccionado.setEstado(chkEstado.isSelected());
            
            if (entrenadorDAO.actualizarEntrenador(entrenadorSeleccionado)) {
                JOptionPane.showMessageDialog(this, 
                    "Entrenador actualizado exitosamente", 
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
    
    private void eliminarEntrenador() {
        if (entrenadorSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un entrenador de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de cambiar el estado del entrenador?\nSe recomienda desactivar en lugar de eliminar.",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (entrenadorDAO.cambiarEstado(entrenadorSeleccionado.getId(), false)) {
                JOptionPane.showMessageDialog(this, 
                    "Entrenador desactivado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
            }
        }
    }
    
    private void cancelar() {
        limpiarCampos();
        entrenadorSeleccionado = null;
        configurarEstadoBotones(false);
        tableEntrenadores.clearSelection();
    }
    
    private void limpiarCampos() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtEspecialidad.setText("");
        chkEstado.setSelected(true);
    }
    
    private boolean validarCampos() {
        if (txtNombres.getText().trim().isEmpty() ||
            txtApellidos.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty() ||
            txtEspecialidad.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Complete todos los campos obligatorios", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void configurarEstadoBotones(boolean modoGuardar) {
        btnGuardar.setEnabled(modoGuardar);
        btnNuevo.setEnabled(!modoGuardar);
        btnEditar.setEnabled(!modoGuardar && entrenadorSeleccionado != null);
        btnEliminar.setEnabled(!modoGuardar && entrenadorSeleccionado != null);
        btnCancelar.setEnabled(modoGuardar);
    }
}