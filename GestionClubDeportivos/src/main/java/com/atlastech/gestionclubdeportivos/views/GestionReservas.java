package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.dao.*;
import com.atlastech.gestionclubdeportivos.models.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


 //Interfaz de Gestión de Reservas
 // @author AtlasTech
 
public class GestionReservas extends JFrame {
    
    private ReservaDAO reservaDAO;
    private SocioDAO socioDAO;
    private InstalacionDAO instalacionDAO;
    private JTable tableReservas;
    private DefaultTableModel modeloTabla;
    private JComboBox<Socio> cmbSocio;
    private JComboBox<Instalacion> cmbInstalacion;
    private JComboBox<String> cmbEstado, cmbTipoReserva;
    private JTextField txtFecha, txtHoraInicio, txtHoraFin, txtCosto, txtNotas;
    private JButton btnNuevo, btnGuardar, btnEditar, btnCancelarReserva, btnCancelar;
    private Reserva reservaSeleccionada;
    
    public GestionReservas(JFrame parent) {
        reservaDAO = new ReservaDAO();
        socioDAO = new SocioDAO();
        instalacionDAO = new InstalacionDAO();
        initComponents();
        cargarTabla();
        setLocationRelativeTo(parent);
    }
    
    private void initComponents() {
        setTitle("Gestión de Reservas");
        setSize(1300, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // ========== PANEL SUPERIOR ==========
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(236, 240, 241));
        
        JLabel lblTitulo = new JLabel("📅 GESTIÓN DE RESERVAS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(52, 73, 94));
        
        JButton btnRefrescar = crearBoton("🔄 Refrescar", new Color(52, 152, 219));
        btnRefrescar.addActionListener(e -> cargarTabla());
        
        JButton btnReservasHoy = crearBoton("📆 Hoy", new Color(155, 89, 182));
        btnReservasHoy.addActionListener(e -> mostrarReservasDelDia());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnReservasHoy);
        buttonPanel.add(btnRefrescar);
        
        topPanel.add(lblTitulo, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);
        
        // ========== TABLA ==========
        String[] columnas = {"ID", "Socio", "Instalación", "Fecha", "Hora Inicio", "Hora Fin", "Estado", "Tipo", "Costo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableReservas = new JTable(modeloTabla);
        tableReservas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableReservas.setRowHeight(25);
        tableReservas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tableReservas.getTableHeader().setBackground(new Color(52, 73, 94));
        tableReservas.getTableHeader().setForeground(Color.WHITE);
        
        tableReservas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tableReservas.getSelectedRow() != -1) {
                cargarReservaSeleccionada();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableReservas);
        
        // ========== FORMULARIO ==========
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        formPanel.setPreferredSize(new Dimension(380, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(3, 5, 3, 5);
        
        JLabel lblFormTitulo = new JLabel("DATOS DE RESERVA");
        lblFormTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblFormTitulo, gbc);
        
        gbc.gridwidth = 1;
        int row = 1;
        
        // Socio
        agregarCampo(formPanel, gbc, row++, "Socio:");
        cmbSocio = new JComboBox<>();
        cargarSocios();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(cmbSocio, gbc);
        gbc.gridwidth = 1;
        
        // Instalación
        agregarCampo(formPanel, gbc, row++, "Instalación:");
        cmbInstalacion = new JComboBox<>();
        cargarInstalaciones();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(cmbInstalacion, gbc);
        gbc.gridwidth = 1;
        
        // Fecha
        agregarCampo(formPanel, gbc, row++, "Fecha (YYYY-MM-DD):");
        txtFecha = new JTextField();
        txtFecha.setText(LocalDate.now().toString());
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtFecha, gbc);
        gbc.gridwidth = 1;
        
        // Hora Inicio
        agregarCampo(formPanel, gbc, row++, "Hora Inicio (HH:MM):");
        txtHoraInicio = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtHoraInicio, gbc);
        gbc.gridwidth = 1;
        
