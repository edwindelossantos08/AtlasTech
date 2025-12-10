
package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.dao.SocioDAO;
import com.atlastech.gestionclubdeportivos.models.Socio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

//Interfz de Gestion de Socios
 // @author AtlasTEch
 
public class GestionSocios extends JFrame {
    
    private SocioDAO socioDAO;
    private JTable tableSocios;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar, txtNombres, txtApellidos, txtEmail, txtTelefono, txtDireccion, txtFechaNac;
    private JCheckBox chkEstado;
    private JButton btnNuevo, btnGuardar, btnEditar, btnEliminar, btnCancelar;
    private Socio socioSeleccionado;
    private boolean modoEdicion = false;
    
    
    public GestionSocios(JFrame parent) {
        socioDAO = new SocioDAO();
        initComponents();
        cargarTabla();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setTitle("Gestión de Socios");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        //Panel Superior de Titulo y Busqueda
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(236, 240, 241));
        
        JLabel lblTitulo = new JLabel("👥 GESTIÓN DE SOCIOS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setBackground(new Color(52, 73, 94));
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JLabel lblBuscar = new JLabel("Buscar:");
        txtBuscar = new JTextField(20);
        JButton btnBuscar = crearBoton("🔍 Buscar", new Color(52, 152, 219));
        btnBuscar.addActionListener(e -> buscarSocios());
        
        JButton btnMostrarTodos = crearBoton("Mostrar Todos", new Color(149, 165, 166));
        btnMostrarTodos.addActionListener(e -> cargarTabla());
        
        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        searchPanel.add(btnBuscar);
        searchPanel.add(btnMostrarTodos);
        
        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(searchPanel, BorderLayout.EAST);
        
        //Panel central - Tabla
        String[] columnas = {"ID", "Nombres", "Apellidos", "Email", "Telefono", "Edad", "Categoría", "Estado"};
         modeloTabla = new DefaultTableModel(columnas, 0) {
             @Override
            public boolean isCellEditable(int row, int column){
               return false;
            }
        };
        
        
    tableSocios = new JTable(modeloTabla);
        tableSocios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableSocios.setRowHeight(25);
        tableSocios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableSocios.getTableHeader().setBackground(new Color(52, 73, 94));
        tableSocios.getTableHeader().setForeground(Color.WHITE);
        
        tableSocios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableSocios.getSelectedRow() != -1) {
                cargarSocioSeleccionado();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableSocios);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        
        // ========== PANEL DERECHO - FORMULARIO ==========
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
        
        // Título del formulario
        JLabel lblFormTitulo = new JLabel("DATOS DEL SOCIO");
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
        
