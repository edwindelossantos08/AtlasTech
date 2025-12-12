package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.dao.*;
import com.atlastech.gestionclubdeportivos.models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


 // Interfaz de Gestión de Usuarios
 //@author AtlasTech
 
public class GestionUsuarios extends JFrame {
    
    private UsuarioDAO usuarioDAO;
    private SocioDAO socioDAO;
    private JTable tableUsuarios;
    private DefaultTableModel modeloTabla;
    private JTextField txtUsername, txtEmail, txtBuscar;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbTipoUsuario;
    private JComboBox<Socio> cmbSocio;
    private JCheckBox chkEstado;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnCancelar;
    private Usuario usuarioSeleccionado;
    
    public GestionUsuarios(JFrame parent) {
        usuarioDAO = new UsuarioDAO();
        socioDAO = new SocioDAO();
        initComponents();
        cargarTabla();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setTitle("Gestión de Usuarios");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // ========== PANEL SUPERIOR ==========
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(236, 240, 241));
        
        JLabel lblTitulo = new JLabel("🔐 GESTIÓN DE USUARIOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(52, 73, 94));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JLabel lblBuscar = new JLabel("Buscar:");
        txtBuscar = new JTextField(20);
        JButton btnBuscar = crearBoton("🔍 Buscar", new Color(52, 152, 219));
        btnBuscar.addActionListener(e -> buscarUsuarios());
        
        JButton btnMostrarTodos = crearBoton("Mostrar Todos", new Color(149, 165, 166));
        btnMostrarTodos.addActionListener(e -> cargarTabla());
        
        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnMostrarTodos);
        
        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        
        // ========== TABLA ==========
        String[] columnas = {"ID", "Usuario", "Email", "Tipo", "ID Socio", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableUsuarios = new JTable(modeloTabla);
        tableUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableUsuarios.setRowHeight(25);
        tableUsuarios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableUsuarios.getTableHeader().setBackground(new Color(52, 73, 94));
        tableUsuarios.getTableHeader().setForeground(Color.WHITE);
        
        tableUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableUsuarios.getSelectedRow() != -1) {
                cargarUsuarioSeleccionado();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableUsuarios);
        
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
        