        // Hora Fin
        agregarCampo(formPanel, gbc, row++, "Hora Fin (HH:MM):");
        txtHoraFin = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtHoraFin, gbc);
        gbc.gridwidth = 1;
        
        // Estado
        agregarCampo(formPanel, gbc, row++, "Estado:");
        cmbEstado = new JComboBox<>(new String[]{"Confirmada", "Pendiente", "Cancelada", "Completada"});
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(cmbEstado, gbc);
        gbc.gridwidth = 1;
        
        // Tipo
        agregarCampo(formPanel, gbc, row++, "Tipo:");
        cmbTipoReserva = new JComboBox<>(new String[]{"Individual", "Grupal", "Torneo", "Entrenamiento"});
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(cmbTipoReserva, gbc);
        gbc.gridwidth = 1;
        
        // Costo
        agregarCampo(formPanel, gbc, row++, "Costo:");
        txtCosto = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtCosto, gbc);
        gbc.gridwidth = 1;
        
        // Notas
        agregarCampo(formPanel, gbc, row++, "Notas:");
        txtNotas = new JTextField();
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        formPanel.add(txtNotas, gbc);
        gbc.gridwidth = 1;
        
        // Botones
        JPanel botonesPanel = new JPanel(new GridLayout(5, 1, 3, 3));
        botonesPanel.setOpaque(false);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        
        btnNuevo = crearBoton("➕ Nuevo", new Color(52, 152, 219));
        btnGuardar = crearBoton("💾 Guardar", new Color(46, 204, 113));
        btnEditar = crearBoton("✏️ Editar", new Color(241, 196, 15));
        btnCancelarReserva = crearBoton("🚫 Cancelar Reserva", new Color(231, 76, 60));
        btnCancelar = crearBoton("❌ Cancelar", new Color(149, 165, 166));
        
        btnNuevo.addActionListener(e -> nuevaReserva());
        btnGuardar.addActionListener(e -> guardarReserva());
        btnEditar.addActionListener(e -> editarReserva());
        btnCancelarReserva.addActionListener(e -> cancelarReserva());
        btnCancelar.addActionListener(e -> cancelar());
        
        botonesPanel.add(btnNuevo);
        botonesPanel.add(btnGuardar);
        botonesPanel.add(btnEditar);
        botonesPanel.add(btnCancelarReserva);
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
        lbl.setFont(new Font("Arial", Font.BOLD, 10));
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
        btn.setFont(new Font("Arial", Font.BOLD, 10));
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
    
    private void cargarInstalaciones() {
        cmbInstalacion.removeAllItems();
        List<Instalacion> instalaciones = instalacionDAO.obtenerInstalacionesDisponibles();
        for (Instalacion i : instalaciones) {
            cmbInstalacion.addItem(i);
        }
        cmbInstalacion.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Instalacion) {
                    Instalacion i = (Instalacion) value;
                    setText(i.getId() + " - " + i.getNombre());
                }
                return this;
            }
        });
    }
    
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Reserva> reservas = reservaDAO.obtenerTodasReservas();
        
        for (Reserva r : reservas) {
            Socio socio = socioDAO.obtenerSocioPorId(r.getIdSocio());
            Instalacion inst = instalacionDAO.obtenerInstalacionPorId(r.getIdInstalacion());
            
            modeloTabla.addRow(new Object[]{
                r.getId(),
                socio != null ? socio.getNombreCompleto() : "N/A",
                inst != null ? inst.getNombre() : "N/A",
                r.getFecha(),
                r.getHoraInicio(),
                r.getHoraFin(),
                r.getEstado(),
                r.getTipoReserva(),
                "$" + r.getCosto()
            });
        }
    }
    
    private void mostrarReservasDelDia() {
        modeloTabla.setRowCount(0);
        List<Reserva> reservas = reservaDAO.obtenerReservasDelDia();
        
        for (Reserva r : reservas) {
            Socio socio = socioDAO.obtenerSocioPorId(r.getIdSocio());
            Instalacion inst = instalacionDAO.obtenerInstalacionPorId(r.getIdInstalacion());
            
            modeloTabla.addRow(new Object[]{
                r.getId(),
                socio != null ? socio.getNombreCompleto() : "N/A",
                inst != null ? inst.getNombre() : "N/A",
                r.getFecha(),
                r.getHoraInicio(),
                r.getHoraFin(),
                r.getEstado(),
                r.getTipoReserva(),
                "$" + r.getCosto()
            });
        }
    }
    
    private void cargarReservaSeleccionada() {
        int row = tableReservas.getSelectedRow();
        if (row == -1) return;
        
        int id = (int) modeloTabla.getValueAt(row, 0);
        reservaSeleccionada = reservaDAO.obtenerReservaPorId(id);
        
        if (reservaSeleccionada != null) {
            // Seleccionar socio
            for (int i = 0; i < cmbSocio.getItemCount(); i++) {
                Socio s = cmbSocio.getItemAt(i);
                if (s.getId() == reservaSeleccionada.getIdSocio()) {
                    cmbSocio.setSelectedIndex(i);
                    break;
                }
            }
            
            // Seleccionar instalación
            for (int i = 0; i < cmbInstalacion.getItemCount(); i++) {
                Instalacion inst = cmbInstalacion.getItemAt(i);
                if (inst.getId() == reservaSeleccionada.getIdInstalacion()) {
                    cmbInstalacion.setSelectedIndex(i);
                    break;
                }
            }
            
            txtFecha.setText(reservaSeleccionada.getFecha().toString());
            txtHoraInicio.setText(reservaSeleccionada.getHoraInicio().toString());
            txtHoraFin.setText(reservaSeleccionada.getHoraFin().toString());
            cmbEstado.setSelectedItem(reservaSeleccionada.getEstado());
            cmbTipoReserva.setSelectedItem(reservaSeleccionada.getTipoReserva());
            txtCosto.setText(reservaSeleccionada.getCosto().toString());
            txtNotas.setText(reservaSeleccionada.getNotas());
            
            configurarEstadoBotones(false);
        }
    }
    
    private void nuevaReserva() {
        limpiarCampos();
        reservaSeleccionada = null;
        configurarEstadoBotones(true);
    }
    
    private void guardarReserva() {
        if (!validarCampos()) return;
        
        try {
            Socio socio = (Socio) cmbSocio.getSelectedItem();
            Instalacion inst = (Instalacion) cmbInstalacion.getSelectedItem();
            
            Reserva reserva = new Reserva();
            reserva.setIdSocio(socio.getId());
            reserva.setIdInstalacion(inst.getId());
            reserva.setFecha(LocalDate.parse(txtFecha.getText().trim()));
            reserva.setHoraInicio(LocalTime.parse(txtHoraInicio.getText().trim()));
            reserva.setHoraFin(LocalTime.parse(txtHoraFin.getText().trim()));
            reserva.setEstado((String) cmbEstado.getSelectedItem());
            reserva.setTipoReserva((String) cmbTipoReserva.getSelectedItem());
            reserva.setCosto(new BigDecimal(txtCosto.getText().trim()));
            reserva.setNotas(txtNotas.getText().trim());
            
            if (reservaDAO.crearReserva(reserva)) {
                JOptionPane.showMessageDialog(this, 
                    "Reserva registrada exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
                configurarEstadoBotones(false);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarReserva() {
        if (reservaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una reserva", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validarCampos()) return;
        
        try {
            reservaSeleccionada.setFecha(LocalDate.parse(txtFecha.getText().trim()));
            reservaSeleccionada.setHoraInicio(LocalTime.parse(txtHoraInicio.getText().trim()));
            reservaSeleccionada.setHoraFin(LocalTime.parse(txtHoraFin.getText().trim()));
            reservaSeleccionada.setEstado((String) cmbEstado.getSelectedItem());
            reservaSeleccionada.setNotas(txtNotas.getText().trim());
            
            if (reservaDAO.actualizarReserva(reservaSeleccionada)) {
                JOptionPane.showMessageDialog(this, 
                    "Reserva actualizada exitosamente", 
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
    
    private void cancelarReserva() {
        if (reservaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione una reserva", 
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de cancelar esta reserva?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (reservaDAO.cancelarReserva(reservaSeleccionada.getId())) {
                JOptionPane.showMessageDialog(this, 
                    "Reserva cancelada exitosamente", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarTabla();
                limpiarCampos();
            }
        }
    }
    
    private void cancelar() {
        limpiarCampos();
        reservaSeleccionada = null;
        configurarEstadoBotones(false);
        tableReservas.clearSelection();
    }
    
    private void limpiarCampos() {
        if (cmbSocio.getItemCount() > 0) cmbSocio.setSelectedIndex(0);
        if (cmbInstalacion.getItemCount() > 0) cmbInstalacion.setSelectedIndex(0);
        txtFecha.setText(LocalDate.now().toString());
        txtHoraInicio.setText("");
        txtHoraFin.setText("");
        cmbEstado.setSelectedIndex(0);
        cmbTipoReserva.setSelectedIndex(0);
        txtCosto.setText("");
        txtNotas.setText("");
    }
    
    private boolean validarCampos() {
        if (cmbSocio.getSelectedItem() == null || cmbInstalacion.getSelectedItem() == null ||
            txtFecha.getText().trim().isEmpty() || txtHoraInicio.getText().trim().isEmpty() ||
            txtHoraFin.getText().trim().isEmpty() || txtCosto.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Complete todos los campos obligatorios", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            LocalDate.parse(txtFecha.getText().trim());
            LocalTime.parse(txtHoraInicio.getText().trim());
            LocalTime.parse(txtHoraFin.getText().trim());
            new BigDecimal(txtCosto.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Formato de fecha/hora/costo inválido", 
                "Validación", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void configurarEstadoBotones(boolean modoGuardar) {
        btnGuardar.setEnabled(modoGuardar);
        btnNuevo.setEnabled(!modoGuardar);
        btnEditar.setEnabled(!modoGuardar && reservaSeleccionada != null);
        btnCancelarReserva.setEnabled(!modoGuardar && reservaSeleccionada != null);
        btnCancelar.setEnabled(modoGuardar);
    }
}