        // Fecha Nacimiento
        agregarCampo(formPanel, gbc, row++, "Fecha Nac. (YYYY-MM-DD):");
        txtFechaNac = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtFechaNac, gbc);
        gbc.gridwidth = 1;
        
        // Dirección
        agregarCampo(formPanel, gbc, row++, "Dirección:");
        txtDireccion = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtDireccion, gbc);
        gbc.gridwidth = 1;
        
        // Estado
        chkEstado = new JCheckBox("Activo");
        chkEstado.setSelected(true);
        chkEstado.setOpaque(false);
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(chkEstado, gbc);
        
        // Panel de botones
        JPanel botonesPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        botonesPanel.setOpaque(false);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        
        btnNuevo = crearBoton("➕ Nuevo", new Color(52, 152, 219));
        btnGuardar = crearBoton("💾 Guardar", new Color(46, 204, 113));
        btnEditar = crearBoton("✏️ Editar", new Color(241, 196, 15));
        btnEliminar = crearBoton("🗑️ Eliminar", new Color(231, 76, 60));
        btnCancelar = crearBoton("❌ Cancelar", new Color(149, 165, 166));
        
        btnNuevo.addActionListener(e -> nuevoSocio());
        btnGuardar.addActionListener(e -> guardarSocio());
        btnEditar.addActionListener(e -> editarSocio());
        btnEliminar.addActionListener(e -> eliminarSocio());
        btnCancelar.addActionListener(e -> cancelar());
        
        botonesPanel.add(btnNuevo);
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnEditar);
        botonesPanel.add(btnEliminar);
        botonesPanel.add(btnCancelar);
        
        formPanel.add(botonesPanel, gbc);
        
        // ========== ENSAMBLAR PANELES ==========
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
        gbc.gridwidth = 1;
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
        List<Socio> socios = socioDAO.obtenerTodosSocios();
        
        for (Socio s : socios) {
            modeloTabla.addRow(new Object[]{
                s.getId(),
                s.getNombres(),
                s.getApellidos(),
                s.getEmail(),
                s.getTelefono(),
                s.calcularEdad(),
                s.obtenerCategoria(),
                s.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }
    
    private void buscarSocios() {
        String busqueda = txtBuscar.getText().trim();
        if (busqueda.isEmpty()) {
            cargarTabla();
            return;
        }
        
        modeloTabla.setRowCount(0);
        List<Socio> socios = socioDAO.buscarSociosPorNombre(busqueda);
        
        for (Socio s : socios) {
            modeloTabla.addRow(new Object[]{
                s.getId(),
                s.getNombres(),
                s.getApellidos(),
                s.getEmail(),
                s.getTelefono(),
                s.calcularEdad(),
                s.obtenerCategoria(),
                s.isEstado() ? "Activo" : "Inactivo"
            });
        }
    }
    
    private void cargarSocioSeleccionado() {
        int row = tableSocios.getSelectedRow();
        if (row == -1) return;
        
        int id = (int) modeloTabla.getValueAt(row, 0);
        socioSeleccionado = socioDAO.obtenerSocioPorId(id);
        
        if (socioSeleccionado != null) {
            txtNombres.setText(socioSeleccionado.getNombres());
            txtApellidos.setText(socioSeleccionado.getApellidos());
            txtEmail.setText(socioSeleccionado.getEmail());
            txtTelefono.setText(socioSeleccionado.getTelefono());
            txtFechaNac.setText(socioSeleccionado.getFechaNacimiento().toString());
            txtDireccion.setText(socioSeleccionado.getDireccion());
            chkEstado.setSelected(socioSeleccionado.isEstado());
            
            configurarEstadoBotones(false);
        }
    }
    
    private void nuevoSocio() {
        limpiarCampos();
        socioSeleccionado = null;
        modoEdicion = true;
        configurarEstadoBotones(true);
        txtNombres.requestFocus();
    }
    
    private void guardarSocio() {
        if (!validarCampos()) return;
        
        try {
            Socio socio = new Socio();
            socio.setNombres(txtNombres.getText().trim());
            socio.setApellidos(txtApellidos.getText().trim());
            socio.setEmail(txtEmail.getText().trim());
            socio.setTelefono(txtTelefono.getText().trim());
            socio.setFechaNacimiento(LocalDate.parse(txtFechaNac.getText().trim()));
            socio.setDireccion(txtDireccion.getText().trim());
            socio.setEstado(chkEstado.isSelected());
            
            if (socioDAO.insertarSocio(socio)) {
                JOptionPane.showMessageDialog(this, 
                    "Socio registrado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
                configurarEstadoBotones(false);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al registrar socio", 
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
    
    private void editarSocio() {
        if (socioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un socio de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validarCampos()) return;
        
        try {
            socioSeleccionado.setNombres(txtNombres.getText().trim());
            socioSeleccionado.setApellidos(txtApellidos.getText().trim());
            socioSeleccionado.setEmail(txtEmail.getText().trim());
            socioSeleccionado.setTelefono(txtTelefono.getText().trim());
            socioSeleccionado.setDireccion(txtDireccion.getText().trim());
            socioSeleccionado.setEstado(chkEstado.isSelected());
            
            if (socioDAO.actualizarSocio(socioSeleccionado)) {
                JOptionPane.showMessageDialog(this, 
                    "Socio actualizado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al actualizar socio", 
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
    
    private void eliminarSocio() {
        if (socioSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un socio de la tabla", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de cambiar el estado del socio?\nSe recomienda desactivar en lugar de eliminar.",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (socioDAO.cambiarEstadoSocio(socioSeleccionado.getId(), false)) {
                JOptionPane.showMessageDialog(this, 
                    "Socio desactivado exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
            }
        }
    }
    
    private void cancelar() {
        limpiarCampos();
        socioSeleccionado = null;
        modoEdicion = false;
        configurarEstadoBotones(false);
        tableSocios.clearSelection();
    }
    
    private void limpiarCampos() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtFechaNac.setText("");
        txtDireccion.setText("");
        chkEstado.setSelected(true);
    }
    
    private boolean validarCampos() {
        if (txtNombres.getText().trim().isEmpty() ||
            txtApellidos.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty() ||
            txtFechaNac.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Complete todos los campos obligatorios", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            LocalDate.parse(txtFechaNac.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de fecha inválido. Use: YYYY-MM-DD", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void configurarEstadoBotones(boolean modoGuardar) {
        btnGuardar.setEnabled(modoGuardar);
        btnNuevo.setEnabled(!modoGuardar);
        btnEditar.setEnabled(!modoGuardar && socioSeleccionado != null);
        btnEliminar.setEnabled(!modoGuardar && socioSeleccionado != null);
        btnCancelar.setEnabled(modoGuardar);
    }             
        
}

   