        JLabel lblFormTitulo = new JLabel("DATOS DEL USUARIO");
        lblFormTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblFormTitulo, gbc);
        
        gbc.gridwidth = 1;
        int row = 1;
        
        // Username
        agregarCampo(formPanel, gbc, row++, "Nombre de Usuario:");
        txtUsername = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtUsername, gbc);
        gbc.gridwidth = 1;
        
        // Email
        agregarCampo(formPanel, gbc, row++, "Email:");
        txtEmail = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtEmail, gbc);
        gbc.gridwidth = 1;
        
        // Contraseña
        agregarCampo(formPanel, gbc, row++, "Contraseña:");
        txtPassword = new JPasswordField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtPassword, gbc);
        gbc.gridwidth = 1;
        
        // Tipo Usuario
        agregarCampo(formPanel, gbc, row++, "Tipo de Usuario:");
        cmbTipoUsuario = new JComboBox<>(new String[]{"administrador", "socio"});
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(cmbTipoUsuario, gbc);
        gbc.gridwidth = 1;
        
        // Socio asociado
        agregarCampo(formPanel, gbc, row++, "Socio Asociado:");
        cmbSocio = new JComboBox<>();
        cargarSocios();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(cmbSocio, gbc);
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
        
        btnNuevo.addActionListener(e -> nuevoUsuario());
        btnGuardar.addActionListener(e -> guardarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());
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
    
    private void cargarSocios() {
        cmbSocio.removeAllItems();
        cmbSocio.addItem(null); // Opción vacía
        List<Socio> socios = socioDAO.obtenerSociosActivos();
        for (Socio s : socios) {
            cmbSocio.addItem(s);
        }
        cmbSocio.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("-- Sin Socio --");
                } else if (value instanceof Socio) {
                    Socio s = (Socio) value;
                    setText(s.getId() + " - " + s.getNombreCompleto());
                }
                return this;
            }
        });
    }
    
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Usuario> usuarios = usuarioDAO.obtenerTodos();
        
        for (Usuario u : usuarios) {
            modeloTabla.addRow(new Object[]{
                u.getId(),
                u.getNombreUsuario(),
                u.getEmail(),
                u.getTipoUsuario(),
                u.getIdSocio() != null ? u.getIdSocio() : "N/A",
                u.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }
    
    private void buscarUsuarios() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            cargarTabla();
            return;
        }
        
        modeloTabla.setRowCount(0);
        List<Usuario> usuarios = usuarioDAO.obtenerTodos();
        
        for (Usuario u : usuarios) {
            if (u.getNombreUsuario().toLowerCase().contains(busqueda) ||
                u.getEmail().toLowerCase().contains(busqueda)) {
                modeloTabla.addRow(new Object[]{
                    u.getId(),
                    u.getNombreUsuario(),
                    u.getEmail(),
                    u.getTipoUsuario(),
                    u.getIdSocio() != null ? u.getIdSocio() : "N/A",
                    u.isEstado() ? "Activo" : "Inactivo"
                });
            }
        }
    }
    
    private void cargarUsuarioSeleccionado() {
        int row = tableUsuarios.getSelectedRow();
        if (row == -1) return;
        
        int id = (int) modeloTabla.getValueAt(row, 0);
        usuarioSeleccionado = usuarioDAO.obtenerPorId(id);
        
        if (usuarioSeleccionado != null) {
            txtUsername.setText(usuarioSeleccionado.getNombreUsuario());
            txtEmail.setText(usuarioSeleccionado.getEmail());
            txtPassword.setText(""); // No mostrar contraseña
            cmbTipoUsuario.setSelectedItem(usuarioSeleccionado.getTipoUsuario());
            chkEstado.setSelected(usuarioSeleccionado.isEstado());
            
            // Seleccionar socio
            if (usuarioSeleccionado.getIdSocio() != null) {
                for (int i = 0; i < cmbSocio.getItemCount(); i++) {
                    Socio s = cmbSocio.getItemAt(i);
                    if (s != null && s.getId() == usuarioSeleccionado.getIdSocio()) {
                        cmbSocio.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                cmbSocio.setSelectedIndex(0);
            }
            
            configurarEstadoBotones(false);
        }
    }
    
    private void nuevoUsuario() {
        limpiarCampos();
        usuarioSeleccionado = null;
        configurarEstadoBotones(true);
        txtUsername.requestFocus();
    }
    
    private void guardarUsuario() {
        if (!validarCampos()) return;
        
        try {
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(txtUsername.getText().trim());
            usuario.setEmail(txtEmail.getText().trim());
            usuario.setContraseña(new String(txtPassword.getPassword()));
            usuario.setTipoUsuario((String) cmbTipoUsuario.getSelectedItem());
            
            Socio socioSel = (Socio) cmbSocio.getSelectedItem();
            usuario.setIdSocio(socioSel != null ? socioSel.getId() : null);
            usuario.setEstado(chkEstado.isSelected());
            
            if (usuarioDAO.insertar(usuario)) {
                JOptionPane.showMessageDialog(this, 
                    "Usuario registrado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
                configurarEstadoBotones(false);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al registrar usuario", 
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
    
    private void editarUsuario() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un usuario de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validarCamposEdicion()) return;
        
        try {
            usuarioSeleccionado.setNombreUsuario(txtUsername.getText().trim());
            usuarioSeleccionado.setEmail(txtEmail.getText().trim());
            
            String password = new String(txtPassword.getPassword());
            if (!password.isEmpty()) {
                usuarioSeleccionado.setContraseña(password);
            }
            
            usuarioSeleccionado.setTipoUsuario((String) cmbTipoUsuario.getSelectedItem());
            Socio socioSel = (Socio) cmbSocio.getSelectedItem();
            usuarioSeleccionado.setIdSocio(socioSel != null ? socioSel.getId() : null);
            usuarioSeleccionado.setEstado(chkEstado.isSelected());
            
            if (usuarioDAO.actualizar(usuarioSeleccionado)) {
                JOptionPane.showMessageDialog(this, 
                    "Usuario actualizado exitosamente", 
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
    
    private void eliminarUsuario() {
        if (usuarioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un usuario de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar este usuario?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (usuarioDAO.eliminar(usuarioSeleccionado.getId())) {
                JOptionPane.showMessageDialog(this, 
                    "Usuario eliminado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
            }
        }
    }
    
    private void cancelar() {
        limpiarCampos();
        usuarioSeleccionado = null;
        configurarEstadoBotones(false);
        tableUsuarios.clearSelection();
    }
    
    private void limpiarCampos() {
        txtUsername.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        cmbTipoUsuario.setSelectedIndex(0);
        cmbSocio.setSelectedIndex(0);
        chkEstado.setSelected(true);
    }
    
    private boolean validarCampos() {
        if (txtUsername.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty() ||
            txtPassword.getPassword().length == 0) {
            
            JOptionPane.showMessageDialog(this, 
                "Complete todos los campos obligatorios", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    private boolean validarCamposEdicion() {
        if (txtUsername.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Complete los campos obligatorios", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void configurarEstadoBotones(boolean modoGuardar) {
        btnGuardar.setEnabled(modoGuardar);
        btnNuevo.setEnabled(!modoGuardar);
        btnEditar.setEnabled(!modoGuardar && usuarioSeleccionado != null);
        btnEliminar.setEnabled(!modoGuardar && usuarioSeleccionado != null);
        btnCancelar.setEnabled(modoGuardar);
    }
}